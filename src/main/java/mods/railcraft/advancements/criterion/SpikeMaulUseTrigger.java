package mods.railcraft.advancements.criterion;

import com.google.gson.JsonObject;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.plugins.WorldPlugin;
import mods.railcraft.util.JsonTools;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.NBTPredicate;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

final class SpikeMaulUseTrigger extends BaseTrigger<SpikeMaulUseTrigger.Instance> {

  static final ResourceLocation ID = RailcraftConstantsAPI.locationOf("spike_maul_use");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public Instance createInstance(JsonObject json, ConditionArrayParser parser) {
    NBTPredicate nbt =
        JsonTools.whenPresent(json, "nbt", NBTPredicate::fromJson, NBTPredicate.ANY);
    ItemPredicate tool =
        JsonTools.whenPresent(json, "tool", ItemPredicate::fromJson, ItemPredicate.ANY);
    LocationPredicate locationPredicate = JsonTools.whenPresent(json, "location",
        LocationPredicate::fromJson, LocationPredicate.ANY);
    return new Instance(nbt, tool, locationPredicate);
  }

  static final class Instance implements ICriterionInstance {

    final NBTPredicate nbt;
    final ItemPredicate tool;
    final LocationPredicate location;

    Instance(NBTPredicate nbt, ItemPredicate tool, LocationPredicate predicate) {
      this.nbt = nbt;
      this.tool = tool;
      this.location = predicate;
    }

    boolean matches(ItemStack item, ServerWorld world, BlockPos pos) {
      return WorldPlugin.getTileEntity(world, pos)
          .map(te -> te.save(new CompoundNBT()))
          .map(nbt::matches)
          .orElse(false)
          && tool.matches(item)
          && location.matches(world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(ConditionArraySerializer p_230240_1_) {
      JsonObject json = new JsonObject();
      json.add("nbt", this.nbt.serializeToJson());
      json.add("tool", this.tool.serializeToJson());
      json.add("location", this.location.serializeToJson());
      return json;
    }
  }
}
