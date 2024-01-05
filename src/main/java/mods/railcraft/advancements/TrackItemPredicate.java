package mods.railcraft.advancements;

import java.util.Optional;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.util.Conditions;
import mods.railcraft.world.level.block.track.TrackTypes;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

public record TrackItemPredicate(
    Optional<Boolean> highSpeed,
    Optional<Boolean> electric,
    Optional<TrackType> type) {

  private static final Codec<TrackType> TRACK_TYPE_CODEC = TrackTypes.REGISTRY.byNameCodec();

  public static final Codec<TrackItemPredicate> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
          ExtraCodecs.strictOptionalField(Codec.BOOL, "highSpeed")
              .forGetter(TrackItemPredicate::highSpeed),
          ExtraCodecs.strictOptionalField(Codec.BOOL, "electric")
              .forGetter(TrackItemPredicate::electric),
          ExtraCodecs.strictOptionalField(TRACK_TYPE_CODEC, "track_type")
              .forGetter(TrackItemPredicate::type))
          .apply(instance, TrackItemPredicate::new));

  public boolean matches(ItemStack stack) {
    var type = TrackUtil.getTrackType(stack);
    if (this.highSpeed.isPresent() && type.isHighSpeed() != this.highSpeed.get()) {
      return false;
    }
    if (this.electric.isPresent() && type.isElectric() != this.electric.get()) {
      return false;
    }
    if (!Conditions.check(this.type, type)) {
      return false;
    }
    return TrackUtil.isRail(stack);
  }
}
