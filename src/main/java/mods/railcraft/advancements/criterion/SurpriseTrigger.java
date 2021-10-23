package mods.railcraft.advancements.criterion;

import com.google.gson.JsonObject;

import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.util.JsonTools;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

public class SurpriseTrigger extends AbstractCriterionTrigger<SurpriseTrigger.Instance> {

  private static final ResourceLocation ID = RailcraftConstantsAPI.locationOf("surprise");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public SurpriseTrigger.Instance createInstance(JsonObject json,
      EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser parser) {
    CartPredicate predicate =
        JsonTools.whenPresent(json, "cart", CartPredicate::deserialize, CartPredicate.ANY);
    return new SurpriseTrigger.Instance(entityPredicate, predicate);
  }

  /**
   * Invoked when the user explodes a cart.
   */
  public void trigger(ServerPlayerEntity playerEntity, AbstractMinecartEntity cart) {
    this.trigger(playerEntity, (SurpriseTrigger.Instance criterionInstance) -> {
      return criterionInstance.matches(playerEntity, cart);
    });
  }

  public static class Instance extends CriterionInstance {

    private final CartPredicate cartPredicate;

    Instance(EntityPredicate.AndPredicate entityPredicate, CartPredicate predicate) {
      super(SurpriseTrigger.ID, entityPredicate);
      this.cartPredicate = predicate;
    }

    public static SurpriseTrigger.Instance hasExplodedCart() {
      return new SurpriseTrigger.Instance(EntityPredicate.AndPredicate.ANY, CartPredicate.ANY);
    }

    public boolean matches(ServerPlayerEntity player, AbstractMinecartEntity cart) {
      return cartPredicate.test(player, cart);
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(ConditionArraySerializer serializer) {
      JsonObject json = new JsonObject();
      json.add("cart", this.cartPredicate.serializeToJson());
      return json;
    }
  }
}
