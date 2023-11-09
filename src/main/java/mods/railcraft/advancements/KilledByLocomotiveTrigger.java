package mods.railcraft.advancements;

import java.util.Optional;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class KilledByLocomotiveTrigger
    extends SimpleCriterionTrigger<KilledByLocomotiveTrigger.Instance> {

  @Override
  protected Instance createInstance(JsonObject json,
      Optional<ContextAwarePredicate> contextAwarePredicate,
      DeserializationContext deserializationContext) {
    var minecart = MinecartPredicate.fromJson(json.get("cart"));
    return new KilledByLocomotiveTrigger.Instance(contextAwarePredicate, minecart);
  }

  /**
   * Invoked when the user dies due to train tomfoolery.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cart) {
    this.trigger(playerEntity, (KilledByLocomotiveTrigger.Instance criterionInstance) ->
        criterionInstance.matches(playerEntity, cart));
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
          .orElse(true);
    }

    @Override
    public JsonObject serializeToJson() {
      var json = super.serializeToJson();
      this.cart.ifPresent(x -> json.add("cart", x.serializeToJson()));
      return json;
    }
  }
}
