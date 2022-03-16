package mods.railcraft.advancements;

import javax.annotation.Nullable;
import com.google.gson.JsonObject;
import mods.railcraft.Railcraft;
import mods.railcraft.season.Season;
import mods.railcraft.util.JsonTools;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.resources.ResourceLocation;

public class SetSeasonTrigger extends SimpleCriterionTrigger<SetSeasonTrigger.Instance> {

  private static final ResourceLocation ID = new ResourceLocation(Railcraft.ID, "set_season");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public Instance createInstance(JsonObject json,
      EntityPredicate.Composite entityPredicate, DeserializationContext parser) {

    @Nullable
    Season season = JsonTools.whenPresent(json, "season",
        // || this element down here returns a json **object**, not an element, so you cannot
        // \/ use getAsString
        (element) -> Season.getByName(element.get("value").getAsString()).orElse(null), null);
    MinecartPredicate cartPredicate =
        JsonTools.whenPresent(json, "cart", MinecartPredicate::deserialize, MinecartPredicate.ANY);
    return new SetSeasonTrigger.Instance(entityPredicate, season, cartPredicate);
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

    private Instance(EntityPredicate.Composite entityPredicate, @Nullable Season season,
        MinecartPredicate predicate) {
      super(SetSeasonTrigger.ID, entityPredicate);
      this.season = season;
      this.cartPredicate = predicate;
    }

    public static SetSeasonTrigger.Instance onSeasonSet() {
      return new SetSeasonTrigger.Instance(EntityPredicate.Composite.ANY,
          null, MinecartPredicate.ANY);
    }

    public boolean matches(ServerPlayer player, AbstractMinecart cart, Season target) {
      return (this.season != null ? this.season.equals(target) : true)
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
        JsonObject season = new JsonObject();
        season.addProperty("value", this.season.getSerializedName());
        json.add("season", season);
      }
      json.add("cart", this.cartPredicate.serializeToJson());
      return json;
    }
  }
}
