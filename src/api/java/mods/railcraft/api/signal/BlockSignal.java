package mods.railcraft.api.signal;

import net.minecraft.core.BlockPos;

public interface BlockSignal extends SignalNetwork<BlockSignalEntity> {

  TrackLocator trackLocator();

  BlockPos blockPos();

  SignalAspect aspectExcluding(BlockPos peerPos);
}
