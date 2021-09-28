package mods.railcraft.api.signals;

import java.util.Collection;
import net.minecraft.util.math.BlockPos;

public interface ClientNetwork<T extends BlockEntityLike> extends Network<T> {

  void setClientPeers(Collection<BlockPos> peers);
}
