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

public class CartLinkingTrigger extends SimpleCriterionTrigger<CartLinkingTrigger.Instance> {

  private static final ResourceLocation ID = new ResourceLocation(Railcraft.ID, "cart_linking");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public CartLinkingTrigger.Instance createInstance(JsonObject json,
      EntityPredicate.Composite entityPredicate, DeserializationContext parser) {
    CartPredicate owned =
        JsonTools.whenPresent(json, "owned", CartPredicate::deserialize, CartPredicate.ANY);
    CartPredicate other =
        JsonTools.whenPresent(json, "other", CartPredicate::deserialize, CartPredicate.ANY);
    return new CartLinkingTrigger.Instance(entityPredicate, owned, other);
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

    private final CartPredicate owned;
    private final CartPredicate other;

    private Instance(EntityPredicate.Composite entityPredicate,
        CartPredicate owned, CartPredicate other) {
      super(CartLinkingTrigger.ID, entityPredicate);
      this.owned = owned;
      this.other = other;
    }

    public static CartLinkingTrigger.Instance hasLinked() {
      return new CartLinkingTrigger.Instance(EntityPredicate.Composite.ANY,
          CartPredicate.ANY, CartPredicate.ANY);
    }

    public boolean matches(ServerPlayer player, AbstractMinecart owned,
        AbstractMinecart other) {
      return this.owned.test(player, owned) && this.other.test(player, other);
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(SerializationContext serializer) {
      JsonObject json = new JsonObject();
      json.add("owned", this.owned.serializeToJson());
      json.add("other", this.other.serializeToJson());
      return json;
    }
  }

}
