package mods.railcraft.api.signal;

import mods.railcraft.api.core.BlockEntityLike;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockEntitySignalNetwork<T extends BlockEntityLike>
    extends AbstractSignalNetwork<T> {

  private final TileEntity blockEntity;

  public BlockEntitySignalNetwork(Class<T> peerType, int maxPeers, Runnable syncListener,
      TileEntity blockEntity) {
    super(peerType, maxPeers, syncListener);
    this.blockEntity = blockEntity;
  }

  public TileEntity getBlockEntity() {
    return this.blockEntity;
  }

  public BlockPos getBlockPos() {
    return this.blockEntity.getBlockPos();
  }

  @Override
  public World getLevel() {
    return this.blockEntity.getLevel();
  }
}
