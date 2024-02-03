package mods.railcraft.world.level.block.entity.track;

import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.container.manipulator.ContainerManipulator;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.world.entity.vehicle.MinecartUtil;
import mods.railcraft.world.inventory.DumpingTrackMenu;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;

public class DumpingTrackBlockEntity extends RailcraftBlockEntity implements MenuProvider {

  private static final int ITEM_DROP_INTERVAL = 16;
  private static final List<Direction> DIRECTION =
      List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.DOWN);

  private final AdvancedContainer cartFilter, itemFilter;
  private final Predicate<ItemStack> itemMatcher;

  private int ticksSinceLastDrop;

  public DumpingTrackBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.DUMPING_TRACK.get(), blockPos, blockState);
    this.cartFilter = new AdvancedContainer(3).listener(x -> this.setChanged()).phantom();
    this.itemFilter = new AdvancedContainer(9).listener(x -> this.setChanged()).phantom();
    this.itemMatcher = StackFilter.anyMatch(itemFilter);
    this.ticksSinceLastDrop = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      DumpingTrackBlockEntity blockEntity) {
    blockEntity.ticksSinceLastDrop++;
  }

  @Nullable
  private static BlockEntity getBlockEntityAround(Level level, BlockPos pos) {
    for (var direction : DIRECTION) {
      var blockEntity = level.getBlockEntity(pos.relative(direction));
      if (blockEntity != null && level.getCapability(Capabilities.ItemHandler.BLOCK,
          blockEntity.getBlockPos(), null) != null) {
        return blockEntity;
      }
    }
    return null;
  }

  public AdvancedContainer getCartFilter() {
    return cartFilter;
  }

  public AdvancedContainer getItemFilter() {
    return itemFilter;
  }

  public void minecartPassed(AbstractMinecart cart, boolean powered) {
    if (powered) {
      return;
    }

    if (!cartFilter.isEmpty()) {
      boolean sameCart = cartFilter.stream()
          .anyMatch(slot -> slot.is(cart.getPickResult().getItem()));
      if (!sameCart) {
        return;
      }
    }

    if (!cart.getPassengers().isEmpty() && tryDumpRider(cart)) {
      return;
    }

    tryDumpInventory(cart);
  }

  private boolean tryDumpRider(AbstractMinecart cart) {
    MinecartUtil.removePassengers(cart);
    return true;
  }

  private void tryDumpInventory(AbstractMinecart cart) {
    if (ticksSinceLastDrop < ITEM_DROP_INTERVAL) {
      return;
    }

    var itemHandler = cart.getCapability(Capabilities.ItemHandler.ENTITY);
    if (itemHandler != null) {
      var cartInv = ContainerManipulator.of(itemHandler);
      if (!cartInv.hasItems()) {
        return;
      }
      this.ticksSinceLastDrop = 0;

      var blockEntity = getBlockEntityAround(this.level, this.blockPos());
      if (blockEntity == null) {
        var below = this.blockPos().below();
        if (this.itemFilter.isEmpty()) {
          cartInv.streamItems()
              .forEach(itemStack -> Containers.dropItemStack(this.level,
                  below.getX(), below.getY(), below.getZ(),
                  itemStack));
        } else {
          cartInv.streamItems()
              .filter(this.itemMatcher)
              .forEach(itemStack -> Containers.dropItemStack(this.level,
                  below.getX(), below.getY(), below.getZ(),
                  itemStack));
        }
        return;
      }
      var itemHandlerBlockEntity = this.level
          .getCapability(Capabilities.ItemHandler.BLOCK, blockEntity.getBlockPos(), null);
      if (itemHandlerBlockEntity != null) {
        var blockInv = ContainerManipulator.of(itemHandlerBlockEntity);
        cartInv.moveOneItemStackTo(blockInv);
      }
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put(CompoundTagKeys.CART_FILTER, this.cartFilter.createTag());
    tag.put(CompoundTagKeys.ITEM_FILTER, this.itemFilter.createTag());
    tag.putInt(CompoundTagKeys.TICKS_SINCE_LAST_DROP, this.ticksSinceLastDrop);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.cartFilter.fromTag(tag.getList(CompoundTagKeys.CART_FILTER, Tag.TAG_COMPOUND));
    this.itemFilter.fromTag(tag.getList(CompoundTagKeys.ITEM_FILTER, Tag.TAG_COMPOUND));
    this.ticksSinceLastDrop = tag.getInt(CompoundTagKeys.TICKS_SINCE_LAST_DROP);
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new DumpingTrackMenu(id, inventory, this);
  }
}
