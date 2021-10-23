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

/**
 * TODO: Implament this on carts.
 */
public class CartRidingTrigger extends AbstractCriterionTrigger<CartRidingTrigger.Instance> {

  private static final ResourceLocation ID = RailcraftConstantsAPI.locationOf("cart_riding");
  // private static final int FREQUENCY = 20;

  // private final Map<ServerPlayerEntity, AbstractMinecartEntity> mounting =
  //     new MapMaker().weakKeys().weakValues().makeMap();

  // private int counter;

  // CartRidingTrigger() {
  //   MinecraftForge.EVENT_BUS.register(this);
  // }

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public CartRidingTrigger.Instance createInstance(JsonObject json,
      EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser parser) {
    CartPredicate predicate =
        JsonTools.whenPresent(json, "cart", CartPredicate::deserialize, CartPredicate.ANY);
    return new CartRidingTrigger.Instance(entityPredicate, predicate);
  }

  /**
   * Invoked when the user rides a cart.
   */
  public void trigger(ServerPlayerEntity playerEntity, AbstractMinecartEntity cart) {
    this.trigger(playerEntity, (CartRidingTrigger.Instance criterionInstance) -> {
      return criterionInstance.matches(playerEntity, cart);
    });
  }

  // @SubscribeEvent
  // public void onMount(EntityMountEvent event) {
  //   if (!(event.getEntityMounting() instanceof ServerPlayerEntity)
  //       || !(event.getEntityBeingMounted() instanceof AbstractMinecartEntity)) {
  //     return;
  //   }

  //   ServerPlayerEntity rider = (ServerPlayerEntity) event.getEntityMounting();
  //   AbstractMinecartEntity cart = (AbstractMinecartEntity) event.getEntityBeingMounted();

  //   if (event.isMounting()) {
  //     mounting.put(rider, cart);
  //   } else {
  //     mounting.remove(rider);
  //   }
  // }

  // @SubscribeEvent
  // public void tick(ServerTickEvent event) {
  //   if (counter != FREQUENCY) {
  //     counter++;
  //     return;
  //   }
  //   counter = 0;

  //   for (Entry<ServerPlayerEntity, AbstractMinecartEntity> entry : mounting.entrySet()) {
  //     trigger(entry.getKey(), instance -> instance.test(entry.getKey(), entry.getValue()));
  //   }
  // }

  public static class Instance extends CriterionInstance {

    private final CartPredicate cartPredicate;

    Instance(EntityPredicate.AndPredicate entityPredicate, CartPredicate predicate) {
      super(CartRidingTrigger.ID, entityPredicate);
      this.cartPredicate = predicate;
    }

    public static CartRidingTrigger.Instance hasRidden() {
      return new CartRidingTrigger.Instance(EntityPredicate.AndPredicate.ANY, CartPredicate.ANY);
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
