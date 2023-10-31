package mods.railcraft.advancements;

import java.util.Optional;
import com.google.gson.JsonObject;
import mods.railcraft.util.JsonUtil;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class UseTrackKitTrigger extends SimpleCriterionTrigger<UseTrackKitTrigger.Instance> {
  @Override
  public UseTrackKitTrigger.Instance createInstance(JsonObject json,
      Optional<ContextAwarePredicate> contextAwarePredicate,
      DeserializationContext deserializationContext) {
    var used = JsonUtil.getAsJsonObject(json, "item")
        .map(ItemPredicate::fromJson)
        .orElse(ItemPredicate.ANY);
    var location = JsonUtil.getAsJsonObject(json, "location")
        .map(LocationPredicate::fromJson)
        .orElse(LocationPredicate.ANY);
    return new UseTrackKitTrigger.Instance(contextAwarePredicate, used, location);
  }

  /**
   * Invoked when the user explodes a cart.
   */
  public void trigger(ServerPlayer playerEntity, ServerLevel serverLevel,
      BlockPos blockPos, ItemStack stack) {
    this.trigger(playerEntity,
        (criterionInstance) -> criterionInstance.matches(serverLevel, blockPos, stack));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final ItemPredicate item;
    private final LocationPredicate location;

    private Instance(Optional<ContextAwarePredicate> contextAwarePredicate,
        ItemPredicate itemPredicate, LocationPredicate locationPredicate) {
      super(contextAwarePredicate);
      this.item = itemPredicate;
      this.location = locationPredicate;
    }

    public static UseTrackKitTrigger.Instance hasUsedTrackKit() {
      return new UseTrackKitTrigger.Instance(ContextAwarePredicate.ANY,
          ItemPredicate.ANY, LocationPredicate.ANY);
    }

    public boolean matches(ServerLevel level, BlockPos blockPos, ItemStack stack) {
      return item.matches(stack)
          && this.location.matches(level, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    @Override
    public JsonObject serializeToJson() {
      JsonObject json = new JsonObject();
      json.add("item", this.item.serializeToJson());
      json.add("location", this.location.serializeToJson());
      return json;
    }
  }
}
