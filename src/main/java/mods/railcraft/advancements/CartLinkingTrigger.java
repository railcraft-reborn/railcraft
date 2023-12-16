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

public class CartLinkingTrigger extends SimpleCriterionTrigger<CartLinkingTrigger.TriggerInstance> {

  /**
   * Invoked when the user links a cart.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart owned,
      AbstractMinecart other) {
    this.trigger(playerEntity,
        criterionInstance -> criterionInstance.matches(playerEntity, owned, other));
  }

  public static Criterion<TriggerInstance> hasLinked() {
    return RailcraftCriteriaTriggers.CART_LINK.createCriterion(
        new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
  }

  @Override
  public Codec<TriggerInstance> codec() {
    return TriggerInstance.CODEC;
  }

  public record TriggerInstance(Optional<ContextAwarePredicate> player,
                                Optional<MinecartPredicate> owned,
                                Optional<MinecartPredicate> other)
      implements SimpleCriterionTrigger.SimpleInstance {

    public static final Codec<TriggerInstance> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player")
                    .forGetter(TriggerInstance::player),
                ExtraCodecs.strictOptionalField(MinecartPredicate.CODEC, "owned")
                    .forGetter(TriggerInstance::owned),
            ExtraCodecs.strictOptionalField(MinecartPredicate.CODEC, "other")
                .forGetter(TriggerInstance::other)
            ).apply(instance, TriggerInstance::new));

    public boolean matches(ServerPlayer player, AbstractMinecart owned, AbstractMinecart other) {
      return this.owned.map(x -> x.matches(player, owned)).orElse(true)
          && this.other.map(x -> x.matches(player, other)).orElse(true);
    }

    @Override
    public Optional<ContextAwarePredicate> player() {
      return player;
    }
  }
}
