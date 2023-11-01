package mods.railcraft.advancements;

import java.util.Optional;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import mods.railcraft.season.Season;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class SetSeasonTrigger extends SimpleCriterionTrigger<SetSeasonTrigger.Instance> {

  @Override
  public Instance createInstance(JsonObject json,
      Optional<ContextAwarePredicate> contextAwarePredicate,
      DeserializationContext deserializationContext) {
    // TODO: Test season encoding/decoding
    var season = Season.CODEC.parse(JsonOps.INSTANCE, json.get("season")).result();
    var minecart = MinecartPredicate.fromJson(json.get("cart"));
    return new SetSeasonTrigger.Instance(contextAwarePredicate, season, minecart);
  }

  /**
   * Invoked when the user changes the cart's season option I think?.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cart,
      Season target) {
    this.trigger(playerEntity,
        (criterionInstance) -> criterionInstance.matches(playerEntity, cart, target));
  }

  public static Criterion<Instance> onSeasonSet() {
    return RailcraftCriteriaTriggers.SEASON_SET.createCriterion(
        new Instance(Optional.empty(), Optional.empty(), Optional.empty()));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {
    private final Optional<Season> season;
    private final Optional<MinecartPredicate> cart;

    private Instance(Optional<ContextAwarePredicate> contextAwarePredicate,
        Optional<Season> season, Optional<MinecartPredicate> cart) {
      super(contextAwarePredicate);
      this.season = season;
      this.cart = cart;
    }

    public boolean matches(ServerPlayer player, AbstractMinecart cart, Season season) {
      if (this.season.isPresent() && !this.season.get().equals(season)) {
        return false;
      }
      return this.cart.map(x -> x.matches(player, cart)).orElse(false);
    }

    @Override
    public JsonObject serializeToJson() {
      var json = super.serializeToJson();
      this.season.ifPresent(x -> {
        var encode = Season.CODEC.encodeStart(JsonOps.INSTANCE, x).result();
        json.add("season", encode.get());
      });
      this.cart.ifPresent(x -> json.add("cart", x.serializeToJson()));
      return json;
    }
  }
}
