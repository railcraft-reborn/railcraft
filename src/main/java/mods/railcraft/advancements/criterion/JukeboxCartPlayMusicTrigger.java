package mods.railcraft.advancements.criterion;

import java.util.Objects;
import javax.annotation.Nullable;
import com.google.gson.JsonObject;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.util.JsonTools;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

final class JukeboxCartPlayMusicTrigger extends BaseTrigger<JukeboxCartPlayMusicTrigger.Instance> {

  static final ResourceLocation ID = RailcraftConstantsAPI.locationOf("jukebox_cart_play_music");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public Instance createInstance(JsonObject json, ConditionArrayParser parser) {
    ResourceLocation sound = JsonTools.whenPresent(json, "music",
        (element) -> new ResourceLocation(element.getAsString()), null);
    CartPredicate cart =
        JsonTools.whenPresent(json, "cart", CartPredicate::deserialize, CartPredicate.ANY);
    return new Instance(sound, cart);
  }

  static final class Instance implements ICriterionInstance {

    final @Nullable ResourceLocation music;
    final CartPredicate cart;

    Instance(@Nullable ResourceLocation music, CartPredicate cart) {
      this.music = music;
      this.cart = cart;
    }

    boolean test(ServerPlayerEntity player, AbstractMinecartEntity cart, ResourceLocation sound) {
      return (music == null || Objects.equals(sound, music)) && this.cart.test(player, cart);
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(ConditionArraySerializer p_230240_1_) {
      JsonObject json = new JsonObject();
      if (this.music != null)
        json.addProperty("music", this.music.toString());
      json.add("cart", this.cart.serializeToJson());
      return json;
    }
  }
}
