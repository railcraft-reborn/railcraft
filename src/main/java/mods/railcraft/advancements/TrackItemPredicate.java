package mods.railcraft.advancements;

import java.util.function.Function;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import com.google.gson.JsonObject;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.util.Conditions;
import mods.railcraft.util.JsonUtil;
import mods.railcraft.world.level.block.track.TrackTypes;
import net.minecraft.world.item.ItemStack;

final class TrackItemPredicate implements Predicate<ItemStack> {

  static final Function<JsonObject, Predicate<ItemStack>> DESERIALIZER = (json) -> {
    var highSpeed = JsonUtil.getAsBoolean(json, "high_speed").orElse(null);
    var electric = JsonUtil.getAsBoolean(json, "electric").orElse(null);
    var type = JsonUtil.getFromRegistry(json, "track_type", TrackTypes.REGISTRY.get())
        .orElse(null);
    return new TrackItemPredicate(highSpeed, electric, type);
  };

  private final @Nullable Boolean highSpeed;
  private final @Nullable Boolean electric;
  private final @Nullable TrackType type;

  private TrackItemPredicate(@Nullable Boolean highSpeed, @Nullable Boolean electric,
      @Nullable TrackType type) {
    this.highSpeed = highSpeed;
    this.electric = electric;
    this.type = type;
  }

  @Override
  public boolean test(ItemStack stack) {
    var type = TrackUtil.getTrackType(stack);
    if (!Conditions.check(this.highSpeed, type.isHighSpeed())) {
      return false;
    }
    if (!Conditions.check(this.electric, type.isElectric())) {
      return false;
    }
    if (!Conditions.check(this.type, type)) {
      return false;
    }
    return TrackUtil.isRail(stack);
  }
}
