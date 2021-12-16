package mods.railcraft.world.level.block.entity.module;

import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ModuleProvider {

  void syncToClient();

  boolean stillValid(Player player);

  BlockPos getBlockPos();

  Level getLevel();

  static ModuleProvider of(RailcraftBlockEntity blockEntity) {
    return new BlockEntityWrapper(blockEntity, blockEntity::syncToClient);
  }

  static ModuleProvider of(BlockEntity blockEntity, Runnable syncListener) {
    return new BlockEntityWrapper(blockEntity, syncListener);
  }

  class BlockEntityWrapper implements ModuleProvider {

    private final BlockEntity blockEntity;
    private final Runnable syncListener;

    private BlockEntityWrapper(BlockEntity blockEntity, Runnable syncListener) {
      this.blockEntity = blockEntity;
      this.syncListener = syncListener;
    }

    @Override
    public void syncToClient() {
      this.syncListener.run();
    }

    @Override
    public boolean stillValid(Player player) {
      return RailcraftBlockEntity.stillValid(this.blockEntity, player);
    }

    @Override
    public BlockPos getBlockPos() {
      return this.blockEntity.getBlockPos();
    }

    @Override
    public Level getLevel() {
      return this.blockEntity.getLevel();
    }
  }
}
