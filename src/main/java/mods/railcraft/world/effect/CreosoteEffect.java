package mods.railcraft.world.effect;

import mods.railcraft.world.damagesource.RailcraftDamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;

public class CreosoteEffect extends MobEffect {

  protected CreosoteEffect(MobEffectCategory category, int color) {
    super(category, color);
  }

  @Override
  public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
    if (livingEntity.getMobType().equals(MobType.ARTHROPOD)) {
      livingEntity.hurt(RailcraftDamageSource.CREOSOTE,
          (float) Math.pow(1.1D, amplifier));
    }
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    int t = 25 >> amplifier;
    return t == 0 || duration % t == 0;
  }
}
