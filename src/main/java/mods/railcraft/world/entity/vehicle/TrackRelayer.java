package mods.railcraft.world.entity.vehicle;

import java.util.EnumSet;
import java.util.Set;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.TrackRelayerMenu;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.entity.track.DumpingTrackBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;

public class TrackRelayer extends MaintenancePatternMinecart {

  private static final int STOCK_SLOT = 0;
  private static final int REPLACE_SLOT = 1;
  private static final int[] SLOTS = ContainerTools.buildSlotArray(0, 1);

  // Pattern container slot
  private static final int EXISTING_TRACK_SLOT = 0;

  private static final Set<Direction> HORIZONTAL_DIRECTION =
      EnumSet.of(Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH);

  public TrackRelayer(EntityType<?> type, Level level) {
    super(type, level);
  }

  public TrackRelayer(ItemStack itemStack, double x, double y, double z, ServerLevel level) {
    super(RailcraftEntityTypes.TRACK_RELAYER.get(), x, y, z, level);
  }

  @Override
  public ItemStack getPickResult() {
    return RailcraftItems.TRACK_RELAYER.get().getDefaultInstance();
  }

  @Override
  protected Item getDropItem() {
    return RailcraftItems.TRACK_RELAYER.get();
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level().isClientSide()) {
      return;
    }
    if (this.mode() == Mode.OFF) {
      return;
    }
    this.stockItems(REPLACE_SLOT, STOCK_SLOT);
    this.replace();
  }

  private void replace() {
    var pos = this.blockPosition();
    if (BaseRailBlock.isRail(this.level(), pos.below())) {
      pos = pos.below();
    }

    var blockState = this.level().getBlockState(pos);
    if (!BaseRailBlock.isRail(blockState)) {
      return;
    }

    var block = blockState.getBlock();

    var existingTrack = this.patternContainer.getItem(EXISTING_TRACK_SLOT);
    if (existingTrack.isEmpty()) {
      return;
    }
    var stockTrack = this.getItem(STOCK_SLOT);
    if (stockTrack.isEmpty()) {
      return;
    }

    boolean nextToSuspended = false;
    for (var direction : HORIZONTAL_DIRECTION) {
      var blockEntity = this.level().getBlockEntity(pos.offset(direction.getNormal()));
      if (blockEntity instanceof DumpingTrackBlockEntity) {
        nextToSuspended = true;
        break;
      }
    }

    if (nextToSuspended) {
      return;
    }

    if (ContainerTools.isItemStackBlock(existingTrack, block)) {
      var trackShape = this.removeOldTrack(pos, blockState);
      this.placeNewTrack(pos, STOCK_SLOT, trackShape);
    }
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    return SLOTS;
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    var trackReplace = this.patternContainer.getItem(REPLACE_SLOT);
    return ItemStack.isSameItem(stack, trackReplace);
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
    return new TrackRelayerMenu(id, inventory, this);
  }
}
