package mods.railcraft.util.datamaps.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record FluidHeat(int heatValuePerBucket) {

  public static final Codec<FluidHeat> HEAT_VALUE_PER_BUCKET_CODEC =
      ExtraCodecs.POSITIVE_INT.xmap(FluidHeat::new, FluidHeat::heatValuePerBucket);

  public static final Codec<FluidHeat> CODEC = ExtraCodecs.withAlternative(
      RecordCodecBuilder.create(instance -> instance.group(
          ExtraCodecs.POSITIVE_INT.fieldOf("heat_value_per_bucket")
              .forGetter(FluidHeat::heatValuePerBucket)
      ).apply(instance, FluidHeat::new)), HEAT_VALUE_PER_BUCKET_CODEC);
}
