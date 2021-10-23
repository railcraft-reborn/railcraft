package mods.railcraft.advancements.criterion;

import com.google.gson.JsonObject;

import mods.railcraft.Railcraft;
import mods.railcraft.util.JsonTools;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

public class CartLinkingTrigger extends AbstractCriterionTrigger<CartLinkingTrigger.Instance> {

  private static final ResourceLocation ID = new ResourceLocation(Railcraft.ID, "cart_linking");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public CartLinkingTrigger.Instance createInstance(JsonObject json,
      EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser parser) {
    CartPredicate owned =
        JsonTools.whenPresent(json, "owned", CartPredicate::deserialize, CartPredicate.ANY);
    CartPredicate other =
        JsonTools.whenPresent(json, "other", CartPredicate::deserialize, CartPredicate.ANY);
    return new CartLinkingTrigger.Instance(entityPredicate, owned, other);
  }

  /**
   * Invoked when the user links a cart.
   */
  public void trigger(ServerPlayerEntity playerEntity, AbstractMinecartEntity owned,
      AbstractMinecartEntity other) {
    this.trigger(playerEntity,
        (criterionInstance) -> criterionInstance.matches(playerEntity, owned, other));
  }

  public static class Instance extends CriterionInstance {

    private final CartPredicate owned;
    private final CartPredicate other;

    private Instance(EntityPredicate.AndPredicate entityPredicate,
        CartPredicate owned, CartPredicate other) {
      super(CartLinkingTrigger.ID, entityPredicate);
      this.owned = owned;
      this.other = other;
    }

    public static CartLinkingTrigger.Instance hasLinked() {
      return new CartLinkingTrigger.Instance(EntityPredicate.AndPredicate.ANY,
          CartPredicate.ANY, CartPredicate.ANY);
    }

    public boolean matches(ServerPlayerEntity player, AbstractMinecartEntity owned,
        AbstractMinecartEntity other) {
      return this.owned.test(player, owned) && this.other.test(player, other);
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(ConditionArraySerializer serializer) {
      JsonObject json = new JsonObject();
      json.add("owned", this.owned.serializeToJson());
      json.add("other", this.other.serializeToJson());
      return json;
    }
  }

}
