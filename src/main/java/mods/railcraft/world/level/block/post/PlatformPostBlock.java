package mods.railcraft.world.level.block.post;

import net.minecraft.block.BlockState;

public class PlatformPostBlock extends PostBlock {

  protected PlatformPostBlock(Properties properties) {
    super(properties);
  }

  @Override
  public boolean isPlatform(BlockState blockState) {
    return true;
  }
}
