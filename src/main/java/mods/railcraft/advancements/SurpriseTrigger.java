package mods.railcraft.advancements;

import java.util.Optional;
import com.google.gson.JsonObject;
import mods.railcraft.util.JsonUtil;
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
    var predicate = JsonUtil.getAsJsonObject(json, "cart")
        .map(MinecartPredicate::deserialize)
        .orElse(MinecartPredicate.ANY);
    return new SurpriseTrigger.Instance(contextAwarePredicate, predicate);
  }

  /**
   * Invoked when the user explodes a cart.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cart) {
    this.trigger(playerEntity,
        (criterionInstance) -> criterionInstance.matches(playerEntity, cart));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final MinecartPredicate cartPredicate;

    private Instance(Optional<ContextAwarePredicate> contextAwarePredicate, MinecartPredicate predicate) {
      super(contextAwarePredicate);
      this.cartPredicate = predicate;
    }

    public static SurpriseTrigger.Instance hasExplodedCart() {
      return new SurpriseTrigger.Instance(ContextAwarePredicate.ANY, MinecartPredicate.ANY);
    }

    public boolean matches(ServerPlayer player, AbstractMinecart cart) {
      return cartPredicate.test(player, cart);
    }

    @Override
    public JsonObject serializeToJson() {
      JsonObject json = new JsonObject();
      json.add("cart", this.cartPredicate.serializeToJson());
      return json;
    }
  }
}
