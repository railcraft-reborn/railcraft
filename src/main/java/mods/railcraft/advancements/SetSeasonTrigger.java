package mods.railcraft.advancements;

import java.util.Optional;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.season.Season;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class SetSeasonTrigger extends SimpleCriterionTrigger<SetSeasonTrigger.TriggerInstance> {

  /**
   * Invoked when the user changes the cart's season option I think?.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cart,
      Season target) {
    this.trigger(playerEntity,
        criterionInstance -> criterionInstance.matches(playerEntity, cart, target));
  }

  public static Criterion<TriggerInstance> onSeasonSet() {
    return RailcraftCriteriaTriggers.SEASON_SET.createCriterion(
        new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
  }

  @Override
  public Codec<TriggerInstance> codec() {
    return TriggerInstance.CODEC;
  }

  public record TriggerInstance(
      Optional<ContextAwarePredicate> player,
      Optional<Season> season,
      Optional<MinecartPredicate> cart) implements SimpleCriterionTrigger.SimpleInstance {

    public static final Codec<TriggerInstance> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player")
                .forGetter(TriggerInstance::player),
            ExtraCodecs.strictOptionalField(Season.CODEC, "season")
                .forGetter(TriggerInstance::season),
            ExtraCodecs.strictOptionalField(MinecartPredicate.CODEC, "cart")
                .forGetter(TriggerInstance::cart)
        ).apply(instance, TriggerInstance::new));

    public boolean matches(ServerPlayer player, AbstractMinecart cart, Season season) {
      if (this.season.isPresent() && !this.season.get().equals(season)) {
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
