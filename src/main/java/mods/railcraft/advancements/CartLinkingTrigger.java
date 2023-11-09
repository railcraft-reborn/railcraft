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

public class CartLinkingTrigger extends SimpleCriterionTrigger<CartLinkingTrigger.Instance> {
  @Override
  public CartLinkingTrigger.Instance createInstance(JsonObject json,
      Optional<ContextAwarePredicate> contextAwarePredicate,
      DeserializationContext deserializationContext) {
    var owned = MinecartPredicate.fromJson(json.get("owned"));
    var other = MinecartPredicate.fromJson(json.get("other"));
    return new CartLinkingTrigger.Instance(contextAwarePredicate, owned, other);
  }

  /**
   * Invoked when the user links a cart.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart owned,
      AbstractMinecart other) {
    this.trigger(playerEntity,
        criterionInstance -> criterionInstance.matches(playerEntity, owned, other));
  }

  public static Criterion<Instance> hasLinked() {
    return RailcraftCriteriaTriggers.CART_LINK.createCriterion(
        new CartLinkingTrigger.Instance(Optional.empty(),
        Optional.empty(), Optional.empty()));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final Optional<MinecartPredicate> owned, other;

    private Instance(Optional<ContextAwarePredicate> contextAwarePredicate,
        Optional<MinecartPredicate> owned, Optional<MinecartPredicate> other) {
      super(contextAwarePredicate);
      this.owned = owned;
      this.other = other;
    }

    public boolean matches(ServerPlayer player, AbstractMinecart owned, AbstractMinecart other) {
      return this.owned.map(x -> x.matches(player, owned)).orElse(true)
          && this.other.map(x -> x.matches(player, other)).orElse(true);
    }

    @Override
    public JsonObject serializeToJson() {
      var json = super.serializeToJson();
      this.owned.ifPresent(x -> json.add("owned", x.serializeToJson()));
      this.other.ifPresent(x -> json.add("other", x.serializeToJson()));
      return json;
    }
  }

}
