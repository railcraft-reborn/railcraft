package mods.railcraft.advancements;

import org.jetbrains.annotations.Nullable;
import com.google.gson.JsonObject;
import mods.railcraft.Railcraft;
import mods.railcraft.season.Season;
import mods.railcraft.util.JsonUtil;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class SetSeasonTrigger extends SimpleCriterionTrigger<SetSeasonTrigger.Instance> {

  private static final ResourceLocation ID = Railcraft.rl("set_season");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public Instance createInstance(JsonObject json,
      ContextAwarePredicate contextAwarePredicate, DeserializationContext deserializationContext) {
    var season = JsonUtil.getAsString(json, "season")
        .map(Integer::valueOf)
        .map(x -> Season.values()[x])
        .orElse(null);
    var cartPredicate = JsonUtil.getAsJsonObject(json, "cart")
        .map(MinecartPredicate::deserialize)
        .orElse(MinecartPredicate.ANY);
    return new SetSeasonTrigger.Instance(contextAwarePredicate, season, cartPredicate);
  }

  /**
   * Invoked when the user changes the cart's season option i think?.
   */
  public void trigger(ServerPlayer playerEntity, AbstractMinecart cart,
      Season target) {
    this.trigger(playerEntity,
        (criterionInstance) -> criterionInstance.matches(playerEntity, cart, target));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    @Nullable
    private final Season season;
    private final MinecartPredicate cartPredicate;

    private Instance(ContextAwarePredicate contextAwarePredicate, @Nullable Season season,
        MinecartPredicate predicate) {
      super(SetSeasonTrigger.ID, contextAwarePredicate);
      this.season = season;
      this.cartPredicate = predicate;
    }

    public static SetSeasonTrigger.Instance onSeasonSet() {
      return new SetSeasonTrigger.Instance(ContextAwarePredicate.ANY,
          null, MinecartPredicate.ANY);
    }

    public boolean matches(ServerPlayer player, AbstractMinecart cart, Season target) {
      return (this.season == null || this.season.equals(target))
          && this.cartPredicate.test(player, cart);
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(SerializationContext serializer) {
      JsonObject json = new JsonObject();
      if (this.season != null) {
        json.addProperty("season", this.season.ordinal());
      }
      json.add("cart", this.cartPredicate.serializeToJson());
      return json;
    }
  }
}
