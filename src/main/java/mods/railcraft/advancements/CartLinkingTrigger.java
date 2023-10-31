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

public class CartLinkingTrigger extends SimpleCriterionTrigger<CartLinkingTrigger.Instance> {
  @Override
  public CartLinkingTrigger.Instance createInstance(JsonObject json,
      Optional<ContextAwarePredicate> contextAwarePredicate,
      DeserializationContext deserializationContext) {
    var owned = JsonUtil.getAsJsonObject(json, "owned")
        .map(MinecartPredicate::deserialize)
        .orElse(MinecartPredicate.ANY);
    var other = JsonUtil.getAsJsonObject(json, "other")
        .map(MinecartPredicate::deserialize)
        .orElse(MinecartPredicate.ANY);
    return new CartLinkingTrigger.Instance(contextAwarePredicate, owned, other);
  }

  /**
   * Invoked when the user links a cart.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart owned,
      AbstractMinecart other) {
    this.trigger(playerEntity,
        (criterionInstance) -> criterionInstance.matches(playerEntity, owned, other));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final MinecartPredicate owned;
    private final MinecartPredicate other;

    private Instance(Optional<ContextAwarePredicate> contextAwarePredicate,
        MinecartPredicate owned, MinecartPredicate other) {
      super(contextAwarePredicate);
      this.owned = owned;
      this.other = other;
    }

    public static CartLinkingTrigger.Instance hasLinked() {
      return new CartLinkingTrigger.Instance(ContextAwarePredicate.ANY,
          MinecartPredicate.ANY, MinecartPredicate.ANY);
    }

    public boolean matches(ServerPlayer player, AbstractMinecart owned,
        AbstractMinecart other) {
      return this.owned.test(player, owned) && this.other.test(player, other);
    }

    @Override
    public JsonObject serializeToJson() {
      JsonObject json = new JsonObject();
      json.add("owned", this.owned.serializeToJson());
      json.add("other", this.other.serializeToJson());
      return json;
    }
  }

}
