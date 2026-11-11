package mods.railcraft.world.level.levelgen.placement;

import com.mojang.serialization.MapCodec;
import mods.railcraft.RailcraftConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;

public class OreConfigFilter implements PlacementFilter {

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
  public boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
    return RailcraftConfig.isOreEnabled(this.ore);
  }

  @Override
  public MapCodec<OreConfigFilter> codec() {
    return CODEC;
  }
}
