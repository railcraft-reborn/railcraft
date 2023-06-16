package mods.railcraft.advancements;

import com.google.gson.JsonObject;
import mods.railcraft.Railcraft;
import mods.railcraft.util.JsonUtil;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class SurpriseTrigger extends SimpleCriterionTrigger<SurpriseTrigger.Instance> {

  private static final ResourceLocation ID = new ResourceLocation(Railcraft.ID, "surprise");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public SurpriseTrigger.Instance createInstance(JsonObject json,
      ContextAwarePredicate contextAwarePredicate, DeserializationContext deserializationContext) {
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

    private Instance(ContextAwarePredicate contextAwarePredicate, MinecartPredicate predicate) {
      super(SurpriseTrigger.ID, contextAwarePredicate);
      this.cartPredicate = predicate;
    }

    public static SurpriseTrigger.Instance hasExplodedCart() {
      return new SurpriseTrigger.Instance(ContextAwarePredicate.ANY, MinecartPredicate.ANY);
    }

    public boolean matches(ServerPlayer player, AbstractMinecart cart) {
      return cartPredicate.test(player, cart);
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(SerializationContext serializer) {
      JsonObject json = new JsonObject();
      json.add("cart", this.cartPredicate.serializeToJson());
      return json;
    }
  }
}
