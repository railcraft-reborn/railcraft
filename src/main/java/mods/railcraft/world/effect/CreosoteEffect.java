package mods.railcraft.world.effect;

import mods.railcraft.world.damagesource.RailcraftDamageSources;
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
      var registryAccess = livingEntity.level().registryAccess();
      livingEntity.hurt(RailcraftDamageSources.creosote(registryAccess),
          (float) Math.pow(1.1D, amplifier));
    }
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    int t = 25 >> amplifier;
    return t == 0 || duration % t == 0;
  }
}
