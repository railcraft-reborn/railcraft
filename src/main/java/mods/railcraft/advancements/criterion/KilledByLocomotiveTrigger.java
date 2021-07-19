package mods.railcraft.advancements.criterion;

import com.google.gson.JsonObject;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.util.JsonTools;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

final class KilledByLocomotiveTrigger extends BaseTrigger<KilledByLocomotiveTrigger.Instance> {

  static final ResourceLocation ID = RailcraftConstantsAPI.locationOf("killed_by_locomotive");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public Instance createInstance(JsonObject json, ConditionArrayParser parser) {
    CartPredicate predicate =
        JsonTools.whenPresent(json, "cart", CartPredicate::deserialize, CartPredicate.ANY);
    return new Instance(predicate);
  }

  static final class Instance implements ICriterionInstance {

    final CartPredicate cart;

    Instance(CartPredicate cart) {
      this.cart = cart;
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(ConditionArraySerializer p_230240_1_) {
      JsonObject json = new JsonObject();
      json.add("cart", this.cart.serializeToJson());
      return json;
    }
  }
}
