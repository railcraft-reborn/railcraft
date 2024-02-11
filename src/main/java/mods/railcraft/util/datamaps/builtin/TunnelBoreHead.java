package mods.railcraft.util.datamaps.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record TunnelBoreHead(float digModifier) {

  public static final Codec<TunnelBoreHead> CODEC = RecordCodecBuilder
      .create(instance -> instance.group(
          ExtraCodecs.POSITIVE_FLOAT.fieldOf("digModifier").forGetter(TunnelBoreHead::digModifier)
      ).apply(instance, TunnelBoreHead::new));

  public static final Codec<TunnelBoreHead> DIGMODIFIER_CODEC =
      ExtraCodecs.POSITIVE_FLOAT.xmap(TunnelBoreHead::new, TunnelBoreHead::digModifier);
}
