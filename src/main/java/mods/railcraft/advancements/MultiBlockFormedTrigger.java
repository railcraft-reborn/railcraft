package mods.railcraft.advancements;

import com.google.gson.JsonObject;

import javax.annotation.Nullable;

import mods.railcraft.Railcraft;
import mods.railcraft.util.Conditions;
import mods.railcraft.util.JsonTools;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate.Composite;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

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
      Composite andPredicate, DeserializationContext parser) {

    BlockEntityType<?> type = json.has("type")
        ? ForgeRegistries.BLOCK_ENTITIES.getValue(
            new ResourceLocation(json.get("type").getAsString()))
        : null;
    NbtPredicate nbt =
        JsonTools.whenPresent(json, "nbt", NbtPredicate::fromJson, NbtPredicate.ANY);

    return new MultiBlockFormedTrigger.Instance(andPredicate, type, nbt);
  }

  /**
   * Invoked when the user forms a multiblock.
   */
  public void trigger(ServerPlayer playerEntity, RailcraftBlockEntity tile) {
    this.trigger(playerEntity, (criterionInstance) -> criterionInstance.matches(tile));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final @Nullable BlockEntityType<?> type;
    private final NbtPredicate nbt;

    private Instance(Composite entityPredicate,
        @Nullable BlockEntityType<?> type, NbtPredicate nbtPredicate) {
      super(MultiBlockFormedTrigger.ID, entityPredicate);
      this.type = type;
      this.nbt = nbtPredicate;
    }

    public static MultiBlockFormedTrigger.Instance formedMultiBlock(
        BlockEntityType<?> tileEntityType) {
      return formedMultiBlock(tileEntityType, NbtPredicate.ANY);
    }

    public static MultiBlockFormedTrigger.Instance formedMultiBlock(
        BlockEntityType<?> tileEntityType, NbtPredicate nbtPredicate) {
      return new MultiBlockFormedTrigger.Instance(Composite.ANY, tileEntityType, nbtPredicate);
    }

    public boolean matches(RailcraftBlockEntity tile) {
      return Conditions.check(this.type, tile.getType())
          && nbt.matches(tile.save(new CompoundTag()));
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(SerializationContext serializer) {
      JsonObject json = new JsonObject();
      if (this.type != null) {
        json.addProperty("type", this.type.getRegistryName().toString());
      }
      json.add("nbt", this.nbt.serializeToJson());
      return json;
    }
  }

}
