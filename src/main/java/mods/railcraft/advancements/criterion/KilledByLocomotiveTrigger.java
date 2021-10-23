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

/**
 * I tried to fight the train. The train won.
 */
public class KilledByLocomotiveTrigger
    extends AbstractCriterionTrigger<KilledByLocomotiveTrigger.Instance> {

  private static final ResourceLocation ID =
      new ResourceLocation(Railcraft.ID, "killed_by_locomotive");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public KilledByLocomotiveTrigger.Instance createInstance(JsonObject json,
      EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser parser) {
    CartPredicate predicate =
        JsonTools.whenPresent(json, "cart", CartPredicate::deserialize, CartPredicate.ANY);
    return new KilledByLocomotiveTrigger.Instance(entityPredicate, predicate);
  }

  /**
   * Invoked when the user dies due to train tomfoolery.
   */
  public void trigger(ServerPlayerEntity playerEntity, AbstractMinecartEntity cart) {
    this.trigger(playerEntity, (KilledByLocomotiveTrigger.Instance criterionInstance) -> {
      return criterionInstance.matches(playerEntity, cart);
    });
  }

  public static class Instance extends CriterionInstance {

    private final CartPredicate cart;

    private Instance(EntityPredicate.AndPredicate entityPredicate, CartPredicate cart) {
      super(KilledByLocomotiveTrigger.ID, entityPredicate);
      this.cart = cart;
    }

    public boolean matches(ServerPlayerEntity player, AbstractMinecartEntity cart) {
      return this.cart.test(player, cart);
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(ConditionArraySerializer serializer) {
      JsonObject json = new JsonObject();
      json.add("cart", this.cart.serializeToJson());
      return json;
    }
  }
}
