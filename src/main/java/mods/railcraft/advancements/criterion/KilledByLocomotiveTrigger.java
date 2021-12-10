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

/**
 * I tried to fight the train. The train won.
 */
public class KilledByLocomotiveTrigger
    extends SimpleCriterionTrigger<KilledByLocomotiveTrigger.Instance> {

  private static final ResourceLocation ID =
      new ResourceLocation(Railcraft.ID, "killed_by_locomotive");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public KilledByLocomotiveTrigger.Instance createInstance(JsonObject json,
      EntityPredicate.Composite entityPredicate, DeserializationContext parser) {
    CartPredicate predicate =
        JsonTools.whenPresent(json, "cart", CartPredicate::deserialize, CartPredicate.ANY);
    return new KilledByLocomotiveTrigger.Instance(entityPredicate, predicate);
  }

  /**
   * Invoked when the user dies due to train tomfoolery.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cart) {
    this.trigger(playerEntity, (KilledByLocomotiveTrigger.Instance criterionInstance) -> {
      return criterionInstance.matches(playerEntity, cart);
    });
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final CartPredicate cart;

    private Instance(EntityPredicate.Composite entityPredicate, CartPredicate cart) {
      super(KilledByLocomotiveTrigger.ID, entityPredicate);
      this.cart = cart;
    }

    public boolean matches(ServerPlayer player, AbstractMinecart cart) {
      return this.cart.test(player, cart);
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(SerializationContext serializer) {
      JsonObject json = new JsonObject();
      json.add("cart", this.cart.serializeToJson());
      return json;
    }
  }
}
