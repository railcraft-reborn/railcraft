package mods.railcraft.advancements.criterion;

import com.google.gson.JsonObject;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.util.JsonTools;
import mods.railcraft.util.LevelUtil;
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

final class UseTrackKitTrigger extends BaseTrigger<UseTrackKitTrigger.Instance> {

  static final ResourceLocation ID = RailcraftConstantsAPI.locationOf("use_track_kit");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public Instance createInstance(JsonObject json, ConditionArrayParser parser) {
    NBTPredicate nbt =
        JsonTools.whenPresent(json, "blockEntityNbt", NBTPredicate::fromJson, NBTPredicate.ANY);
    ItemPredicate used =
        JsonTools.whenPresent(json, "item", ItemPredicate::fromJson, ItemPredicate.ANY);
    LocationPredicate location = JsonTools.whenPresent(json,
        "location", LocationPredicate::fromJson, LocationPredicate.ANY);
    return new Instance(nbt, used, location);
  }

  static final class Instance implements ICriterionInstance {

    final NBTPredicate blockEntityNbt;
    final ItemPredicate item;
    final LocationPredicate location;

    Instance(NBTPredicate nbtPredicate, ItemPredicate itemPredicate,
        LocationPredicate locationPredicate) {
      this.blockEntityNbt = nbtPredicate;
      this.item = itemPredicate;
      this.location = locationPredicate;
    }

    boolean matches(ServerWorld world, BlockPos blockPos, ItemStack stack) {
      return item.matches(stack)
          && this.location.matches(world, blockPos.getX(), blockPos.getY(), blockPos.getZ())
          && LevelUtil.getBlockEntity(world, blockPos)
              .map(te -> te.save(new CompoundNBT()))
              .map(blockEntityNbt::matches)
              .orElse(false);
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(ConditionArraySerializer p_230240_1_) {
      JsonObject json = new JsonObject();
      json.add("blockEntityNbt", this.blockEntityNbt.serializeToJson());
      json.add("item", this.item.serializeToJson());
      json.add("location", this.location.serializeToJson());
      return json;
    }
  }
}
