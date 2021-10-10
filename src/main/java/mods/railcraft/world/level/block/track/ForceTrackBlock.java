package mods.railcraft.world.level.block.track;

import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import mods.railcraft.world.level.block.entity.track.ForceTrackBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

/**
 * Created by CovertJaguar on 8/2/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public final class ForceTrackBlock extends TrackBlock {

  public static final EnumProperty<DyeColor> COLOR = ForceTrackEmitterBlock.COLOR;
  public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;

  public ForceTrackBlock(Properties properties) {
    super(TrackTypes.HIGH_SPEED, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(this.getShapeProperty(), RailShape.NORTH_SOUTH)
        .setValue(COLOR, ForceTrackEmitterBlock.DEFAULT_COLOR));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(COLOR);
  }

  @Override
  public Property<RailShape> getShapeProperty() {
    return SHAPE;
  }

  @Override
  protected BlockState updateDir(World level, BlockPos pos,
      BlockState blockState, boolean initialPlacement) {
    return blockState;
  }

  @Override
  public boolean canMakeSlopes(BlockState blockState, IBlockReader world, BlockPos pos) {
    return false;
  }

  @Override
  public boolean isFlexibleRail(BlockState blockState, IBlockReader world, BlockPos pos) {
    return false;
  }

  @Override
  public void neighborChanged(BlockState blockState,
      World level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean moved) {
    if (!neighborBlock.is(this)) {
      TileEntity tile = level.getBlockEntity(pos);
      if (tile instanceof ForceTrackBlockEntity) {
        ((ForceTrackBlockEntity) tile).neighborChanged();
      }
    }
  }

  @Override
  public float getRailMaxSpeed(BlockState blockState, World world, BlockPos pos,
      AbstractMinecartEntity cart) {
    return 0.6F;
  }

  @Override
  public boolean canBeReplacedByLeaves(BlockState blockState, IWorldReader world, BlockPos pos) {
    return true;
  }

  @Override
  public boolean canBeReplaced(BlockState blockState, BlockItemUseContext context) {
    return true;
  }

  @Override
  public boolean hasTileEntity(BlockState blockState) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState blockState, IBlockReader level) {
    return new ForceTrackBlockEntity();
  }
}
