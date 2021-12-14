package mods.railcraft.advancements.criterion;

import com.google.gson.JsonObject;
import mods.railcraft.Railcraft;
import mods.railcraft.util.JsonTools;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class UseTrackKitTrigger extends SimpleCriterionTrigger<UseTrackKitTrigger.Instance> {

  private static final ResourceLocation ID = new ResourceLocation(Railcraft.ID, "use_track_kit");

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public UseTrackKitTrigger.Instance createInstance(JsonObject json,
      EntityPredicate.Composite entityPredicate, DeserializationContext parser) {
    NbtPredicate nbt =
        JsonTools.whenPresent(json, "blockEntityNbt", NbtPredicate::fromJson, NbtPredicate.ANY);
    ItemPredicate used =
        JsonTools.whenPresent(json, "item", ItemPredicate::fromJson, ItemPredicate.ANY);
    LocationPredicate location = JsonTools.whenPresent(json,
        "location", LocationPredicate::fromJson, LocationPredicate.ANY);
    return new UseTrackKitTrigger.Instance(entityPredicate, nbt, used, location);
  }

  /**
   * Invoked when the user explodes a cart.
   */
  public void trigger(ServerPlayer playerEntity, ServerLevel world,
      BlockPos blockPos, ItemStack stack) {
    this.trigger(playerEntity,
        (criterionInstance) -> criterionInstance.matches(playerEntity.getLevel(), blockPos, stack));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final NbtPredicate blockEntityNbt;
    private final ItemPredicate item;
    private final LocationPredicate location;

    private Instance(EntityPredicate.Composite entityPredicate, NbtPredicate nbtPredicate,
        ItemPredicate itemPredicate, LocationPredicate locationPredicate) {
      super(UseTrackKitTrigger.ID, entityPredicate);
      this.blockEntityNbt = nbtPredicate;
      this.item = itemPredicate;
      this.location = locationPredicate;
    }

    public static UseTrackKitTrigger.Instance hasUsedTrackKit() {
      return new UseTrackKitTrigger.Instance(EntityPredicate.Composite.ANY, NbtPredicate.ANY,
          ItemPredicate.ANY, LocationPredicate.ANY);
    }

    public boolean matches(ServerLevel level, BlockPos blockPos, ItemStack stack) {
      return item.matches(stack)
          && this.location.matches(level, blockPos.getX(), blockPos.getY(), blockPos.getZ())
          && LevelUtil.getBlockEntity(level, blockPos)
              .map(te -> te.save(new CompoundTag()))
              .map(blockEntityNbt::matches)
              .orElse(true); // some rails dont have TE
    }

    @Override
    public ResourceLocation getCriterion() {
      return ID;
    }

    @Override
    public JsonObject serializeToJson(SerializationContext serializer) {
      JsonObject json = new JsonObject();
      json.add("blockEntityNbt", this.blockEntityNbt.serializeToJson());
      json.add("item", this.item.serializeToJson());
      json.add("location", this.location.serializeToJson());
      return json;
    }
  }
}
