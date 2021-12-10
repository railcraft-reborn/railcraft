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

public class BedCartSleepTrigger extends SimpleCriterionTrigger<BedCartSleepTrigger.Instance> {

  private static final ResourceLocation ID = new ResourceLocation(Railcraft.ID, "bed_cart_sleep");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public BedCartSleepTrigger.Instance createInstance(JsonObject json,
      EntityPredicate.Composite entityPredicate, DeserializationContext parser) {
    CartPredicate predicate =
        JsonTools.whenPresent(json, "cart", CartPredicate::deserialize, CartPredicate.ANY);
    return new BedCartSleepTrigger.Instance(entityPredicate, predicate);
  }

  /**
   * Invoked when the user sleeps on a cart.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cartPredicate) {
    this.trigger(playerEntity,
        (criterionInstance) -> criterionInstance.matches(playerEntity, cartPredicate));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final CartPredicate cartPredicate;

    private Instance(EntityPredicate.Composite entityPredicate, CartPredicate predicate) {
      super(BedCartSleepTrigger.ID, entityPredicate);
      this.cartPredicate = predicate;
    }

    public static BedCartSleepTrigger.Instance hasSlept() {
      return new BedCartSleepTrigger.Instance(EntityPredicate.Composite.ANY, CartPredicate.ANY);
    }

    public boolean matches(ServerPlayer player, AbstractMinecart cartPredicate) {
      return this.cartPredicate.test(player, cartPredicate) && player.isSleeping();
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
