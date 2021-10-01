package mods.railcraft.api.signal;

import net.minecraft.util.math.BlockPos;

public interface SignalController extends SignalNetwork<SignalReceiverProvider> {

  BlockPos getBlockPos();
}
