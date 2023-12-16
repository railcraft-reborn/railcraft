package mods.railcraft.advancements;

import java.util.Optional;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class BedCartSleepTrigger extends SimpleCriterionTrigger<BedCartSleepTrigger.TriggerInstance> {

  /**
   * Invoked when the user sleeps on a cart.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cart) {
    this.trigger(playerEntity, criterionInstance -> criterionInstance.matches(playerEntity, cart));
  }

  public static Criterion<TriggerInstance> hasSlept() {
    return RailcraftCriteriaTriggers.BED_CART_SLEEP.createCriterion(
        new TriggerInstance(Optional.empty(), Optional.empty()));
  }

  @Override
  public Codec<TriggerInstance> codec() {
    return TriggerInstance.CODEC;
  }

  public record TriggerInstance(Optional<ContextAwarePredicate> player,
                                Optional<MinecartPredicate> cart)
      implements SimpleCriterionTrigger.SimpleInstance {

    public static final Codec<TriggerInstance> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player")
                .forGetter(TriggerInstance::player),
            ExtraCodecs.strictOptionalField(MinecartPredicate.CODEC, "cart")
                .forGetter(TriggerInstance::cart)
            ).apply(instance, TriggerInstance::new));

    public boolean matches(ServerPlayer player, AbstractMinecart cart) {
      return this.cart
          .map(x -> x.matches(player, cart))
          .orElse(true) && player.isSleeping();
    }

    @Override
    public Optional<ContextAwarePredicate> player() {
      return player;
    }
  }
}
