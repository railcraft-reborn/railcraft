package mods.railcraft.world.level.block.signal;

import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.JeiSearchable;
import mods.railcraft.world.level.block.entity.signal.DistantSignalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DistantSignalBlock extends SingleSignalBlock implements JeiSearchable {


  public DistantSignalBlock(Properties properties) {
    super(properties);
  }


  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new DistantSignalBlockEntity(blockPos, blockState);
  }

  @Override
  public Component jeiDescription() {
    return Component.translatable(Translations.Jei.DISTANT_SIGNAL);
  }
}
