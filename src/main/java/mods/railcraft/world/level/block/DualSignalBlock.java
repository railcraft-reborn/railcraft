package mods.railcraft.world.level.block;

import java.util.function.Supplier;
import mods.railcraft.util.AABBFactory;
import mods.railcraft.world.level.block.entity.AbstractSignalBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

/**
 * Created by CovertJaguar on 7/5/2017 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class DualSignalBlock extends AbstractSignalBlock {

  public static final VoxelShape SHAPE =
      VoxelShapes.create(AABBFactory.start()
          .box()
          .expandHorizontally(-BLOCK_BOUNDS)
          .build());

  protected DualSignalBlock(Supplier<? extends AbstractSignalBlockEntity> blockEntityFactory,
      Properties properties) {
    super(blockEntityFactory, properties);
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos,
      ISelectionContext context) {
    return SHAPE;
  }
}
