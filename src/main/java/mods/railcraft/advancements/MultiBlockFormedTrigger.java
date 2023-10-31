package mods.railcraft.advancements;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import com.google.gson.JsonObject;
import mods.railcraft.util.Conditions;
import mods.railcraft.util.JsonUtil;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.ForgeRegistries;

public class MultiBlockFormedTrigger extends
    SimpleCriterionTrigger<MultiBlockFormedTrigger.Instance> {
  @Override
  protected MultiBlockFormedTrigger.Instance createInstance(JsonObject json,
      Optional<ContextAwarePredicate> contextAwarePredicate,
      DeserializationContext deserializationContext) {
    var type = JsonUtil.getFromRegistry(json, "type", ForgeRegistries.BLOCK_ENTITY_TYPES)
        .orElse(null);
    var nbt = JsonUtil.getAsJsonObject(json, "nbt")
        .map(NbtPredicate::fromJson)
        .orElse(NbtPredicate.ANY);
    return new MultiBlockFormedTrigger.Instance(contextAwarePredicate, type, nbt);
  }

  /**
   * Invoked when the user forms a multiblock.
   */
  public void trigger(ServerPlayer playerEntity, RailcraftBlockEntity blockEntity) {
    this.trigger(playerEntity, (criterionInstance) -> criterionInstance.matches(blockEntity));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    @Nullable
    private final BlockEntityType<?> type;
    private final NbtPredicate predicate;

    private Instance(Optional<ContextAwarePredicate> contextAwarePredicate,
        @Nullable BlockEntityType<?> type, NbtPredicate predicate) {
      super(contextAwarePredicate);
      this.type = type;
      this.predicate = predicate;
    }

    public static MultiBlockFormedTrigger.Instance formedMultiBlock(
        BlockEntityType<?> tileEntityType) {
      return formedMultiBlock(tileEntityType, NbtPredicate.ANY);
    }

    public static MultiBlockFormedTrigger.Instance formedMultiBlock(
        BlockEntityType<?> tileEntityType, NbtPredicate nbtPredicate) {
      return new MultiBlockFormedTrigger.Instance(ContextAwarePredicate.ANY, tileEntityType,
          nbtPredicate);
    }

    public boolean matches(RailcraftBlockEntity blockEntity) {
      return Conditions.check(this.type, blockEntity.getType())
          && this.predicate.matches(blockEntity.saveWithoutMetadata());
    }

    @Override
    public JsonObject serializeToJson() {
      JsonObject json = new JsonObject();
      if (this.type != null) {
        json.addProperty("type", ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(this.type).toString());
      }
      json.add("nbt", this.predicate.serializeToJson());
      return json;
    }
  }

}
