package mods.railcraft.world.level.block.signal;

import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.JeiSearchable;
import mods.railcraft.world.level.block.entity.signal.DualDistantSignalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DualDistantSignalBlock extends DualSignalBlock implements JeiSearchable {


  public DualDistantSignalBlock(Properties properties) {
    super(properties);
  }


  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new DualDistantSignalBlockEntity(blockPos, blockState);
  }

  @Override
  public Component jeiDescription() {
    return Component.translatable(Translations.Jei.DUAL_DISTANT_SIGNAL);
  }
}
