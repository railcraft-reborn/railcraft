package mods.railcraft.advancements;

import java.util.Optional;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.util.LevelUtil;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.NbtPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class SpikeMaulUseTrigger extends
    SimpleCriterionTrigger<SpikeMaulUseTrigger.TriggerInstance> {

  /**
   * Invoked when the user successfully uses a spike maul.
   */
  public void trigger(ServerPlayer player, ItemStack item,
      ServerLevel serverLevel, BlockPos pos) {
    this.trigger(player, criterionInstance -> criterionInstance.matches(item, serverLevel, pos));
  }

  public static Criterion<TriggerInstance> hasUsedSpikeMaul() {
    return RailcraftCriteriaTriggers.SPIKE_MAUL_USE.createCriterion(
        new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty()));
  }

  @Override
  public Codec<TriggerInstance> codec() {
    return TriggerInstance.CODEC;
  }

  public record TriggerInstance(
      Optional<ContextAwarePredicate> player,
      Optional<NbtPredicate> nbt,
      Optional<ItemPredicate> tool,
      Optional<LocationPredicate> location) implements SimpleCriterionTrigger.SimpleInstance {

    public static final Codec<TriggerInstance> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                .forGetter(TriggerInstance::player),
            NbtPredicate.CODEC.optionalFieldOf("nbt")
                .forGetter(TriggerInstance::nbt),
            ItemPredicate.CODEC.optionalFieldOf("tool")
                .forGetter(TriggerInstance::tool),
            LocationPredicate.CODEC.optionalFieldOf("location")
                .forGetter(TriggerInstance::location)
        ).apply(instance, TriggerInstance::new));

    public boolean matches(ItemStack item, ServerLevel level, BlockPos pos) {
      return LevelUtil.getBlockEntity(level, pos)
          .map(x -> x.saveWithoutMetadata(level.registryAccess()))
          .map(tag -> this.nbt.map(x -> x.matches(tag)).orElse(true))
          .orElse(false)
          && this.tool.map(x -> x.test(item)).orElse(true)
          && this.location.map(x -> x.matches(level, pos.getX(), pos.getY(), pos.getZ()))
          .orElse(true);
    }

    @Override
    public Optional<ContextAwarePredicate> player() {
      return player;
    }
  }
}
