package mods.railcraft.world.level.block.track.outfitted;

import java.util.Arrays;
import java.util.function.Supplier;
import mods.railcraft.api.item.Crowbar;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.api.track.TrackUtil;
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
    ItemStack itemStack = player.getItemInHand(hand);
    if (itemStack.getItem() instanceof Crowbar) {
      Crowbar crowbar = (Crowbar) itemStack.getItem();
      if (crowbar.canWhack(player, hand, itemStack, pos)
          && this.crowbarWhack(blockState, level, pos, player, hand, itemStack)) {
        crowbar.onWhack(player, hand, itemStack, pos);
        return ActionResultType.SUCCESS;
      }
    }
    return ActionResultType.CONSUME;
  }

  protected boolean crowbarWhack(BlockState blockState, World level, BlockPos pos,
      PlayerEntity player, Hand hand, ItemStack itemStack) {
    return false;
  }

  @Override
  public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player,
      boolean willHarvest, FluidState fluid) {
    BlockState newState = TrackUtil.setShape(this.getTrackType().getBaseBlock(),
        TrackTools.getRailShapeRaw(state));
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
