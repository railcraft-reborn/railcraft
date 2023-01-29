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
  public static final RailcraftDamageSource TRACK_ELECTRIC =
      new RailcraftDamageSource("track.electric");
  public static final RailcraftDamageSource TRAIN = new RailcraftDamageSource("train");
  public static final RailcraftDamageSource CREOSOTE = new RailcraftDamageSource("creosote");

  static {
    BORE.bypassArmor();
    ELECTRIC.bypassArmor();
    TRACK_ELECTRIC.bypassArmor();
    TRAIN.bypassArmor();
    CREOSOTE.bypassArmor();
  }

  private final int numMessages;

  private RailcraftDamageSource(String msgId) {
    this(msgId, 6);
  }

  private RailcraftDamageSource(String msgId, int numMessages) {
    super(msgId);
    this.numMessages = numMessages;
  }

  @Override
  public Component getLocalizedDeathMessage(LivingEntity entity) {
    int randomMessage = entity.getRandom().nextInt(this.numMessages) + 1;
    var reason = Translations.makeKey("death", this.msgId + "." + randomMessage);
    return Component.translatable(reason, entity.getName());
  }
}
