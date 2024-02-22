package mods.railcraft.world.level.block.entity.worldspike;

import mods.railcraft.particle.ChunkLoaderParticleOptions;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class WorldSpikeBlockEntity extends RailcraftBlockEntity {

  public WorldSpikeBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.WORLD_SPIKE.get(), blockPos, blockState);
  }

  protected WorldSpikeBlockEntity(BlockEntityType<?> type, BlockPos blockPos,
      BlockState blockState) {
    super(type, blockPos, blockState);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      WorldSpikeBlockEntity blockEntity) {
    spawnParticle((ServerLevel) level, blockPos);
  }

  public static void spawnParticle(ServerLevel level, BlockPos blockPos) {
    var random = level.random;
    var chunkPos = new ChunkPos(blockPos);
    for (int x = chunkPos.x - 1; x <= chunkPos.x + 1; x++) {
      for (int z = chunkPos.z - 1; z <= chunkPos.z + 1; z++) {
        int xCorner = x * 16;
        int zCorner = z * 16;
        double yCorner = blockPos.getY() - 8;

        if (random.nextBoolean()) {
          double xParticle = xCorner + random.nextFloat() * 16;
          double yParticle = yCorner + random.nextFloat() * 16;
          double zParticle = zCorner + random.nextFloat() * 16;

          var dest = new Vec3(blockPos.getX() + 0.1, blockPos.getY(), blockPos.getZ() + 0.1);
          level.sendParticles(new ChunkLoaderParticleOptions(dest),
              xParticle, yParticle, zParticle, 1, 0, 0, 0, 0);
        }
      }
    }
  }
}
