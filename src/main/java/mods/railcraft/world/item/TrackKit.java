package mods.railcraft.world.item;

import com.google.common.collect.ImmutableMap;
import mods.railcraft.api.track.TrackType;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class TrackKit {

  private final Map<TrackType, Supplier<? extends BaseRailBlock>> outfittedTrackBlocks;
  private final boolean supportsSlopes;

  private TrackKit(
      Map<TrackType, Supplier<? extends BaseRailBlock>> outfittedTrackBlocks,
      boolean supportsSlopes) {
    this.outfittedTrackBlocks = outfittedTrackBlocks;
    this.supportsSlopes = supportsSlopes;
  }

  public boolean supportsSlopes() {
    return this.supportsSlopes;
  }

  public Optional<? extends BaseRailBlock> getOutfittedBlock(TrackType trackType) {
    return Optional.ofNullable(this.outfittedTrackBlocks.get(trackType))
        .map(Supplier::get);
  }

  public static class Builder {

    private final ImmutableMap.Builder<TrackType, Supplier<? extends BaseRailBlock>> outfittedBlocks =
        ImmutableMap.builder();
    private boolean supportsSlopes;

    public Builder addOutfittedBlock(
        RegistryObject<? extends TrackType> trackType,
        Supplier<? extends BaseRailBlock> block) {
      return this.addOutfittedBlock(trackType, block);
    }

    public Builder addOutfittedBlock(TrackType trackType,
        Supplier<? extends BaseRailBlock> block) {
      this.outfittedBlocks.put(trackType, block);
      return this;
    }

    public Builder setSupportsSlopes(boolean supportsSlopes) {
      this.supportsSlopes = supportsSlopes;
      return this;
    }

    public TrackKit build() {
      return new TrackKit(this.outfittedBlocks.build(), this.supportsSlopes);
    }
  }
}


