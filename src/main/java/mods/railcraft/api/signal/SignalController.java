package mods.railcraft.api.signal;

import net.minecraft.core.BlockPos;

public interface SignalController extends SignalNetwork<SignalReceiverProvider> {

  BlockPos getBlockPos();
}
