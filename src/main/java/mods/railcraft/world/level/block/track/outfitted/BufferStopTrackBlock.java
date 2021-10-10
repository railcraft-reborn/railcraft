package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class BufferStopTrackBlock extends DirectionalOutfittedTrackBlock {

  private static final VoxelShape BUFFER_STOP_SHAPE = box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
  private static final VoxelShape SHAPE = VoxelShapes.or(FLAT_AABB, BUFFER_STOP_SHAPE);

  public BufferStopTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  public VoxelShape getShape(BlockState blockState, IBlockReader level, BlockPos pos,
      ISelectionContext context) {
    return SHAPE;
  }

  @Override
  public VoxelShape getCollisionShape(BlockState blockState, IBlockReader level,
      BlockPos pos, ISelectionContext context) {
    return BUFFER_STOP_SHAPE;
  }
}
