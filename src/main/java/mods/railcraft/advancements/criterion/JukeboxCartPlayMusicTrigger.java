package mods.railcraft.advancements.criterion;

import com.google.gson.JsonObject;

import java.util.Objects;

import javax.annotation.Nullable;

import mods.railcraft.Railcraft;
import mods.railcraft.util.JsonTools;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

public class JukeboxCartPlayMusicTrigger
    extends AbstractCriterionTrigger<JukeboxCartPlayMusicTrigger.Instance> {

  private static final ResourceLocation ID =
      new ResourceLocation(Railcraft.ID + ":jukebox_cart_play_music");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public JukeboxCartPlayMusicTrigger.Instance createInstance(JsonObject json,
      EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser parser) {
    ResourceLocation sound = JsonTools.whenPresent(json, "music",
        (element) -> new ResourceLocation(element.getAsString()), null);
    CartPredicate cart =
        JsonTools.whenPresent(json, "cart", CartPredicate::deserialize, CartPredicate.ANY);
    return new JukeboxCartPlayMusicTrigger.Instance(entityPredicate, sound, cart);
  }

  /**
   * Invoked when the user plays music on a cart.
   */
  public void trigger(ServerPlayerEntity playerEntity, AbstractMinecartEntity cart,
      ResourceLocation music) {
    this.trigger(playerEntity,
        (criterionInstance) -> criterionInstance.matches(playerEntity, cart, music));
  }

  public static class Instance extends CriterionInstance {

    @Nullable
    private final ResourceLocation music;
    private final CartPredicate cart;

    private Instance(EntityPredicate.AndPredicate entityPredicate,
        @Nullable ResourceLocation music, CartPredicate cart) {
      super(JukeboxCartPlayMusicTrigger.ID, entityPredicate);
      this.music = music;
      this.cart = cart;
    }

    public static JukeboxCartPlayMusicTrigger.Instance hasPlayedAnyMusic() {
      return new JukeboxCartPlayMusicTrigger.Instance(EntityPredicate.AndPredicate.ANY,
          null, CartPredicate.ANY);
    }

    public static JukeboxCartPlayMusicTrigger.Instance hasPlayedMusic(ResourceLocation music) {
      return new JukeboxCartPlayMusicTrigger.Instance(EntityPredicate.AndPredicate.ANY,
          music, CartPredicate.ANY);
    }

    public boolean matches(ServerPlayerEntity player, AbstractMinecartEntity cart,
        ResourceLocation sound) {
      return (music == null || Objects.equals(sound, music)) && this.cart.test(player, cart);
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(ConditionArraySerializer serializer) {
      JsonObject json = new JsonObject();
      if (this.music != null) {
        json.addProperty("music", this.music.toString());
      }
      json.add("cart", this.cart.serializeToJson());
      return json;
    }
  }
}
