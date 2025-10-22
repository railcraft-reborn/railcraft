package mods.railcraft.world.damagesource;

import mods.railcraft.Translations;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class RailcraftDamageSource extends DamageSource {
  public static final RailcraftDamageSource BORE = new RailcraftDamageSource("bore");
  public static final RailcraftDamageSource CRUSHER = new RailcraftDamageSource("crusher", 8);
  public static final RailcraftDamageSource ELECTRIC = new RailcraftDamageSource("electric");
  public static final RailcraftDamageSource STEAM = new RailcraftDamageSource("steam");
  public static final RailcraftDamageSource TRACK_ELECTRIC = new RailcraftDamageSource("track_electric");
  public static final RailcraftDamageSource TRAIN = new RailcraftDamageSource("train");
  public static final RailcraftDamageSource CREOSOTE = new RailcraftDamageSource("creosote");

  private final int numMessages;

  RailcraftDamageSource(String messageId) {
    this(messageId, 6);
  }

  RailcraftDamageSource(String messageId, int numMessages) {
    super(messageId);
    this.numMessages = numMessages;
  }

  @Override
  public Component getLocalizedDeathMessage(LivingEntity entity) {
    int randomMessage = entity.getRandom().nextInt(this.numMessages) + 1;
    var reason = Translations.makeKey("death", this.getMsgId() + "." + randomMessage);
    return Component.translatable(reason, entity.getName());
  }
}
