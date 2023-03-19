package mods.railcraft.world.damagesource;

import mods.railcraft.Translations;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

public class RailcraftDamageSource extends DamageSource {

  private final int numMessages;

  RailcraftDamageSource(Holder<DamageType> damageType) {
    this(damageType, 6);
  }

  RailcraftDamageSource(Holder<DamageType> damageType, int numMessages) {
    super(damageType);
    this.numMessages = numMessages;
  }

  @Override
  public Component getLocalizedDeathMessage(LivingEntity entity) {
    int randomMessage = entity.getRandom().nextInt(this.numMessages) + 1;
    var reason = Translations.makeKey("death", this.getMsgId() + "." + randomMessage);
    return Component.translatable(reason, entity.getName());
  }
}
