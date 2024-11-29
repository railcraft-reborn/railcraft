package mods.railcraft.world.effect;

import mods.railcraft.world.damagesource.RailcraftDamageSources;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class CreosoteEffect extends MobEffect {

  protected CreosoteEffect(MobEffectCategory category, int color) {
    super(category, color);
  }

  @Override
  public boolean applyEffectTick(ServerLevel level, LivingEntity livingEntity, int amplifier) {
    if (livingEntity.getType().is(EntityTypeTags.ARTHROPOD)) {
      var registryAccess = level.registryAccess();
      livingEntity.hurt(RailcraftDamageSources.creosote(registryAccess),
          (float) Math.pow(1.1D, amplifier));
    }
    return true;
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
    int t = 25 >> amplifier;
    return t == 0 || duration % t == 0;
  }
}
