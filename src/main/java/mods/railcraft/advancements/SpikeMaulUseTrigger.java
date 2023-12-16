package mods.railcraft.advancements;

import java.util.Optional;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.util.LevelUtil;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SpikeMaulUseTrigger extends SimpleCriterionTrigger<SpikeMaulUseTrigger.TriggerInstance> {

  /**
   * Invoked when the user successfully uses a spike maul.
   */
  public void trigger(ServerPlayer player, ItemStack item,
      ServerLevel serverLevel, BlockPos pos) {
    this.trigger(player, criterionInstance -> criterionInstance.matches(item, serverLevel, pos));
  }

  public static Criterion<TriggerInstance> hasUsedSpikeMaul() {
    return RailcraftCriteriaTriggers.SPIKE_MAUL_USE.createCriterion(
        new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
  }

  @Override
  public Codec<TriggerInstance> codec() {
    return TriggerInstance.CODEC;
  }

  public record TriggerInstance(Optional<ContextAwarePredicate> player,
                                Optional<NbtPredicate> nbt,
                                Optional<ItemPredicate> tool,
                                Optional<LocationPredicate> location)
      implements SimpleCriterionTrigger.SimpleInstance {

    public static final Codec<TriggerInstance> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player")
                .forGetter(TriggerInstance::player),
            ExtraCodecs.strictOptionalField(NbtPredicate.CODEC, "nbt")
                .forGetter(TriggerInstance::nbt),
            ExtraCodecs.strictOptionalField(ItemPredicate.CODEC, "tool")
                .forGetter(TriggerInstance::tool),
            ExtraCodecs.strictOptionalField(LocationPredicate.CODEC, "location")
                .forGetter(TriggerInstance::location)
        ).apply(instance, TriggerInstance::new));

    public boolean matches(ItemStack item, ServerLevel level, BlockPos pos) {
      return LevelUtil.getBlockEntity(level, pos)
          .map(BlockEntity::saveWithoutMetadata)
          .map(tag -> this.nbt.map(x -> x.matches(tag)).orElse(true))
          .orElse(false)
          && this.tool.map(x -> x.matches(item)).orElse(true)
          && this.location.map(x -> x.matches(level, pos.getX(), pos.getY(), pos.getZ())).orElse(true);
    }

    @Override
    public Optional<ContextAwarePredicate> player() {
      return player;
    }
  }
}
