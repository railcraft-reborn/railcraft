package mods.railcraft.advancements.criterion;

import java.util.ArrayList;
import java.util.Collection;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.Ownable;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.api.event.CartLinkEvent;
import mods.railcraft.util.JsonTools;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

final class CartLinkingTrigger extends BaseTrigger<CartLinkingTrigger.Instance> {

  static final ResourceLocation ID = RailcraftConstantsAPI.locationOf("cart_linking");

  CartLinkingTrigger() {
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public Instance createInstance(JsonObject json, ConditionArrayParser parser) {
    CartPredicate owned =
        JsonTools.whenPresent(json, "owned", CartPredicate::deserialize, CartPredicate.ANY);
    CartPredicate other =
        JsonTools.whenPresent(json, "other", CartPredicate::deserialize, CartPredicate.ANY);
    return new Instance(owned, other);
  }

  @SubscribeEvent
  public void onCartLink(CartLinkEvent.Link event) {
    this.processCart(event.getCartOne(), event.getCartTwo());
    this.processCart(event.getCartTwo(), event.getCartOne());
  }

  private void processCart(AbstractMinecartEntity cart, AbstractMinecartEntity linkedCart) {
    if (!(cart instanceof Ownable)) {
      return;
    }

    ServerPlayerEntity owner = ((Ownable) cart).getOwner()
        .map(GameProfile::getId)
        .map(cart.level::getPlayerByUUID)
        .filter(ServerPlayerEntity.class::isInstance)
        .map(ServerPlayerEntity.class::cast)
        .orElse(null);
    if (owner == null) {
      return;
    }

    Collection<Listener<Instance>> done = new ArrayList<>();
    for (Listener<Instance> listener : this.manager.get(owner.getAdvancements())) {
      Instance instance = listener.getTriggerInstance();
      if (instance.test(owner, cart, linkedCart)) {
        done.add(listener);
      }
    }

    for (Listener<Instance> listener : done) {
      listener.run(owner.getAdvancements());
    }
  }

  static final class Instance implements ICriterionInstance {

    final CartPredicate owned;
    final CartPredicate other;

    Instance(CartPredicate owned, CartPredicate other) {
      this.owned = owned;
      this.other = other;
    }

    boolean test(ServerPlayerEntity player, AbstractMinecartEntity owned,
        AbstractMinecartEntity other) {
      return this.owned.test(player, owned) && this.other.test(player, other);
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(ConditionArraySerializer serializer) {
      JsonObject json = new JsonObject();
      json.add("owned", this.owned.serializeToJson());
      json.add("other", this.other.serializeToJson());
      return json;
    }
  }
}
