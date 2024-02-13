package mods.railcraft.advancements;

import com.google.gson.JsonObject;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.util.JsonUtil;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

/**
 * I tried to fight the train. The train won.
 */
public class KilledByLocomotiveTrigger
    extends SimpleCriterionTrigger<KilledByLocomotiveTrigger.Instance> {

  private static final ResourceLocation ID = RailcraftConstants.rl("killed_by_locomotive");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  protected Instance createInstance(JsonObject json, ContextAwarePredicate contextAwarePredicate,
      DeserializationContext deserializationContext) {
    var predicate = JsonUtil.getAsJsonObject(json, "cart")
        .map(MinecartPredicate::deserialize)
        .orElse(MinecartPredicate.ANY);
    return new KilledByLocomotiveTrigger.Instance(contextAwarePredicate, predicate);
  }

  /**
   * Invoked when the user dies due to train tomfoolery.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cart) {
    this.trigger(playerEntity, (KilledByLocomotiveTrigger.Instance criterionInstance) -> {
      return criterionInstance.matches(playerEntity, cart);
    });
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final MinecartPredicate cart;

    private Instance(ContextAwarePredicate contextAwarePredicate, MinecartPredicate cart) {
      super(KilledByLocomotiveTrigger.ID, contextAwarePredicate);
      this.cart = cart;
    }

    public boolean matches(ServerPlayer player, AbstractMinecart cart) {
      return this.cart.test(player, cart);
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(SerializationContext serializer) {
      JsonObject json = new JsonObject();
      json.add("cart", this.cart.serializeToJson());
      return json;
    }
  }
}
