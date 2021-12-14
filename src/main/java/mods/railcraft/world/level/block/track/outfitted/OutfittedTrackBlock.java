package mods.railcraft.world.level.block.track.outfitted;

import java.util.Arrays;
import java.util.function.Supplier;
import mods.railcraft.api.item.Crowbar;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.level.block.track.TrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
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
  public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player,
      InteractionHand hand, BlockHitResult rayTraceResult) {
    ItemStack itemStack = player.getItemInHand(hand);
    if (itemStack.getItem() instanceof Crowbar) {
      Crowbar crowbar = (Crowbar) itemStack.getItem();
      if (crowbar.canWhack(player, hand, itemStack, pos)
          && this.crowbarWhack(blockState, level, pos, player, hand, itemStack)) {
        crowbar.onWhack(player, hand, itemStack, pos);
        return InteractionResult.SUCCESS;
      }
    }
    return InteractionResult.PASS;
  }

  protected boolean crowbarWhack(BlockState blockState, Level level, BlockPos pos,
      Player player, InteractionHand hand, ItemStack itemStack) {
    return false;
  }

  @Override
  public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player,
      boolean willHarvest, FluidState fluid) {
    BlockState newState = TrackUtil.setShape(this.getTrackType().getFlexBlock(),
        TrackTools.getRailShapeRaw(state));
    boolean result = world.setBlockAndUpdate(pos, newState);
    // Below is ugly workaround for fluids!
    if (Arrays.stream(Direction.values())
        .map(pos::relative)
        .map(world::getBlockState)
        .map(BlockState::getBlock)
        .anyMatch(block -> block instanceof IFluidBlock || block instanceof LiquidBlock)) {
      Block.dropResources(newState, world, pos);
    }
    return result;
  }

  @Override
  public boolean isFlexibleRail(BlockState state, BlockGetter world, BlockPos pos) {
    return false;
  }
}
