package mods.railcraft.advancements;

import java.util.Optional;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class JukeboxCartPlayMusicTrigger
    extends SimpleCriterionTrigger<JukeboxCartPlayMusicTrigger.TriggerInstance> {

  /**
   * Invoked when the user plays music on a cart.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cart,
      ResourceLocation music) {
    this.trigger(playerEntity,
        criterionInstance -> criterionInstance.matches(playerEntity, cart, music));
  }

  public static Criterion<TriggerInstance> hasPlayedAnyMusic() {
    return RailcraftCriteriaTriggers.JUKEBOX_CART_MUSIC_PLAY.createCriterion(
        new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
  }

  public static Criterion<TriggerInstance> hasPlayedMusic(Optional<ResourceLocation> music) {
    return RailcraftCriteriaTriggers.JUKEBOX_CART_MUSIC_PLAY.createCriterion(
        new TriggerInstance(Optional.empty(), music, Optional.empty()));
  }

  @Override
  public Codec<TriggerInstance> codec() {
    return TriggerInstance.CODEC;
  }

  public record TriggerInstance(Optional<ContextAwarePredicate> player,
                                Optional<ResourceLocation> music,
                                Optional<MinecartPredicate> cart)
      implements SimpleCriterionTrigger.SimpleInstance {

    public static final Codec<TriggerInstance> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player")
                .forGetter(TriggerInstance::player),
            ExtraCodecs.strictOptionalField(ResourceLocation.CODEC, "music")
                .forGetter(TriggerInstance::music),
            ExtraCodecs.strictOptionalField(MinecartPredicate.CODEC, "cart")
                .forGetter(TriggerInstance::cart)
        ).apply(instance, TriggerInstance::new));

    public boolean matches(ServerPlayer player, AbstractMinecart cart, ResourceLocation music) {
      if (this.music.isPresent() && !this.music.get().equals(music)) {
        return false;
      }
      return this.cart.map(x -> x.matches(player, cart)).orElse(true);
    }

    @Override
    public Optional<ContextAwarePredicate> player() {
      return player;
    }
  }
}
