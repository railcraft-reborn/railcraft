package mods.railcraft.advancements;

import com.google.gson.JsonObject;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.util.JsonUtil;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

/**
 * TODO: Implament this on carts.
 */
public class CartRidingTrigger extends SimpleCriterionTrigger<CartRidingTrigger.Instance> {

  private static final ResourceLocation ID = RailcraftConstants.rl("cart_riding");
  // private static final int FREQUENCY = 20;

  // private final Map<ServerPlayerEntity, AbstractMinecartEntity> mounting =
  // new MapMaker().weakKeys().weakValues().makeMap();

  // private int counter;

  // CartRidingTrigger() {
  // MinecraftForge.EVENT_BUS.register(this);
  // }

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public CartRidingTrigger.Instance createInstance(JsonObject json,
      ContextAwarePredicate contextAwarePredicate, DeserializationContext deserializationContext) {
    var predicate = JsonUtil.getAsJsonObject(json, "cart")
        .map(MinecartPredicate::deserialize)
        .orElse(MinecartPredicate.ANY);
    return new CartRidingTrigger.Instance(contextAwarePredicate, predicate);
  }

  /**
   * Invoked when the user rides a cart.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cart) {
    this.trigger(playerEntity,
        (criterionInstance) -> criterionInstance.matches(playerEntity, cart));
  }

  // @SubscribeEvent
  // public void onMount(EntityMountEvent event) {
  // if (!(event.getEntityMounting() instanceof ServerPlayerEntity)
  // || !(event.getEntityBeingMounted() instanceof AbstractMinecartEntity)) {
  // return;
  // }

  // ServerPlayerEntity rider = (ServerPlayerEntity) event.getEntityMounting();
  // AbstractMinecartEntity cart = (AbstractMinecartEntity) event.getEntityBeingMounted();

  // if (event.isMounting()) {
  // mounting.put(rider, cart);
  // } else {
  // mounting.remove(rider);
  // }
  // }

  // @SubscribeEvent
  // public void tick(ServerTickEvent event) {
  // if (counter != FREQUENCY) {
  // counter++;
  // return;
  // }
  // counter = 0;

  // for (Entry<ServerPlayerEntity, AbstractMinecartEntity> entry : mounting.entrySet()) {
  // trigger(entry.getKey(), instance -> instance.test(entry.getKey(), entry.getValue()));
  // }
  // }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final MinecartPredicate cartPredicate;

    private Instance(ContextAwarePredicate contextAwarePredicate, MinecartPredicate predicate) {
      super(CartRidingTrigger.ID, contextAwarePredicate);
      this.cartPredicate = predicate;
    }

    public static CartRidingTrigger.Instance hasRidden() {
      return new CartRidingTrigger.Instance(ContextAwarePredicate.ANY, MinecartPredicate.ANY);
    }

    public boolean matches(ServerPlayer player, AbstractMinecart cart) {
      return cartPredicate.test(player, cart);
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
