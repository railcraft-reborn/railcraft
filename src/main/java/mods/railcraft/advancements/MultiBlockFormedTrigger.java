package mods.railcraft.advancements;

import java.util.Optional;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.advancements.predicates.NbtPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class MultiBlockFormedTrigger extends
    SimpleCriterionTrigger<MultiBlockFormedTrigger.TriggerInstance> {

  /**
   * Invoked when the user forms a multiblock.
   */
  public void trigger(ServerPlayer playerEntity, RailcraftBlockEntity blockEntity) {
    this.trigger(playerEntity, (criterionInstance) -> criterionInstance.matches(blockEntity));
  }

  public static Criterion<?> formedMultiBlock(BlockEntityType<?> tileEntityType) {
    return formedMultiBlock(tileEntityType, Optional.empty());
  }

  public static Criterion<TriggerInstance> formedMultiBlock(
      BlockEntityType<?> tileEntityType, Optional<NbtPredicate> nbtPredicate) {
    return RailcraftCriteriaTriggers.MULTIBLOCK_FORM.createCriterion(
        new TriggerInstance(Optional.empty(), Optional.of(tileEntityType), nbtPredicate));
  }

  @Override
  public Codec<TriggerInstance> codec() {
    return TriggerInstance.CODEC;
  }

  public record TriggerInstance(
      Optional<Holder<LootItemCondition>> player,
      Optional<BlockEntityType<?>> type,
      Optional<NbtPredicate> nbt) implements SimpleCriterionTrigger.SimpleInstance {

    public static final Codec<TriggerInstance> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            LootItemCondition.CODEC.optionalFieldOf("player")
                .forGetter(TriggerInstance::player),
            BuiltInRegistries.BLOCK_ENTITY_TYPE.byNameCodec().optionalFieldOf("type")
                .forGetter(TriggerInstance::type),
            NbtPredicate.CODEC.optionalFieldOf("nbt")
                .forGetter(TriggerInstance::nbt)
        ).apply(instance, TriggerInstance::new));

    public boolean matches(RailcraftBlockEntity blockEntity) {
      return this.type.map(type -> type.equals(blockEntity.getType())).orElse(true)
          && this.nbt.map(predicate -> {
            var registryAccess = blockEntity.level().registryAccess();
            return predicate.matches(blockEntity.saveWithoutMetadata(registryAccess));
          })
          .orElse(true);
    }

    @Override
    public Optional<Holder<LootItemCondition>> player() {
      return player;
    }
  }
}
