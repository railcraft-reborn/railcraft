package mods.railcraft.advancements;

import java.util.Optional;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import mods.railcraft.util.LevelUtil;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SpikeMaulUseTrigger extends SimpleCriterionTrigger<SpikeMaulUseTrigger.Instance> {

  @Override
  public SpikeMaulUseTrigger.Instance createInstance(JsonObject json,
      Optional<ContextAwarePredicate> contextAwarePredicate,
      DeserializationContext deserializationContext) {
    var nbt = NbtPredicate.CODEC.decode(JsonOps.INSTANCE, json.get("nbt")).result()
        .map(Pair::getFirst);
    var tool = ItemPredicate.fromJson(json.get("tool"));
    var location = LocationPredicate.fromJson(json.get("location"));
    return new SpikeMaulUseTrigger.Instance(contextAwarePredicate, nbt, tool, location);
  }

  /**
   * Invoked when the user successfully uses a spike maul.
   */
  public void trigger(ServerPlayer player, ItemStack item,
      ServerLevel serverLevel, BlockPos pos) {
    this.trigger(player, (criterionInstance) -> criterionInstance.matches(item, serverLevel, pos));
  }

  public static Criterion<Instance> hasUsedSpikeMaul() {
    return RailcraftCriteriaTriggers.SPIKE_MAUL_USE.createCriterion(
        new Instance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
  }

  public static class Instance extends AbstractCriterionTriggerInstance {

    private final Optional<NbtPredicate> nbt;
    private final Optional<ItemPredicate> tool;
    private final Optional<LocationPredicate> location;

    private Instance(Optional<ContextAwarePredicate> contextAwarePredicate,
        Optional<NbtPredicate> nbt, Optional<ItemPredicate> tool,
        Optional<LocationPredicate> location) {
      super(contextAwarePredicate);
      this.nbt = nbt;
      this.tool = tool;
      this.location = location;
    }

    public boolean matches(ItemStack item, ServerLevel level, BlockPos pos) {
      return LevelUtil.getBlockEntity(level, pos)
          .map(BlockEntity::saveWithoutMetadata)
          .map(tag -> this.nbt.map(x -> x.matches(tag)).orElse(false))
          .orElse(false)
          && this.tool.map(x -> x.matches(item)).orElse(false)
          && this.location.map(x -> x.matches(level, pos.getX(), pos.getY(), pos.getZ())).orElse(false);
    }

    @Override
    public JsonObject serializeToJson() {
      var json = super.serializeToJson();
      this.tool.ifPresent(x -> json.add("tool", x.serializeToJson()));
      this.location.ifPresent(x -> json.add("location", x.serializeToJson()));
      this.nbt.ifPresent(x -> {
        var encode = NbtPredicate.CODEC
            .encodeStart(JsonOps.INSTANCE, x).result();
        json.add("nbt", encode.get());
      });
      return json;
    }
  }
}
