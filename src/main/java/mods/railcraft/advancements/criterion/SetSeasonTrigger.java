package mods.railcraft.advancements.criterion;

import com.google.gson.JsonObject;

import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.season.Season;
import mods.railcraft.util.JsonTools;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

public class SetSeasonTrigger extends AbstractCriterionTrigger<SetSeasonTrigger.Instance> {

  private static final ResourceLocation ID = RailcraftConstantsAPI.locationOf("set_season");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public Instance createInstance(JsonObject json,
      EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser parser) {
    Season season = JsonTools.whenPresent(json, "season",
        (element) -> Season.getByName(element.getAsString()).orElse(null), null);
    CartPredicate cartPredicate =
        JsonTools.whenPresent(json, "cart", CartPredicate::deserialize, CartPredicate.ANY);
    return new SetSeasonTrigger.Instance(entityPredicate, season, cartPredicate);
  }

  /**
   * Invoked when the user changes the cart's season option i think?.
   */
  public void trigger(ServerPlayerEntity playerEntity, AbstractMinecartEntity cart,
      Season target) {
    this.trigger(playerEntity, (SetSeasonTrigger.Instance criterionInstance) -> {
      return criterionInstance.matches(playerEntity, cart, target);
    });
  }

  public static class Instance extends CriterionInstance {

    private final Season season;
    private final CartPredicate cartPredicate;

    Instance(EntityPredicate.AndPredicate entityPredicate, Season season, CartPredicate predicate) {
      super(SetSeasonTrigger.ID, entityPredicate);
      this.season = season;
      this.cartPredicate = predicate;
    }

    public static SetSeasonTrigger.Instance onSeasonSet() {
      return new SetSeasonTrigger.Instance(EntityPredicate.AndPredicate.ANY,
          Season.ANY, CartPredicate.ANY);
    }

    public boolean matches(ServerPlayerEntity player, AbstractMinecartEntity cart, Season target) {
      return this.season.equals(target) && this.cartPredicate.test(player, cart);
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(ConditionArraySerializer serializer) {
      JsonObject json = new JsonObject();
      json.addProperty("season", this.season.getSerializedName());
      json.add("cart", this.cartPredicate.serializeToJson());
      return json;
    }
  }
}
