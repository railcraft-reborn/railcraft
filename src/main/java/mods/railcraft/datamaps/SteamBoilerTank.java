package mods.railcraft.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record SteamBoilerTank(float steamMultiplier) {

  public static final Codec<SteamBoilerTank> STEAM_MULTIPLIER_CODEC =
      ExtraCodecs.POSITIVE_FLOAT.xmap(SteamBoilerTank::new, SteamBoilerTank::steamMultiplier);

  public static final Codec<SteamBoilerTank> CODEC =
      Codec.withAlternative(
          RecordCodecBuilder.create(instance -> instance
              .group(
                  ExtraCodecs.POSITIVE_FLOAT
                      .fieldOf("steam_multiplier")
                      .forGetter(SteamBoilerTank::steamMultiplier))
              .apply(instance, SteamBoilerTank::new)),
          STEAM_MULTIPLIER_CODEC);
}
