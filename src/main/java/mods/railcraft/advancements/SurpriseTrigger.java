package mods.railcraft.advancements;

import java.util.Optional;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class SurpriseTrigger extends SimpleCriterionTrigger<SurpriseTrigger.Instance> {

  @Override
  public SurpriseTrigger.Instance createInstance(JsonObject json,
      Optional<ContextAwarePredicate> contextAwarePredicate,
      DeserializationContext deserializationContext) {
    var minecart = MinecartPredicate.fromJson(json.get("cart"));
    return new SurpriseTrigger.Instance(contextAwarePredicate, minecart);
  }

  /**
   * Invoked when the user explodes a cart.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cart) {
    this.trigger(playerEntity,
        (criterionInstance) -> criterionInstance.matches(playerEntity, cart));
  }

  public static Criterion<Instance> hasExplodedCart() {
    return RailcraftCriteriaTriggers.CART_SURPRISE_EXPLODE.createCriterion(
        new Instance(Optional.empty(), Optional.empty()));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final Optional<MinecartPredicate> cart;

    private Instance(Optional<ContextAwarePredicate> contextAwarePredicate,
        Optional<MinecartPredicate> cart) {
      super(contextAwarePredicate);
      this.cart = cart;
    }

    public boolean matches(ServerPlayer player, AbstractMinecart cart) {
      return this.cart
          .map(x -> x.matches(player, cart))
          .orElse(false);
    }

    @Override
    public JsonObject serializeToJson() {
      JsonObject json = new JsonObject();
      this.cart.ifPresent(x -> json.add("cart", x.serializeToJson()));
      return json;
    }
  }
}
