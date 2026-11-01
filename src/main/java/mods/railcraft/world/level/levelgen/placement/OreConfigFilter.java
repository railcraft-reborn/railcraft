package mods.railcraft.world.level.levelgen.placement;

import com.mojang.serialization.MapCodec;
import mods.railcraft.RailcraftConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

/**
 * Skips a placed feature when its ore has been turned off in the server config. Only chunks
 * generated after the change are affected.
 */
public class OreConfigFilter extends PlacementFilter {

  public static final MapCodec<OreConfigFilter> CODEC = WorldgenOre.CODEC
      .fieldOf("ore")
      .xmap(OreConfigFilter::new, filter -> filter.ore);

  private final WorldgenOre ore;

  private OreConfigFilter(WorldgenOre ore) {
    this.ore = ore;
  }

  public static OreConfigFilter of(WorldgenOre ore) {
    return new OreConfigFilter(ore);
  }

  @Override
  protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
    return RailcraftConfig.isOreEnabled(this.ore);
  }

  @Override
  public PlacementModifierType<?> type() {
    return RailcraftPlacementModifierTypes.ORE_CONFIG.get();
  }
}
