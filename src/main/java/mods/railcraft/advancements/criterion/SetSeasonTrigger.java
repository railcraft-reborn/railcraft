package mods.railcraft.advancements.criterion;

import javax.annotation.Nullable;
import com.google.gson.JsonObject;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.season.Season;
import mods.railcraft.util.Conditions;
import mods.railcraft.util.JsonTools;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

final class SetSeasonTrigger extends BaseTrigger<SetSeasonTrigger.Instance> {

  static final ResourceLocation ID = RailcraftConstantsAPI.locationOf("set_season");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public Instance createInstance(JsonObject json, ConditionArrayParser parser) {
    Season season = JsonTools.whenPresent(json, "season",
        (element) -> Season.getByName(element.getAsString()).orElse(null), null);
    CartPredicate cartPredicate =
        JsonTools.whenPresent(json, "cart", CartPredicate::deserialize, CartPredicate.ANY);
    return new Instance(season, cartPredicate);
  }

  static final class Instance implements ICriterionInstance {

    @Nullable
    final Season season;
    final CartPredicate cartPredicate;

    Instance(@Nullable Season season, CartPredicate predicate) {
      this.season = season;
      this.cartPredicate = predicate;
    }

    boolean test(ServerPlayerEntity player, AbstractMinecartEntity cart, Season target) {
      return Conditions.check(this.season, target) && this.cartPredicate.test(player, cart);
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(ConditionArraySerializer p_230240_1_) {
      JsonObject json = new JsonObject();
      json.addProperty("season", this.season.getSerializedName());
      json.add("cart", this.cartPredicate.serializeToJson());
      return json;
    }
  }
}
