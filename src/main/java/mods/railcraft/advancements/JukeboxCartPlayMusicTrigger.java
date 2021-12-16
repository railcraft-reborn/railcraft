package mods.railcraft.advancements;

import com.google.gson.JsonObject;

import java.util.Objects;

import javax.annotation.Nullable;

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

public class JukeboxCartPlayMusicTrigger
    extends SimpleCriterionTrigger<JukeboxCartPlayMusicTrigger.Instance> {

  private static final ResourceLocation ID =
      new ResourceLocation(Railcraft.ID + ":jukebox_cart_play_music");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public JukeboxCartPlayMusicTrigger.Instance createInstance(JsonObject json,
      EntityPredicate.Composite entityPredicate, DeserializationContext parser) {
    ResourceLocation sound = JsonTools.whenPresent(json, "music",
        (element) -> new ResourceLocation(element.getAsString()), null);
    CartPredicate cart =
        JsonTools.whenPresent(json, "cart", CartPredicate::deserialize, CartPredicate.ANY);
    return new JukeboxCartPlayMusicTrigger.Instance(entityPredicate, sound, cart);
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
    private final CartPredicate cart;

    private Instance(EntityPredicate.Composite entityPredicate,
        @Nullable ResourceLocation music, CartPredicate cart) {
      super(JukeboxCartPlayMusicTrigger.ID, entityPredicate);
      this.music = music;
      this.cart = cart;
    }

    public static JukeboxCartPlayMusicTrigger.Instance hasPlayedAnyMusic() {
      return new JukeboxCartPlayMusicTrigger.Instance(EntityPredicate.Composite.ANY,
          null, CartPredicate.ANY);
    }

    public static JukeboxCartPlayMusicTrigger.Instance hasPlayedMusic(ResourceLocation music) {
      return new JukeboxCartPlayMusicTrigger.Instance(EntityPredicate.Composite.ANY,
          music, CartPredicate.ANY);
    }

    public boolean matches(ServerPlayer player, AbstractMinecart cart,
        ResourceLocation sound) {
      return (music == null || Objects.equals(sound, music)) && this.cart.test(player, cart);
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(SerializationContext serializer) {
      JsonObject json = new JsonObject();
      if (this.music != null) {
        json.addProperty("music", this.music.toString());
      }
      json.add("cart", this.cart.serializeToJson());
      return json;
    }
  }
}
