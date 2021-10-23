package mods.railcraft.advancements.criterion;

import com.google.gson.JsonObject;
import mods.railcraft.Railcraft;
import mods.railcraft.util.JsonTools;
import mods.railcraft.util.LevelUtil;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.NBTPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class SpikeMaulUseTrigger extends AbstractCriterionTrigger<SpikeMaulUseTrigger.Instance> {

  private static final ResourceLocation ID = new ResourceLocation(Railcraft.ID, "spike_maul_use");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public SpikeMaulUseTrigger.Instance createInstance(JsonObject json,
      EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser parser) {
    NBTPredicate nbt =
        JsonTools.whenPresent(json, "nbt", NBTPredicate::fromJson, NBTPredicate.ANY);
    ItemPredicate tool =
        JsonTools.whenPresent(json, "tool", ItemPredicate::fromJson, ItemPredicate.ANY);
    LocationPredicate locationPredicate = JsonTools.whenPresent(json, "location",
        LocationPredicate::fromJson, LocationPredicate.ANY);
    return new SpikeMaulUseTrigger.Instance(entityPredicate, nbt, tool, locationPredicate);
  }

  /**
   * Invoked when the user successfully uses a spike maul.
   */
  public void trigger(ServerPlayerEntity playerEntity, ItemStack item,
      ServerWorld world, BlockPos pos) {
    this.trigger(playerEntity, (criterionInstance) -> criterionInstance.matches(item, world, pos));
  }

  public static class Instance extends CriterionInstance {

    private final NBTPredicate nbt;
    private final ItemPredicate tool;
    private final LocationPredicate location;

    private Instance(EntityPredicate.AndPredicate entityPredicate, NBTPredicate nbt,
        ItemPredicate tool, LocationPredicate predicate) {
      super(SpikeMaulUseTrigger.ID, entityPredicate);
      this.nbt = nbt;
      this.tool = tool;
      this.location = predicate;
    }

    public static SpikeMaulUseTrigger.Instance hasUsedSpikeMaul() {
      return new SpikeMaulUseTrigger.Instance(EntityPredicate.AndPredicate.ANY,
          NBTPredicate.ANY, ItemPredicate.ANY, LocationPredicate.ANY);
    }

    public boolean matches(ItemStack item, ServerWorld world, BlockPos pos) {
      return LevelUtil
          .getBlockEntity(world, pos)
          .map(blockEntity -> blockEntity.save(new CompoundNBT()))
          .map(this.nbt::matches)
          .orElse(false)
          && this.tool.matches(item)
          && this.location.matches(world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(ConditionArraySerializer serializer) {
      JsonObject json = new JsonObject();
      json.add("nbt", this.nbt.serializeToJson());
      json.add("tool", this.tool.serializeToJson());
      json.add("location", this.location.serializeToJson());
      return json;
    }
  }
}
