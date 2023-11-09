package mods.railcraft.advancements;

import java.util.Optional;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

/**
 * TODO: Implament this on carts.
 */
public class CartRidingTrigger extends SimpleCriterionTrigger<CartRidingTrigger.Instance> {

  // private static final int FREQUENCY = 20;

  // private final Map<ServerPlayerEntity, AbstractMinecartEntity> mounting =
  // new MapMaker().weakKeys().weakValues().makeMap();

  // private int counter;

  // CartRidingTrigger() {
  // MinecraftForge.EVENT_BUS.register(this);
  // }


  @Override
  public CartRidingTrigger.Instance createInstance(JsonObject json,
      Optional<ContextAwarePredicate> contextAwarePredicate,
      DeserializationContext deserializationContext) {
    var minecart = MinecartPredicate.fromJson(json.get("cart"));
    return new CartRidingTrigger.Instance(contextAwarePredicate, minecart);
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

    private final Optional<MinecartPredicate> cartPredicate;

    private Instance(Optional<ContextAwarePredicate> contextAwarePredicate,
        Optional<MinecartPredicate> predicate) {
      super(contextAwarePredicate);
      this.cartPredicate = predicate;
    }

    public static CartRidingTrigger.Instance hasRidden() {
      return new CartRidingTrigger.Instance(Optional.empty(), Optional.empty());
    }

    public boolean matches(ServerPlayer player, AbstractMinecart cart) {
      return cartPredicate.map(x -> x.matches(player, cart)).orElse(true);
    }

    @Override
    public JsonObject serializeToJson() {
      var json = super.serializeToJson();
      this.cartPredicate.ifPresent(x -> json.add("cart", x.serializeToJson()));
      return json;
    }
  }
}
