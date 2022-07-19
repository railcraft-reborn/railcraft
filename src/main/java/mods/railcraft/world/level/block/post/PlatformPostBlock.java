package mods.railcraft.world.level.block.post;

import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class PlatformPostBlock extends PostBlock {

  protected PlatformPostBlock(Properties properties) {
    super(properties);
  }

  @Override
  public boolean isPlatform(BlockState blockState) {
    return true;
  }
}
