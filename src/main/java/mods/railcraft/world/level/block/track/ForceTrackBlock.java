package mods.railcraft.world.level.block.track;

import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import mods.railcraft.world.level.block.entity.track.ForceTrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;

/**
 * Created by CovertJaguar on 8/2/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public final class ForceTrackBlock extends TrackBlock implements EntityBlock {

  public static final EnumProperty<DyeColor> COLOR = ForceTrackEmitterBlock.COLOR;
  public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;

  public ForceTrackBlock(Properties properties) {
    super(TrackTypes.HIGH_SPEED, properties);
  }

  @Override
  protected BlockState buildDefaultState(BlockState blockState) {
    return super.buildDefaultState(blockState)
        .setValue(COLOR, ForceTrackEmitterBlock.DEFAULT_COLOR);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(COLOR);
  }

  @Override
  public Property<RailShape> getShapeProperty() {
    return SHAPE;
  }

  @Override
  protected BlockState updateDir(Level level, BlockPos pos,
      BlockState blockState, boolean initialPlacement) {
    return blockState;
  }

  @Override
  public boolean canMakeSlopes(BlockState blockState, BlockGetter world, BlockPos pos) {
    return false;
  }

  @Override
  public boolean isFlexibleRail(BlockState blockState, BlockGetter world, BlockPos pos) {
    return false;
  }

  @Override
  public void neighborChanged(BlockState blockState,
      Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean moved) {
    if (neighborBlock != this) {
      BlockEntity tile = level.getBlockEntity(pos);
      if (tile instanceof ForceTrackBlockEntity) {
        ((ForceTrackBlockEntity) tile).neighborChanged();
      }
    }
  }

  @Override
  public float getRailMaxSpeed(BlockState blockState, Level world, BlockPos pos,
      AbstractMinecart cart) {
    return 0.6F;
  }

  @Override
  public boolean canBeReplaced(BlockState blockState, BlockPlaceContext context) {
    return true;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new ForceTrackBlockEntity(blockPos, blockState);
  }
}
