package mods.railcraft.world.level.block.entity.manipulator;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.world.entity.vehicle.MinecartUtil;
import mods.railcraft.world.inventory.CartDispenserMenu;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.SharedConstants;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

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
    EntitySearcher.findMinecarts()
        .at(this.getBlockPos().offset(this.getFacing().getNormal()))
        .stream(serverLevel)
        .findAny()
        .ifPresentOrElse(cart -> {
          if (!cart.isAlive()) {
            return;
          }
          var coppiedContainer = AdvancedContainer.copyOf(this);
          var cartStack = cart.getPickResult();
          if (cart.hasCustomName()) {
            cartStack.setHoverName(cart.getName());
          }
          var remainder = coppiedContainer.insert(cartStack.copy());
          if (remainder.isEmpty()) {
            this.insert(cartStack);
            if (!cart.getPassengers().isEmpty()) {
              MinecartUtil.removePassengers(cart);
            }
            cart.kill();
          }
        }, () -> {
          if (this.timeSinceLastSpawn > RailcraftConfig.SERVER.cartDispenserDelay.get() * SharedConstants.TICKS_PER_SECOND) {
            for (int i = 0; i < this.getContainerSize(); i++) {
              var cartStack = this.getItem(i);
              if (!cartStack.isEmpty()) {
                var pos = this.getBlockPos().offset(this.getFacing().getNormal());
                var placedCart = MinecartUtil.placeCart(cartStack, serverLevel, pos);

                if (placedCart != null) {
                  this.removeItem(i, 1);
                  this.timeSinceLastSpawn = 0;
                  break;
                } else {
                  LevelUtil.spewItem(cartStack, this.level, pos.getX(), pos.getY(), pos.getZ());
                  this.setItem(i, ItemStack.EMPTY);
                }
              }
            }
          }
        });
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new CartDispenserMenu(id, inventory, this);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putBoolean(CompoundTagKeys.POWERED, this.powered);
    tag.putInt(CompoundTagKeys.TIME_SINCE_LAST_SPAWN, this.timeSinceLastSpawn);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.powered = tag.getBoolean(CompoundTagKeys.POWERED);
    this.timeSinceLastSpawn = tag.getInt(CompoundTagKeys.TIME_SINCE_LAST_SPAWN);
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
