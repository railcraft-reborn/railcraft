package mods.railcraft.world.entity.vehicle;

import java.util.EnumSet;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.TrackRelayerMenu;
import mods.railcraft.world.item.RailcraftItems;
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

  private static final EnumSet<Direction> HORIZONTAL_DIRECTION =
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
    if (level().isClientSide) {
      return;
    }
    if (this.getMode() == Mode.TRANSPORT) {
      return;
    }
    stockItems(SLOT_REPLACE, SLOT_STOCK);
    replace();
  }

  private void replace() {
    BlockPos pos = BlockPos.containing(position());

    if (TrackUtil.isRailBlockAt(level(), pos.below()))
      pos = pos.below();

    var blockstate = level().getBlockState(pos);
    var block = blockstate.getBlock();

    if (TrackUtil.isRail(block)) {
      ItemStack trackExist = patternContainer.getItem(SLOT_EXISTS);
      ItemStack trackStock = patternContainer.getItem(SLOT_STOCK);

      boolean nextToSuspended = false;
      //TODO: Implement when we have Dumping tracks
      /*for (var direction : HORIZONTAL_DIRECTION) {
        var blockEntity = level().getBlockEntity(pos.offset(direction.getNormal()));
        if (blockEntity instanceof IOutfittedTrackTile) {
          IOutfittedTrackTile track = (IOutfittedTrackTile) tile;
          if (track.getTrackKitInstance() instanceof TrackKitSuspended) {
            nextToSuspended = true;
            break;
          }
        }
      }*/

      if (nextToSuspended)
        return;

      if (!trackExist.isEmpty() && !trackStock.isEmpty())
        if (trackExist.getItem() instanceof BlockItem trackExistsBlockItem) {
          //ITrackItem trackItem = (ITrackItem) trackExist.getItem();
          if (trackExistsBlockItem.getBlock() == block) {
            var blockEntity = level().getBlockEntity(pos);
            //if (trackExistsBlockItem.isPlacedTileEntity(trackExist, blockEntity)) {
              var trackShape = removeOldTrack(pos, blockstate);
              placeNewTrack(pos, SLOT_STOCK, trackShape);
            //}
          }
        } else if (ContainerTools.isStackEqualToBlock(trackExist, block)) {
          var trackShape = removeOldTrack(pos, blockstate);
          placeNewTrack(pos, SLOT_STOCK, trackShape);
        }
    }
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    return SLOTS;
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    ItemStack trackReplace = patternContainer.getItem(SLOT_REPLACE);
    return ContainerTools.isItemEqual(stack, trackReplace);
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
    return new TrackRelayerMenu(id, inventory, this);
  }
}
