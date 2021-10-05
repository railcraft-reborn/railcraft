package mods.railcraft.world.level.block.signal;

import java.util.function.Supplier;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import mods.railcraft.world.level.block.post.PostBlock;
import net.minecraft.util.math.shapes.VoxelShape;

/**
 * 
 * @author Sm0keySa1m0n
 *
 */
public class DualSignalBlock extends AbstractSignalBlock {

  public static final VoxelShape SHAPE = box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);

  public DualSignalBlock(Supplier<? extends AbstractSignalBlockEntity> blockEntityFactory,
      Properties properties) {
    super(SHAPE, PostBlock.HORIZONTAL_CONNECTION_SHAPES, blockEntityFactory, properties);
  }
}
