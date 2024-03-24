package mods.railcraft.world.level.block.track.outfitted;

import java.util.Arrays;
import java.util.function.Supplier;
import mods.railcraft.api.item.Crowbar;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.api.track.TrackUtil;
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
import net.neoforged.neoforge.fluids.IFluidBlock;

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
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
      InteractionHand hand, BlockHitResult rayTraceResult) {
    var itemStack = player.getItemInHand(hand);
    if (itemStack.getItem() instanceof Crowbar crowbar
        && crowbar.canWhack(player, hand, itemStack, pos)
        && this.crowbarWhack(state, level, pos, player, hand, itemStack)) {
      crowbar.onWhack(player, hand, itemStack, pos);
      return InteractionResult.sidedSuccess(level.isClientSide());
    }
    return InteractionResult.PASS;
  }

  protected boolean crowbarWhack(BlockState state, Level level, BlockPos pos,
      Player player, InteractionHand hand, ItemStack itemStack) {
    return false;
  }

  @Override
  public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
      boolean willHarvest, FluidState fluid) {
    var newState = TrackUtil.setShape(this.getTrackType().getFlexBlock(),
        TrackUtil.getRailShapeRaw(state));
    boolean result = level.setBlockAndUpdate(pos, newState);
    // Below is ugly workaround for fluids!
    if (Arrays.stream(Direction.values())
        .map(pos::relative)
        .map(level::getBlockState)
        .map(BlockState::getBlock)
        .anyMatch(block -> block instanceof IFluidBlock || block instanceof LiquidBlock)) {
      Block.dropResources(newState, level, pos);
    }
    return result;
  }

  @Override
  public boolean isFlexibleRail(BlockState state, BlockGetter world, BlockPos pos) {
    return false;
  }
}
