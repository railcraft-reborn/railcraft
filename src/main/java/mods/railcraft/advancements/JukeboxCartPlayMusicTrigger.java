package mods.railcraft.advancements;

import java.util.Optional;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.Criterion;
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
    // TODO: Test music encoding/decoding
    var music = ResourceLocation.CODEC.parse(JsonOps.INSTANCE, json.get("music")).result();
    var minecart = MinecartPredicate.fromJson(json.get("cart"));
    return new JukeboxCartPlayMusicTrigger.Instance(contextAwarePredicate, music, minecart);
  }

  /**
   * Invoked when the user plays music on a cart.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cart,
      ResourceLocation music) {
    this.trigger(playerEntity,
        (criterionInstance) -> criterionInstance.matches(playerEntity, cart, music));
  }

  public static Criterion<Instance> hasPlayedAnyMusic() {
    return RailcraftCriteriaTriggers.JUKEBOX_CART_MUSIC_PLAY.createCriterion(
        new Instance(Optional.empty(), Optional.empty(), Optional.empty()));
  }

  public static Criterion<Instance> hasPlayedMusic(Optional<ResourceLocation> music) {
    return RailcraftCriteriaTriggers.JUKEBOX_CART_MUSIC_PLAY.createCriterion(
        new Instance(Optional.empty(), music, Optional.empty()));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final Optional<ResourceLocation> music;
    private final Optional<MinecartPredicate> cart;

    private Instance(Optional<ContextAwarePredicate> contextAwarePredicate,
        Optional<ResourceLocation> music, Optional<MinecartPredicate> cart) {
      super(contextAwarePredicate);
      this.music = music;
      this.cart = cart;
    }

    public boolean matches(ServerPlayer player, AbstractMinecart cart, ResourceLocation music) {
      if (this.music.isPresent() && !this.music.get().equals(music)) {
        return false;
      }
      return this.cart.map(x -> x.matches(player, cart)).orElse(false);
    }

    @Override
    public JsonObject serializeToJson() {
      var json = super.serializeToJson();
      this.music.ifPresent(x -> {
        var encode = ResourceLocation.CODEC.encodeStart(JsonOps.INSTANCE, x).result();
        json.add("music", encode.get());
      });
      this.cart.ifPresent(x -> json.add("cart", x.serializeToJson()));
      return json;
    }
  }
}
