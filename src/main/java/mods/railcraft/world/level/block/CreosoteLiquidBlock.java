package mods.railcraft.world.level.block;

import mods.railcraft.world.effect.RailcraftMobEffects;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CreosoteLiquidBlock extends LiquidBlock {

  public CreosoteLiquidBlock(Properties properties) {
    super(RailcraftFluids.CREOSOTE, properties);
  }

  @Override
  public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos,
      Direction direction) {
    return true;
  }

  @Override
  public int getFlammability(BlockState state, BlockGetter level, BlockPos pos,
      Direction direction) {
    return 10;
  }

  @Override
  public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    if (entity instanceof LivingEntity living) {
      var potion = RailcraftMobEffects.CREOSOTE.get();
      if (!living.hasEffect(potion)) {
        living.addEffect(new MobEffectInstance(potion, 100, 0));
      }
    }
  }
}
