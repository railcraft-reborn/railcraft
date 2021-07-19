package mods.railcraft.advancements.criterion;

import java.util.Map;
import java.util.Map.Entry;
import com.google.common.collect.MapMaker;
import com.google.gson.JsonObject;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.util.JsonTools;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

final class CartRidingTrigger extends BaseTrigger<CartRidingTrigger.Instance> {

  static final ResourceLocation ID = RailcraftConstantsAPI.locationOf("cart_riding");
  private static final int FREQUENCY = 20;

  private final Map<ServerPlayerEntity, AbstractMinecartEntity> mounting =
      new MapMaker().weakKeys().weakValues().makeMap();

  private int counter;

  CartRidingTrigger() {
    MinecraftForge.EVENT_BUS.register(this);
  }

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

  @SubscribeEvent
  public void onMount(EntityMountEvent event) {
    if (!(event.getEntityMounting() instanceof ServerPlayerEntity)
        || !(event.getEntityBeingMounted() instanceof AbstractMinecartEntity)) {
      return;
    }

    ServerPlayerEntity rider = (ServerPlayerEntity) event.getEntityMounting();
    AbstractMinecartEntity cart = (AbstractMinecartEntity) event.getEntityBeingMounted();

    if (event.isMounting()) {
      mounting.put(rider, cart);
    } else {
      mounting.remove(rider);
    }
  }

  @SubscribeEvent
  public void tick(ServerTickEvent event) {
    if (counter != FREQUENCY) {
      counter++;
      return;
    }
    counter = 0;

    for (Entry<ServerPlayerEntity, AbstractMinecartEntity> entry : mounting.entrySet()) {
      trigger(entry.getKey(), instance -> instance.test(entry.getKey(), entry.getValue()));
    }
  }

  static final class Instance implements ICriterionInstance {

    final CartPredicate cartPredicate;

    Instance(CartPredicate predicate) {
      this.cartPredicate = predicate;
    }

    boolean test(ServerPlayerEntity player, AbstractMinecartEntity cart) {
      return cartPredicate.test(player, cart);
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
