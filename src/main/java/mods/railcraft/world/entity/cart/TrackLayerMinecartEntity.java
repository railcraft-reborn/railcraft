package mods.railcraft.world.entity.cart;

import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.IPlantable;

public class TrackLayerMinecartEntity extends MaintenancePatternMinecartEntity {

  public static final int SLOT_STOCK = 0;
  public static final int SLOT_REPLACE = 0;
  public static final int[] SLOTS = InvTools.buildSlotArray(0, 1);

  public TrackLayerMinecartEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  public TrackLayerMinecartEntity(ItemStack itemStack, double x, double y, double z,
      ServerWorld world) {
    super(RailcraftEntityTypes.TRACK_LAYER.get(), x, y, z, world);
  }

  @Override
  public ItemStack getCartItem() {
    return RailcraftItems.TRACK_LAYER.get().getDefaultInstance();
  }

  @Override
  protected void moveAlongTrack(BlockPos pos, BlockState state) {
    super.moveAlongTrack(pos, state);
    if (this.level.isClientSide())
      return;

    stockItems(SLOT_REPLACE, SLOT_STOCK);
    updateTravelDirection(pos, state);
    if (travelDirection != null)
      placeTrack(pos);
  }

  private void placeTrack(BlockPos pos) {
    if (getMode() == Mode.TRANSPORT)
      return;
    pos = pos.relative(travelDirection);

    RailShape trackShape = RailShape.NORTH_SOUTH;
    if (travelDirection == Direction.EAST || travelDirection == Direction.WEST)
      trackShape = RailShape.EAST_WEST;
    if (!isValidReplacementBlock(pos) && isValidReplacementBlock(pos.above())
        && TrackShapeHelper.isStraight(trackShape))
      pos = pos.above();
    if (isValidReplacementBlock(pos) && isValidReplacementBlock(pos.below())) {
      pos = pos.below();
      if (travelDirection == Direction.NORTH)
        trackShape = RailShape.ASCENDING_SOUTH;
      if (travelDirection == Direction.SOUTH)
        trackShape = RailShape.ASCENDING_NORTH;
      if (travelDirection == Direction.WEST)
        trackShape = RailShape.ASCENDING_WEST;
      if (travelDirection == Direction.EAST)
        trackShape = RailShape.ASCENDING_EAST;
    }

    if (isValidNewTrackPosition(pos)) {
      BlockState targetState = this.level.getBlockState(pos);
      if (placeNewTrack(pos, SLOT_STOCK, trackShape)) {
        Block.dropResources(targetState, this.level, pos);
      }
    }
  }

  private boolean isValidNewTrackPosition(BlockPos pos) {
    return isValidReplacementBlock(pos) && Block.canSupportRigidBlock(this.level, pos.below());
  }

  @SuppressWarnings("deprecation")
  private boolean isValidReplacementBlock(BlockPos pos) {
    BlockState state = this.level.getBlockState(pos);
    Block block = state.getBlock();
    return (state.isAir(this.level, pos) ||
        block instanceof IPlantable ||
        block instanceof IForgeShearable ||
        TunnelBoreEntity.replaceableTags.stream().anyMatch(state::is) ||
        TunnelBoreEntity.replaceableBlocks.contains(block));
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    return SLOTS;
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    ItemStack trackReplace = patternInventory.getItem(SLOT_REPLACE);
    return InvTools.isItemEqual(stack, trackReplace);
  }

  @Override
  protected Container createMenu(int p_213968_1_, PlayerInventory p_213968_2_) {
    return null;
  }
}
