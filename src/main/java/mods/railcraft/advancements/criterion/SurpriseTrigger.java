package mods.railcraft.advancements.criterion;

import com.google.gson.JsonObject;
import mods.railcraft.Railcraft;
import mods.railcraft.util.JsonTools;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.resources.ResourceLocation;

public class SurpriseTrigger extends SimpleCriterionTrigger<SurpriseTrigger.Instance> {

  private static final ResourceLocation ID = new ResourceLocation(Railcraft.ID, "surprise");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public SurpriseTrigger.Instance createInstance(JsonObject json,
      EntityPredicate.Composite entityPredicate, DeserializationContext parser) {
    CartPredicate predicate =
        JsonTools.whenPresent(json, "cart", CartPredicate::deserialize, CartPredicate.ANY);
    return new SurpriseTrigger.Instance(entityPredicate, predicate);
  }

  /**
   * Invoked when the user explodes a cart.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cart) {
    this.trigger(playerEntity,
        (criterionInstance) -> criterionInstance.matches(playerEntity, cart));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final CartPredicate cartPredicate;

    private Instance(EntityPredicate.Composite entityPredicate, CartPredicate predicate) {
      super(SurpriseTrigger.ID, entityPredicate);
      this.cartPredicate = predicate;
    }

    public static SurpriseTrigger.Instance hasExplodedCart() {
      return new SurpriseTrigger.Instance(EntityPredicate.Composite.ANY, CartPredicate.ANY);
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
