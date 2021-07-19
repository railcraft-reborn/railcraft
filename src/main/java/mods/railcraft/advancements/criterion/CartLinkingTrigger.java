package mods.railcraft.advancements.criterion;

import java.util.ArrayList;
import java.util.Collection;
import com.google.gson.JsonObject;
import mods.railcraft.api.carts.CartToolsAPI;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.api.events.CartLinkEvent;
import mods.railcraft.plugins.PlayerPlugin;
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
    AbstractMinecartEntity one = event.getCartOne();
    AbstractMinecartEntity two = event.getCartTwo();

    ServerPlayerEntity ownerOne =
        (ServerPlayerEntity) PlayerPlugin.getPlayer(one.level, CartToolsAPI.getCartOwner(one));
    ServerPlayerEntity ownerTwo =
        (ServerPlayerEntity) PlayerPlugin.getPlayer(two.level, CartToolsAPI.getCartOwner(two));

    Collection<Listener<Instance>> doneOne = new ArrayList<>();
    Collection<Listener<Instance>> doneTwo = new ArrayList<>();

    if (ownerOne != null) {
      for (Listener<Instance> listener : manager.get(ownerOne.getAdvancements())) {
        Instance instance = listener.getTriggerInstance();
        if (instance.test(ownerOne, one, two)) {
          doneOne.add(listener);
        }
      }
    }

    if (ownerTwo != null) {
      for (Listener<Instance> listener : manager.get(ownerTwo.getAdvancements())) {
        Instance instance = listener.getTriggerInstance();
        if (instance.test(ownerTwo, two, one)) {
          doneTwo.add(listener);
        }
      }
    }

    for (Listener<Instance> listener : doneOne) {
      listener.run(ownerOne.getAdvancements());
    }

    for (Listener<Instance> listener : doneTwo) {
      listener.run(ownerTwo.getAdvancements());
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
