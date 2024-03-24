package mods.railcraft.advancements;

import java.util.Optional;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

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
      Optional<ContextAwarePredicate> player,
      Optional<ItemPredicate> item,
      Optional<LocationPredicate> location) implements SimpleCriterionTrigger.SimpleInstance {

    public static final Codec<TriggerInstance> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player")
                .forGetter(TriggerInstance::player),
            ExtraCodecs.strictOptionalField(ItemPredicate.CODEC, "item")
                .forGetter(TriggerInstance::item),
            ExtraCodecs.strictOptionalField(LocationPredicate.CODEC, "location")
                .forGetter(TriggerInstance::location)
        ).apply(instance, TriggerInstance::new));

    public boolean matches(ServerLevel level, BlockPos blockPos, ItemStack stack) {
      return this.item.map(x -> x.matches(stack)).orElse(true)
          && this.location.map(x ->
          x.matches(level, blockPos.getX(), blockPos.getY(), blockPos.getZ())).orElse(true);
    }

    @Override
    public Optional<ContextAwarePredicate> player() {
      return player;
    }
  }
}
