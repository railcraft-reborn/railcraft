package mods.railcraft.world.level.block.signal;

import mods.railcraft.world.level.block.post.PostBlock;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 
 * @author Sm0keySa1m0n
 *
 */
public abstract class DualSignalBlock extends SignalBlock {

  public static final VoxelShape SHAPE = box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);

  public DualSignalBlock(Properties properties) {
    super(SHAPE, PostBlock.HORIZONTAL_CONNECTION_SHAPES, properties);
  }
}
