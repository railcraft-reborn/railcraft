package mods.railcraft.world.entity.vehicle;

import mods.railcraft.api.track.RailShapeUtil;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.inventory.TrackLayerMenu;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.IPlantable;

public class TrackLayer extends MaintenancePatternMinecart {

  private static final int SLOT_STOCK = 0;
  private static final int SLOT_REPLACE = 0;
  private static final int[] SLOTS = ContainerTools.buildSlotArray(0, 1);

  public TrackLayer(EntityType<?> type, Level level) {
    super(type, level);
  }

  public TrackLayer(ItemStack itemStack, double x, double y, double z, ServerLevel level) {
    super(itemStack, RailcraftEntityTypes.TRACK_LAYER.get(), x, y, z, level);
  }

  @Override
  public ItemStack getPickResult() {
    return RailcraftItems.TRACK_LAYER.get().getDefaultInstance();
  }

  @Override
  public Item getDropItem() {
    return RailcraftItems.TRACK_LAYER.get();
  }

  @Override
  protected void moveAlongTrack(BlockPos pos, BlockState state) {
    super.moveAlongTrack(pos, state);
    if (this.level().isClientSide()) {
      return;
    }

    this.stockItems(SLOT_REPLACE, SLOT_STOCK);
    this.updateTravelDirection(pos, state);
    this.travelDirection().ifPresent(direction -> this.placeTrack(pos, direction));
  }

  private void placeTrack(BlockPos pos, Direction direction) {
    if (this.mode() == Mode.OFF) {
      return;
    }

    pos = pos.relative(direction);

    var railShape = RailShape.NORTH_SOUTH;
    if (direction == Direction.EAST || direction == Direction.WEST) {
      railShape = RailShape.EAST_WEST;
    }

    if (!this.isValidReplacementBlock(pos)
        && this.isValidReplacementBlock(pos.above())
        && !RailShapeUtil.isTurn(railShape)) {
      pos = pos.above();
    }

    if (this.isValidReplacementBlock(pos) && this.isValidReplacementBlock(pos.below())) {
      pos = pos.below();
      railShape = switch (direction) {
        case NORTH -> RailShape.ASCENDING_SOUTH;
        case SOUTH -> RailShape.ASCENDING_NORTH;
        case WEST -> RailShape.ASCENDING_WEST;
        case EAST -> RailShape.ASCENDING_EAST;
        default -> throw new IllegalArgumentException("Unexpected value: " + direction);
      };
    }

    if (this.isValidNewTrackPosition(pos)) {
      var targetState = this.level().getBlockState(pos);
      if (this.placeNewTrack(pos, SLOT_STOCK, railShape)) {
        Block.dropResources(targetState, this.level(), pos);
      }
    }
  }

  private boolean isValidNewTrackPosition(BlockPos pos) {
    return this.isValidReplacementBlock(pos)
        && Block.canSupportRigidBlock(this.level(), pos.below());
  }

  private boolean isValidReplacementBlock(BlockPos pos) {
    var state = this.level().getBlockState(pos);
    var block = state.getBlock();
    return (state.isAir() ||
        block instanceof IPlantable ||
        block instanceof IForgeShearable ||
        state.is(RailcraftTags.Blocks.TUNNEL_BORE_REPLACEABLE_BLOCKS));
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    return SLOTS;
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    var trackReplace = this.patternContainer.getItem(SLOT_REPLACE);
    return ItemStack.isSameItem(stack, trackReplace);
  }

  @Override
  protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
    return new TrackLayerMenu(id, inventory, this);
  }
}
