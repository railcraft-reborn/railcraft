package mods.railcraft.advancements;

import java.util.Optional;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class MultiBlockFormedTrigger extends
    SimpleCriterionTrigger<MultiBlockFormedTrigger.Instance> {
  @Override
  protected MultiBlockFormedTrigger.Instance createInstance(JsonObject json,
      Optional<ContextAwarePredicate> contextAwarePredicate,
      DeserializationContext deserializationContext) {
    var type = BuiltInRegistries.BLOCK_ENTITY_TYPE.byNameCodec()
        .decode(JsonOps.INSTANCE, json.get("type"))
        .map(Pair::getFirst)
        .result();
    var nbt = NbtPredicate.CODEC.decode(JsonOps.INSTANCE, json.get("nbt")).result()
        .map(Pair::getFirst);
    return new MultiBlockFormedTrigger.Instance(contextAwarePredicate, type, nbt);
  }

  /**
   * Invoked when the user forms a multiblock.
   */
  public void trigger(ServerPlayer playerEntity, RailcraftBlockEntity blockEntity) {
    this.trigger(playerEntity, (criterionInstance) -> criterionInstance.matches(blockEntity));
  }

  public static Criterion<?> formedMultiBlock(BlockEntityType<?> tileEntityType) {
    return formedMultiBlock(tileEntityType, Optional.empty());
  }

  public static Criterion<Instance> formedMultiBlock(
      BlockEntityType<?> tileEntityType, Optional<NbtPredicate> nbtPredicate) {
    return RailcraftCriteriaTriggers.MULTIBLOCK_FORM.createCriterion(
        new Instance(Optional.empty(), Optional.of(tileEntityType), nbtPredicate));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final Optional<? extends BlockEntityType<?>> type;
    private final Optional<NbtPredicate> nbt;

    private Instance(Optional<ContextAwarePredicate> contextAwarePredicate,
        Optional<? extends BlockEntityType<?>> type, Optional<NbtPredicate> nbt) {
      super(contextAwarePredicate);
      this.type = type;
      this.nbt = nbt;
    }

    public boolean matches(RailcraftBlockEntity blockEntity) {
      return this.type.map(type -> type.equals(blockEntity.getType())).orElse(true)
          && this.nbt.map(predicate -> predicate.matches(blockEntity.saveWithoutMetadata()))
              .orElse(true);
    }

    @Override
    public JsonObject serializeToJson() {
      var json = super.serializeToJson();
      this.type.ifPresent(x -> {
        var encode = BuiltInRegistries.BLOCK_ENTITY_TYPE.byNameCodec()
            .encodeStart(JsonOps.INSTANCE, x).result();
        json.add("type", encode.get());
      });
      this.nbt.ifPresent(x -> {
        var encode = NbtPredicate.CODEC
            .encodeStart(JsonOps.INSTANCE, x).result();
        json.add("nbt", encode.get());
      });
      return json;
    }
  }

}
