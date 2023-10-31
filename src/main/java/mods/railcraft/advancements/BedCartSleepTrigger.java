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

public class BedCartSleepTrigger extends SimpleCriterionTrigger<BedCartSleepTrigger.Instance> {
  @Override
  protected BedCartSleepTrigger.Instance createInstance(JsonObject json,
      Optional<ContextAwarePredicate> contextAwarePredicate,
      DeserializationContext deserializationContext) {
    var predicate = JsonUtil.getAsJsonObject(json, "cart")
        .map(MinecartPredicate::deserialize)
        .orElse(MinecartPredicate.ANY);
    return new BedCartSleepTrigger.Instance(contextAwarePredicate, predicate);
  }

  /**
   * Invoked when the user sleeps on a cart.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cartPredicate) {
    this.trigger(playerEntity,
        (criterionInstance) -> criterionInstance.matches(playerEntity, cartPredicate));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final MinecartPredicate cartPredicate;

    private Instance(Optional<ContextAwarePredicate> contextAwarePredicate,
        MinecartPredicate predicate) {
      super(contextAwarePredicate);
      this.cartPredicate = predicate;
    }

    public static BedCartSleepTrigger.Instance hasSlept() {
      return new BedCartSleepTrigger.Instance(ContextAwarePredicate.ANY, MinecartPredicate.ANY);
    }

    public boolean matches(ServerPlayer player, AbstractMinecart cartPredicate) {
      return this.cartPredicate.test(player, cartPredicate) && player.isSleeping();
    }

    @Override
    public JsonObject serializeToJson() {
      JsonObject json = new JsonObject();
      json.add("cart", this.cartPredicate.serializeToJson());
      return json;
    }
  }
}
