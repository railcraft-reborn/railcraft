package mods.railcraft.api.signal;

import mods.railcraft.api.signal.entity.SignalReceiverEntity;
import net.minecraft.core.BlockPos;

public interface SignalController extends SignalNetwork<SignalReceiverEntity> {

  BlockPos blockPos();
}
