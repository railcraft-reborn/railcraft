package mods.railcraft.world.level.block.track.outfitted;

import java.util.Arrays;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import mods.railcraft.api.items.Crowbar;
import mods.railcraft.api.tracks.ReversibleTrack;
import mods.railcraft.api.tracks.TrackToolsAPI;
import mods.railcraft.api.tracks.TrackType;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.level.block.track.TrackBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;

public class OutfittedTrackBlock extends TrackBlock {

  public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;

  public OutfittedTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  public Property<RailShape> getShapeProperty() {
    return SHAPE;
  }

  @Override
  public ActionResultType use(BlockState blockState, World level, BlockPos pos, PlayerEntity player,
      Hand hand, BlockRayTraceResult rayTraceResult) {
    ItemStack heldItem = player.getItemInHand(hand);
    if (heldItem.getItem() instanceof Crowbar) {
      Crowbar crowbar = (Crowbar) heldItem.getItem();
      if (crowbar.canWhack(player, hand, heldItem, pos)
          && this.onCrowbarWhack(blockState, level, pos, player, hand, heldItem)) {
        crowbar.onWhack(player, hand, heldItem, pos);
        return ActionResultType.SUCCESS;
      }
    }
    return ActionResultType.CONSUME;
  }

  public boolean onCrowbarWhack(BlockState blockState, World level, BlockPos pos,
      PlayerEntity player, Hand hand,
      @Nullable ItemStack heldItem) {
    if (this instanceof ReversibleTrack) {
      ReversibleTrack track = (ReversibleTrack) this;
      track.setReversed(blockState, level, pos, !track.isReversed(blockState, level, pos));
      return true;
    }
    return false;
  }

  @Override
  public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player,
      boolean willHarvest, FluidState fluid) {
    BlockState newState = TrackToolsAPI.setShape(this.getTrackType().getBaseBlock(),
        TrackTools.getTrackDirectionRaw(state));
    boolean result = world.setBlockAndUpdate(pos, newState);
    // Below is ugly workaround for fluids!
    if (Arrays.stream(Direction.values())
        .map(pos::relative)
        .map(world::getBlockState)
        .map(BlockState::getBlock)
        .anyMatch(block -> block instanceof IFluidBlock || block instanceof FlowingFluidBlock)) {
      Block.dropResources(newState, world, pos);
    }
    return result;
  }

  @Override
  public boolean isFlexibleRail(BlockState state, IBlockReader world, BlockPos pos) {
    return false;
  }
}
