package mods.railcraft.advancements;

import java.util.Optional;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SurpriseTrigger extends SimpleCriterionTrigger<SurpriseTrigger.TriggerInstance> {

  /**
   * Invoked when the user explodes a cart.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cart) {
    this.trigger(playerEntity, criterionInstance -> criterionInstance.matches(playerEntity, cart));
  }

  public static Criterion<TriggerInstance> hasExplodedCart() {
    return RailcraftCriteriaTriggers.CART_SURPRISE_EXPLODE.createCriterion(
        new TriggerInstance(Optional.empty(), Optional.empty()));
  }

  @Override
  public Codec<TriggerInstance> codec() {
    return TriggerInstance.CODEC;
  }

  public record TriggerInstance(
      Optional<Holder<LootItemCondition>> player,
      Optional<MinecartPredicate> cart) implements SimpleCriterionTrigger.SimpleInstance {

    public static final Codec<TriggerInstance> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            LootItemCondition.CODEC.optionalFieldOf("player")
                .forGetter(TriggerInstance::player),
            MinecartPredicate.CODEC.optionalFieldOf("cart")
                .forGetter(TriggerInstance::cart)
        ).apply(instance, TriggerInstance::new));

    public boolean matches(ServerPlayer player, AbstractMinecart cart) {
      return this.cart
          .map(x -> x.matches(player, cart))
          .orElse(true);
    }

    @Override
    public Optional<Holder<LootItemCondition>> player() {
      return player;
    }
  }
}
