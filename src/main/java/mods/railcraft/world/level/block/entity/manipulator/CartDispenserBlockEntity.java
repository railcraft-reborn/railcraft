package mods.railcraft.world.level.block.entity.manipulator;

import mods.railcraft.RailcraftConfig;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.util.container.ContainerCopy;
import mods.railcraft.world.entity.vehicle.CartTools;
import mods.railcraft.world.inventory.CartDispenserMenu;
import mods.railcraft.world.item.CartItem;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CartDispenserBlockEntity extends ManipulatorBlockEntity implements MenuProvider {

  protected boolean powered;
  protected int timeSinceLastSpawn;

  public CartDispenserBlockEntity(BlockPos pos, BlockState state) {
    super(RailcraftBlockEntityTypes.CART_DISPENSER.get(), pos, state);
    this.setContainerSize(3);
  }

  protected CartDispenserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      CartDispenserBlockEntity blockEntity) {
    if (blockEntity.timeSinceLastSpawn < Integer.MAX_VALUE) {
      blockEntity.timeSinceLastSpawn++;
    }
  }

  @Override
  protected boolean hasWorkForCart(AbstractMinecart cart) {
    return false;
  }

  @Override
  protected void processCart(AbstractMinecart cart) {}

  public void onNeighborChange() {
    if (!(this.level instanceof ServerLevel serverLevel)) {
      return;
    }

    boolean newPowered = this.isPowered();
    if (!this.powered && newPowered) {
      this.powered = true;
      this.setPowered(true);
      this.onPulse(serverLevel);
    } else {
      this.powered = newPowered;
      this.setPowered(this.powered);
    }
  }

  protected void onPulse(ServerLevel serverLevel) {

    var cart = EntitySearcher.findMinecarts()
        .around(this.getBlockPos().offset(this.getFacing().getNormal()))
        .search(serverLevel)
        .any();

    if (cart == null) {
      if (this.timeSinceLastSpawn > RailcraftConfig.server.cartDispenserDelay.get() * 20) {
        for (int i = 0; i < this.getContainerSize(); i++) {
          ItemStack cartStack = this.getItem(i);
          if (!cartStack.isEmpty()) {
            BlockPos pos = this.getBlockPos().offset(this.getFacing().getNormal());
            AbstractMinecart placedCart = null;
            var placedStack = cartStack.copy();
            if (cartStack.getItem() instanceof CartItem cartItem) {
              placedCart = CartTools.placeCart(cartItem.getMinecartFactory(), placedStack,
                  serverLevel, pos);
            } else if (cartStack.getItem() instanceof MinecartItem minecartItem) {
              placedCart = CartTools.placeCart(minecartItem.type, placedStack, serverLevel, pos);
            }

            if (placedCart != null) {
              this.removeItem(i, 1);
              this.timeSinceLastSpawn = 0;
              break;
            } else {
              LevelUtil.spewItem(cartStack, level, pos.getX(), pos.getY(), pos.getZ());
              this.setItem(i, ItemStack.EMPTY);
            }
          }
        }
      }
    } else if (cart.isAlive()) {
      ContainerCopy testInv = new ContainerCopy(this);
      ItemStack cartStack = new ItemStack(cart.getDropItem());
      if (cart.hasCustomName())
        cartStack.setHoverName(cart.getName());
      ItemStack remainder = testInv.addStack(cartStack.copy());
      if (remainder.isEmpty()) {
        this.addStack(cartStack);
        if (!cart.getPassengers().isEmpty())
          CartTools.removePassengers(cart);
        cart.kill();
      }
    }
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new CartDispenserMenu(id, inventory, this);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putBoolean("powered", this.powered);
    tag.putInt("timeSinceLastSpawn", this.timeSinceLastSpawn);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.powered = tag.getBoolean("powered");
    this.timeSinceLastSpawn = tag.getInt("timeSinceLastSpawn");
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeBoolean(this.powered);
    data.writeInt(this.timeSinceLastSpawn);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.powered = data.readBoolean();
    this.timeSinceLastSpawn = data.readInt();
  }
}
