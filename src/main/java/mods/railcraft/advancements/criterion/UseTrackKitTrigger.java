package mods.railcraft.advancements.criterion;

import com.google.gson.JsonObject;

import mods.railcraft.api.core.RailcraftConstantsAPI;
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

public class UseTrackKitTrigger extends AbstractCriterionTrigger<UseTrackKitTrigger.Instance> {

  private static final ResourceLocation ID = RailcraftConstantsAPI.locationOf("use_track_kit");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public UseTrackKitTrigger.Instance createInstance(JsonObject json,
      EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser parser) {
    NBTPredicate nbt =
        JsonTools.whenPresent(json, "blockEntityNbt", NBTPredicate::fromJson, NBTPredicate.ANY);
    ItemPredicate used =
        JsonTools.whenPresent(json, "item", ItemPredicate::fromJson, ItemPredicate.ANY);
    LocationPredicate location = JsonTools.whenPresent(json,
        "location", LocationPredicate::fromJson, LocationPredicate.ANY);
    return new UseTrackKitTrigger.Instance(entityPredicate, nbt, used, location);
  }

  /**
   * Invoked when the user explodes a cart.
   */
  public void trigger(ServerPlayerEntity playerEntity, ServerWorld world,
      BlockPos blockPos, ItemStack stack) {
    this.trigger(playerEntity, (UseTrackKitTrigger.Instance criterionInstance) -> {
      return criterionInstance.matches(playerEntity.getLevel(), blockPos, stack);
    });
  }

  public static class Instance extends CriterionInstance {

    private final NBTPredicate blockEntityNbt;
    private final ItemPredicate item;
    private final LocationPredicate location;

    Instance(EntityPredicate.AndPredicate entityPredicate, NBTPredicate nbtPredicate,
        ItemPredicate itemPredicate, LocationPredicate locationPredicate) {
      super(UseTrackKitTrigger.ID, entityPredicate);
      this.blockEntityNbt = nbtPredicate;
      this.item = itemPredicate;
      this.location = locationPredicate;
    }

    public static UseTrackKitTrigger.Instance hasUsedTrackKit() {
      return new UseTrackKitTrigger.Instance(EntityPredicate.AndPredicate.ANY, NBTPredicate.ANY,
          ItemPredicate.ANY, LocationPredicate.ANY);
    }

    public boolean matches(ServerWorld world, BlockPos blockPos, ItemStack stack) {
      return item.matches(stack)
          && this.location.matches(world, blockPos.getX(), blockPos.getY(), blockPos.getZ())
          && LevelUtil.getBlockEntity(world, blockPos)
              .map(te -> te.save(new CompoundNBT()))
              .map(blockEntityNbt::matches)
              .orElse(true); // some rails dont have TE
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(ConditionArraySerializer serializer) {
      JsonObject json = new JsonObject();
      json.add("blockEntityNbt", this.blockEntityNbt.serializeToJson());
      json.add("item", this.item.serializeToJson());
      json.add("location", this.location.serializeToJson());
      return json;
    }
  }
}
