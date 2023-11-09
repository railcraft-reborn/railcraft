package mods.railcraft.advancements;

import java.util.Optional;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Criterion;
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
    var used = ItemPredicate.fromJson(json.get("item"));
    var location = LocationPredicate.fromJson(json.get("location"));
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

  public static Criterion<Instance> hasUsedTrackKit() {
    return RailcraftCriteriaTriggers.TRACK_KIT_USE.createCriterion(
        new Instance(Optional.empty(), Optional.empty(), Optional.empty()));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final Optional<ItemPredicate> item;
    private final Optional<LocationPredicate> location;

    private Instance(Optional<ContextAwarePredicate> contextAwarePredicate,
        Optional<ItemPredicate> itemPredicate, Optional<LocationPredicate> locationPredicate) {
      super(contextAwarePredicate);
      this.item = itemPredicate;
      this.location = locationPredicate;
    }

    public boolean matches(ServerLevel level, BlockPos blockPos, ItemStack stack) {
      return this.item.map(x -> x.matches(stack)).orElse(true)
          && this.location.map(x ->
          x.matches(level, blockPos.getX(), blockPos.getY(), blockPos.getZ())).orElse(true);
    }

    @Override
    public JsonObject serializeToJson() {
      var json = super.serializeToJson();
      this.item.ifPresent(x -> json.add("item", x.serializeToJson()));
      this.location.ifPresent(x -> json.add("location", x.serializeToJson()));
      return json;
    }
  }
}
