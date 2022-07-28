package mods.railcraft.api.signal;

import mods.railcraft.api.core.BlockEntityLike;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public abstract class BlockEntitySignalNetwork<T extends BlockEntityLike>
    extends AbstractSignalNetwork<T> {

  private final BlockEntity blockEntity;

  public BlockEntitySignalNetwork(Class<T> peerType, int maxPeers, Runnable syncListener,
      BlockEntity blockEntity) {
    super(peerType, maxPeers, syncListener);
    this.blockEntity = blockEntity;
  }

  @Override
  public boolean addPeer(T peer) {
    if (super.addPeer(peer)) {
      this.blockEntity.setChanged();
      return true;
    }
    return false;
  }

  @Override
  public boolean removePeer(BlockPos peerPos) {
    if (this.peers.remove(peerPos)) {
      this.blockEntity.setChanged();
      return true;
    }
    return false;
  }

  @Override
  public void refresh() {
    super.refresh();
    this.blockEntity.setChanged();
  }

  public BlockEntity getBlockEntity() {
    return this.blockEntity;
  }

  public BlockPos blockPos() {
    return this.blockEntity.getBlockPos();
  }

  @Override
  public Level getLevel() {
    return this.blockEntity.getLevel();
  }
}
