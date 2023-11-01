package mods.railcraft.advancements;

import java.util.Optional;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class BedCartSleepTrigger extends SimpleCriterionTrigger<BedCartSleepTrigger.Instance> {
  @Override
  protected BedCartSleepTrigger.Instance createInstance(JsonObject json,
      Optional<ContextAwarePredicate> contextAwarePredicate,
      DeserializationContext deserializationContext) {
    var minecart = MinecartPredicate.fromJson(json.get("cart"));
    return new BedCartSleepTrigger.Instance(contextAwarePredicate, minecart);
  }

  /**
   * Invoked when the user sleeps on a cart.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cartPredicate) {
    this.trigger(playerEntity,
        (criterionInstance) -> criterionInstance.matches(playerEntity, cartPredicate));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final Optional<MinecartPredicate> cart;

    private Instance(Optional<ContextAwarePredicate> contextAwarePredicate,
        Optional<MinecartPredicate> cart) {
      super(contextAwarePredicate);
      this.cart = cart;
    }

    public static BedCartSleepTrigger.Instance hasSlept() {
      return new BedCartSleepTrigger.Instance(Optional.empty(), Optional.empty());
    }

    public boolean matches(ServerPlayer player, AbstractMinecart cart) {
      return this.cart
          .map(x -> x.matches(player, cart))
          .orElse(false) && player.isSleeping();
    }

    @Override
    public JsonObject serializeToJson() {
      var json = super.serializeToJson();
      this.cart.ifPresent(x -> json.add("cart", x.serializeToJson()));
      return json;
    }
  }
}
