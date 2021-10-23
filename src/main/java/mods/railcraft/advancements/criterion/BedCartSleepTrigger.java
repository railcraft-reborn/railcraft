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

public class BedCartSleepTrigger extends AbstractCriterionTrigger<BedCartSleepTrigger.Instance> {

  private static final ResourceLocation ID = new ResourceLocation(Railcraft.ID + ":bed_cart_sleep");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public BedCartSleepTrigger.Instance createInstance(JsonObject json,
      EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser parser) {
    CartPredicate predicate =
        JsonTools.whenPresent(json, "cart", CartPredicate::deserialize, CartPredicate.ANY);
    return new BedCartSleepTrigger.Instance(entityPredicate, predicate);
  }

  /**
   * Invoked when the user sleeps on a cart.
   */
  public void trigger(ServerPlayerEntity playerEntity, AbstractMinecartEntity cartPredicate) {
    this.trigger(playerEntity, (BedCartSleepTrigger.Instance criterionInstance) -> {
      return criterionInstance.matches(playerEntity, cartPredicate);
    });
  }

  public static class Instance extends CriterionInstance {

    private final CartPredicate cartPredicate;

    Instance(EntityPredicate.AndPredicate entityPredicate, CartPredicate predicate) {
      super(BedCartSleepTrigger.ID, entityPredicate);
      this.cartPredicate = predicate;
    }

    public static BedCartSleepTrigger.Instance hasSlept() {
      return new BedCartSleepTrigger.Instance(EntityPredicate.AndPredicate.ANY, CartPredicate.ANY);
    }

    public boolean matches(ServerPlayerEntity player, AbstractMinecartEntity cartPredicate) {
      return this.cartPredicate.test(player, cartPredicate) && player.isSleeping();
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
