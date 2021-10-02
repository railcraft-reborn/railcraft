package mods.railcraft.api.signal;

import net.minecraft.util.math.BlockPos;

public interface BlockSignalNetwork extends SignalNetwork<BlockSignal> {

  TrackLocator getTrackLocator();

  BlockPos getBlockPos();

  SignalAspect getSignalAspectExcluding(BlockPos peerPos);
}
