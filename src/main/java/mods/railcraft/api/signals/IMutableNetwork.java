package mods.railcraft.api.signals;

import net.minecraft.util.math.BlockPos;

public interface IMutableNetwork extends INetwork {

  void add(BlockPos blockPos);

  void remove(BlockPos blockPos);

  void clear();
}
