package mods.railcraft.world.entity.vehicle;

import java.util.EnumSet;
import java.util.Set;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.TrackRelayerMenu;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.entity.track.DumpingTrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TrackRelayer extends MaintenancePatternMinecart {

  private static final int SLOT_STOCK = 0;
  private static final int SLOT_EXISTS = 0;
  private static final int SLOT_REPLACE = 1;
  private static final int[] SLOTS = ContainerTools.buildSlotArray(0, 1);

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
  public Item getDropItem() {
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
    this.stockItems(SLOT_REPLACE, SLOT_STOCK);
    this.replace();
  }

  private void replace() {
    var pos = BlockPos.containing(position());

    if (TrackUtil.isRailBlockAt(level(), pos.below())) {
      pos = pos.below();
    }

    var blockstate = level().getBlockState(pos);
    var block = blockstate.getBlock();

    if (TrackUtil.isRail(block)) {
      var trackExist = this.patternContainer.getItem(SLOT_EXISTS);
      var trackStock = this.patternContainer.getItem(SLOT_STOCK);

      boolean nextToSuspended = false;
      for (var direction : HORIZONTAL_DIRECTION) {
        var blockEntity = level().getBlockEntity(pos.offset(direction.getNormal()));
        if (blockEntity instanceof DumpingTrackBlockEntity) {
          nextToSuspended = true;
          break;
        }
      }

      if (nextToSuspended) {
        return;
      }

      if (!trackExist.isEmpty() && !trackStock.isEmpty()) {
        if (trackExist.getItem() instanceof BlockItem trackExistsBlockItem) {
          if (trackExistsBlockItem.getBlock() == block) {
            var trackShape = removeOldTrack(pos, blockstate);
            placeNewTrack(pos, SLOT_STOCK, trackShape);
          }
        } else if (ContainerTools.isStackEqualToBlock(trackExist, block)) {
          var trackShape = removeOldTrack(pos, blockstate);
          placeNewTrack(pos, SLOT_STOCK, trackShape);
        }
      }
    }
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    return SLOTS;
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    var trackReplace = this.patternContainer.getItem(SLOT_REPLACE);
    return ContainerTools.isItemEqual(stack, trackReplace);
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
    return new TrackRelayerMenu(id, inventory, this);
  }
}
