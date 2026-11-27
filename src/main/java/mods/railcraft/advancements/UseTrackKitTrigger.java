package mods.railcraft.advancements;

import java.util.Optional;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class UseTrackKitTrigger extends SimpleCriterionTrigger<UseTrackKitTrigger.TriggerInstance> {

  /**
   * Invoked when the user explodes a cart.
   */
  public void trigger(ServerPlayer playerEntity, ServerLevel serverLevel,
      BlockPos blockPos, ItemStack stack) {
    this.trigger(playerEntity,
        criterionInstance -> criterionInstance.matches(serverLevel, blockPos, stack));
  }

  public static Criterion<TriggerInstance> hasUsedTrackKit() {
    return RailcraftCriteriaTriggers.TRACK_KIT_USE.createCriterion(
        new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
  }

  @Override
  public Codec<TriggerInstance> codec() {
    return TriggerInstance.CODEC;
  }

  public record TriggerInstance(
      Optional<Holder<LootItemCondition>> player,
      Optional<ItemPredicate> item,
      Optional<LocationPredicate> location) implements SimpleCriterionTrigger.SimpleInstance {

    public static final Codec<TriggerInstance> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            LootItemCondition.CODEC.optionalFieldOf("player")
                .forGetter(TriggerInstance::player),
            ItemPredicate.CODEC.optionalFieldOf("item")
                .forGetter(TriggerInstance::item),
            LocationPredicate.CODEC.optionalFieldOf("location")
                .forGetter(TriggerInstance::location)
        ).apply(instance, TriggerInstance::new));

    public boolean matches(ServerLevel level, BlockPos blockPos, ItemStack stack) {
      return this.item.map(x -> x.test(stack)).orElse(true)
          && this.location.map(x ->
          x.matches(level, blockPos.getX(), blockPos.getY(), blockPos.getZ())).orElse(true);
    }

    @Override
    public Optional<Holder<LootItemCondition>> player() {
      return player;
    }
  }
}
