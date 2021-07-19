package mods.railcraft.advancements.criterion;

import com.google.gson.JsonObject;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.util.JsonTools;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

final class BedCartSleepTrigger extends BaseTrigger<BedCartSleepTrigger.Instance> {

  static final ResourceLocation ID = RailcraftConstantsAPI.locationOf("bed_cart_sleep");

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

    final CartPredicate cartPredicate;

    Instance(CartPredicate predicate) {
      this.cartPredicate = predicate;
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(ConditionArraySerializer p_230240_1_) {
      JsonObject json = new JsonObject();
      json.add("cart", this.cartPredicate.serializeToJson());
      return json;
    }
  }
}
