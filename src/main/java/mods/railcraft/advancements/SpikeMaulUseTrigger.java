package mods.railcraft.advancements;

import com.google.gson.JsonObject;
import mods.railcraft.Railcraft;
import mods.railcraft.util.JsonUtil;
import mods.railcraft.util.LevelUtil;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SpikeMaulUseTrigger extends SimpleCriterionTrigger<SpikeMaulUseTrigger.Instance> {

  private static final ResourceLocation ID = new ResourceLocation(Railcraft.ID, "spike_maul_use");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public SpikeMaulUseTrigger.Instance createInstance(JsonObject json,
      EntityPredicate.Composite entityPredicate, DeserializationContext parser) {
    var tag = JsonUtil.getAsJsonObject(json, "nbt")
        .map(NbtPredicate::fromJson)
        .orElse(NbtPredicate.ANY);
    var tool = JsonUtil.getAsJsonObject(json, "tool")
        .map(ItemPredicate::fromJson)
        .orElse(ItemPredicate.ANY);
    var locationPredicate = JsonUtil.getAsJsonObject(json, "location")
        .map(LocationPredicate::fromJson)
        .orElse(LocationPredicate.ANY);
    return new SpikeMaulUseTrigger.Instance(entityPredicate, tag, tool, locationPredicate);
  }

  /**
   * Invoked when the user successfully uses a spike maul.
   */
  public void trigger(ServerPlayer playerEntity, ItemStack item,
      ServerLevel world, BlockPos pos) {
    this.trigger(playerEntity, (criterionInstance) -> criterionInstance.matches(item, world, pos));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final NbtPredicate nbt;
    private final ItemPredicate tool;
    private final LocationPredicate location;

    private Instance(EntityPredicate.Composite entityPredicate, NbtPredicate nbt,
        ItemPredicate tool, LocationPredicate predicate) {
      super(SpikeMaulUseTrigger.ID, entityPredicate);
      this.nbt = nbt;
      this.tool = tool;
      this.location = predicate;
    }

    public static SpikeMaulUseTrigger.Instance hasUsedSpikeMaul() {
      return new SpikeMaulUseTrigger.Instance(EntityPredicate.Composite.ANY,
          NbtPredicate.ANY, ItemPredicate.ANY, LocationPredicate.ANY);
    }

    public boolean matches(ItemStack item, ServerLevel level, BlockPos pos) {
      return LevelUtil.getBlockEntity(level, pos)
          .map(BlockEntity::saveWithoutMetadata)
          .map(this.nbt::matches)
          .orElse(false)
          && this.tool.matches(item)
          && this.location.matches(level, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(SerializationContext serializer) {
      JsonObject json = new JsonObject();
      json.add("nbt", this.nbt.serializeToJson());
      json.add("tool", this.tool.serializeToJson());
      json.add("location", this.location.serializeToJson());
      return json;
    }
  }
}
