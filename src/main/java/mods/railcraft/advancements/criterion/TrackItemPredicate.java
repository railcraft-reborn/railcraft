package mods.railcraft.advancements.criterion;

import java.util.function.Function;
import javax.annotation.Nullable;
import com.google.gson.JsonObject;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.util.Conditions;
import mods.railcraft.util.JsonTools;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.level.block.track.TrackTypes;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.ItemStack;

final class TrackItemPredicate extends ItemPredicate {

  static final Function<JsonObject, ItemPredicate> DESERIALIZER = (json) -> {
    Boolean highSpeed = JsonTools.nullableBoolean(json, "high_speed");
    Boolean electric = JsonTools.nullableBoolean(json, "electric");
    TrackType type = JsonTools.getFromRegistryWhenPresent(json, "track_type",
        TrackTypes.registry.get(), null);
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
  public boolean matches(ItemStack stack) {
    TrackType type = TrackUtil.getTrackType(stack);
    if (!Conditions.check(highSpeed, type.isHighSpeed())) {
      return false;
    }
    if (!Conditions.check(electric, type.isElectric())) {
      return false;
    }
    if (!Conditions.check(this.type, type)) {
      return false;
    }
    return TrackTools.isRail(stack);
  }
}
