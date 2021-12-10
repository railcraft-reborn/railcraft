package mods.railcraft.world.item;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import com.google.common.collect.ImmutableMap;
import mods.railcraft.api.track.TrackType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

public class TrackKit extends ForgeRegistryEntry<TrackKit> {

  private final Map<ResourceLocation, Supplier<? extends BaseRailBlock>> outfittedTrackBlocks;
  private final boolean supportsSlopes;

  private TrackKit(
      Map<ResourceLocation, Supplier<? extends BaseRailBlock>> outfittedTrackBlocks,
      boolean supportsSlopes) {
    this.outfittedTrackBlocks = outfittedTrackBlocks;
    this.supportsSlopes = supportsSlopes;
  }

  public boolean supportsSlopes() {
    return this.supportsSlopes;
  }

  public Optional<? extends BaseRailBlock> getOutfittedBlock(TrackType trackType) {
    return Optional.ofNullable(this.outfittedTrackBlocks.get(trackType.getRegistryName()))
        .map(Supplier::get);
  }

  public static class Builder {

    private final ImmutableMap.Builder<ResourceLocation, Supplier<? extends BaseRailBlock>> outfittedBlocks =
        ImmutableMap.builder();
    private boolean supportsSlopes;

    public Builder addOutfittedBlock(
        RegistryObject<? extends TrackType> trackType,
        Supplier<? extends BaseRailBlock> block) {
      return this.addOutfittedBlock(trackType.getId(), block);
    }

    public Builder addOutfittedBlock(ResourceLocation trackTypeId,
        Supplier<? extends BaseRailBlock> block) {
      this.outfittedBlocks.put(trackTypeId, block);
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


