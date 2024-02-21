package mods.railcraft.world.level.block.worldspike;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.world.level.block.entity.worldspike.PersonalWorldSpikeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class PersonalWorldSpikeBlock extends WorldSpikeBlock implements EntityBlock {

  public PersonalWorldSpikeBlock(Properties properties) {
    super(properties);
  }

  @Override
  public void setPlacedBy(Level level, BlockPos pos, BlockState state,
      @Nullable LivingEntity placer, ItemStack stack) {
    if (level.getBlockEntity(pos) instanceof PersonalWorldSpikeBlockEntity worldSpike) {
      if (placer instanceof Player player) {
        worldSpike.setOwner(player.getGameProfile());
      }
    }
  }

  @Override
  public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
      boolean willHarvest, FluidState fluid) {
    if (level.getBlockEntity(pos) instanceof PersonalWorldSpikeBlockEntity worldSpike) {
      if (worldSpike.getOwner().isEmpty() ||
          worldSpike.isOwnerOrOperator(player.getGameProfile())) {
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
      }
    }
    return false;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new PersonalWorldSpikeBlockEntity(blockPos, blockState);
  }

  @Override
  public Component jeiDescription() {
    return Component.translatable(Translations.Jei.PERSONAL_WORLD_SPIKE);
  }
}
