package mods.railcraft.advancements;

import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import com.google.gson.JsonObject;
import mods.railcraft.util.JsonUtil;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class JukeboxCartPlayMusicTrigger
    extends SimpleCriterionTrigger<JukeboxCartPlayMusicTrigger.Instance> {

  @Override
  public JukeboxCartPlayMusicTrigger.Instance createInstance(JsonObject json,
      Optional<ContextAwarePredicate> contextAwarePredicate,
      DeserializationContext deserializationContext) {
    var sound = JsonUtil.getAsString(json, "music")
        .map(ResourceLocation::tryParse)
        .orElse(null);
    var cart = JsonUtil.getAsJsonObject(json, "cart")
        .map(MinecartPredicate::deserialize)
        .orElse(MinecartPredicate.ANY);
    return new JukeboxCartPlayMusicTrigger.Instance(contextAwarePredicate, sound, cart);
  }

  /**
   * Invoked when the user plays music on a cart.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cart,
      ResourceLocation music) {
    this.trigger(playerEntity,
        (criterionInstance) -> criterionInstance.matches(playerEntity, cart, music));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    @Nullable
    private final ResourceLocation music;
    private final MinecartPredicate cart;

    private Instance(Optional<ContextAwarePredicate> contextAwarePredicate,
        @Nullable ResourceLocation music, MinecartPredicate cart) {
      super(contextAwarePredicate);
      this.music = music;
      this.cart = cart;
    }

    public static JukeboxCartPlayMusicTrigger.Instance hasPlayedAnyMusic() {
      return new JukeboxCartPlayMusicTrigger.Instance(ContextAwarePredicate.ANY,
          null, MinecartPredicate.ANY);
    }

    public static JukeboxCartPlayMusicTrigger.Instance hasPlayedMusic(ResourceLocation music) {
      return new JukeboxCartPlayMusicTrigger.Instance(ContextAwarePredicate.ANY,
          music, MinecartPredicate.ANY);
    }

    public boolean matches(ServerPlayer player, AbstractMinecart cart, ResourceLocation sound) {
      return (music == null || Objects.equals(sound, music)) && this.cart.test(player, cart);
    }

    @Override
    public JsonObject serializeToJson() {
      JsonObject json = new JsonObject();
      if (this.music != null) {
        json.addProperty("music", this.music.toString());
      }
      json.add("cart", this.cart.serializeToJson());
      return json;
    }
  }
}
