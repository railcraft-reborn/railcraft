package mods.railcraft.advancements.criterion;

import com.google.gson.JsonObject;

import javax.annotation.Nullable;

import mods.railcraft.Railcraft;
import mods.railcraft.util.Conditions;
import mods.railcraft.util.JsonTools;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate.AndPredicate;
import net.minecraft.advancements.criterion.NBTPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class MultiBlockFormedTrigger extends
    AbstractCriterionTrigger<MultiBlockFormedTrigger.Instance> {

  private static final ResourceLocation ID =
      new ResourceLocation(Railcraft.ID + ":multiblock_formed");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  protected MultiBlockFormedTrigger.Instance createInstance(JsonObject json,
      AndPredicate andPredicate, ConditionArrayParser parser) {

    TileEntityType<?> type = json.has("type")
        ? ForgeRegistries.TILE_ENTITIES.getValue(
            new ResourceLocation(json.get("type").getAsString()))
        : null;
    NBTPredicate nbt =
        JsonTools.whenPresent(json, "nbt", NBTPredicate::fromJson, NBTPredicate.ANY);

    return new MultiBlockFormedTrigger.Instance(andPredicate, type, nbt);
  }

  /**
   * Invoked when the user forms a multiblock.
   */
  public void trigger(ServerPlayerEntity playerEntity, RailcraftBlockEntity tile) {
    this.trigger(playerEntity, (criterionInstance) -> criterionInstance.matches(tile));
  }

  public static class Instance extends CriterionInstance {

    private final @Nullable TileEntityType<?> type;
    private final NBTPredicate nbt;

    private Instance(AndPredicate entityPredicate,
        @Nullable TileEntityType<?> type, NBTPredicate nbtPredicate) {
      super(MultiBlockFormedTrigger.ID, entityPredicate);
      this.type = type;
      this.nbt = nbtPredicate;
    }

    public static MultiBlockFormedTrigger.Instance formedMultiBlock(
        TileEntityType<?> tileEntityType) {
      return formedMultiBlock(tileEntityType, NBTPredicate.ANY);
    }

    public static MultiBlockFormedTrigger.Instance formedMultiBlock(
        TileEntityType<?> tileEntityType, NBTPredicate nbtPredicate) {
      return new MultiBlockFormedTrigger.Instance(AndPredicate.ANY, tileEntityType, nbtPredicate);
    }

    public boolean matches(RailcraftBlockEntity tile) {
      return Conditions.check(this.type, tile.getType())
          && nbt.matches(tile.save(new CompoundNBT()));
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(ConditionArraySerializer serializer) {
      JsonObject json = new JsonObject();
      if (this.type != null) {
        json.addProperty("type", this.type.getRegistryName().toString());
      }
      json.add("nbt", this.nbt.serializeToJson());
      return json;
    }
  }

}
