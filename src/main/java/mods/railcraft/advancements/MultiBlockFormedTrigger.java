package mods.railcraft.advancements;

import com.google.gson.JsonObject;
import mods.railcraft.Railcraft;
import mods.railcraft.util.Conditions;
import mods.railcraft.util.JsonTools;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.advancements.critereon.*;
import net.minecraft.advancements.critereon.EntityPredicate.Composite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class MultiBlockFormedTrigger extends
    SimpleCriterionTrigger<MultiBlockFormedTrigger.Instance> {

  private static final ResourceLocation ID =
      new ResourceLocation(Railcraft.ID + ":multiblock_formed");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  protected MultiBlockFormedTrigger.Instance createInstance(JsonObject json,
      EntityPredicate.Composite andPredicate, DeserializationContext parser) {

    BlockEntityType<?> type = json.has("type")
        ? ForgeRegistries.BLOCK_ENTITY_TYPES.getValue(
            new ResourceLocation(json.get("type").getAsString()))
        : null;
    NbtPredicate nbt =
        JsonTools.whenPresent(json, "nbt", NbtPredicate::fromJson, NbtPredicate.ANY);

    return new MultiBlockFormedTrigger.Instance(andPredicate, type, nbt);
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

    private Instance(EntityPredicate.Composite entityPredicate,
        @Nullable BlockEntityType<?> type, NbtPredicate predicate) {
      super(MultiBlockFormedTrigger.ID, entityPredicate);
      this.type = type;
      this.predicate = predicate;
    }

    public static MultiBlockFormedTrigger.Instance formedMultiBlock(
        BlockEntityType<?> tileEntityType) {
      return formedMultiBlock(tileEntityType, NbtPredicate.ANY);
    }

    public static MultiBlockFormedTrigger.Instance formedMultiBlock(
        BlockEntityType<?> tileEntityType, NbtPredicate nbtPredicate) {
      return new MultiBlockFormedTrigger.Instance(Composite.ANY, tileEntityType, nbtPredicate);
    }

    public boolean matches(RailcraftBlockEntity blockEntity) {
      return Conditions.check(this.type, blockEntity.getType())
          && this.predicate.matches(blockEntity.saveWithoutMetadata());
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(SerializationContext serializer) {
      JsonObject json = new JsonObject();
      if (this.type != null) {
        json.addProperty("type", ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(this.type).toString());
      }
      json.add("nbt", this.predicate.serializeToJson());
      return json;
    }
  }

}
