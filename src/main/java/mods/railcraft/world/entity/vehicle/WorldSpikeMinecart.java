package mods.railcraft.world.entity.vehicle;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import mods.railcraft.Railcraft;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.worldspike.WorldSpikeBlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class WorldSpikeMinecart extends RailcraftMinecart {

  private static final Logger LOGGER = LogUtils.getLogger();
  private ChunkPos lastChunk;
  private final LongOpenHashSet chunkSet = new LongOpenHashSet();

  public WorldSpikeMinecart(EntityType<?> type, Level level) {
    super(type, level);
  }

  public WorldSpikeMinecart(ItemStack itemStack, double x, double y, double z, Level level) {
    super(itemStack, RailcraftEntityTypes.WORLD_SPIKE.get(), x, y, z, level);
  }

  @Override
  public BlockState getDefaultDisplayBlockState() {
    return RailcraftBlocks.WORLD_SPIKE.get().defaultBlockState();
  }

  @Override
  public int getDefaultDisplayOffset() {
    return 6;
  }

  @Override
  public void tick() {
    super.tick();

    if (this.level() instanceof ServerLevel serverLevel) {
      WorldSpikeBlockEntity.spawnParticle(serverLevel, getOnPos());
      if (!this.chunkPosition().equals(this.lastChunk)) {
        var newChunkSet = new LongOpenHashSet();
        for (int x = - 1; x <= 1; x++) {
          for (int z = - 1; z <= 1; z++) {
            // Load chunk with X shape
            if (x * z == 0) {
              var loadChunk = new ChunkPos(this.chunkPosition().x + x, this.chunkPosition().z + z);
              newChunkSet.add(loadChunk.toLong());
              Railcraft.CHUNK_CONTROLLER.forceChunk(serverLevel, this.uuid,
                  loadChunk.x, loadChunk.z, true, false);
            }
          }
        }
        var modified = this.chunkSet.removeAll(newChunkSet);
        if (modified) {
          for (long chunkPos : chunkSet) {
            int x = (int) chunkPos;
            int z = (int) (chunkPos >> 32);
            LOGGER.info("Unload chunk [X: {}, Z: {}]", x, z);
            Railcraft.CHUNK_CONTROLLER.forceChunk(serverLevel, this.uuid, x, z, false, false);
          }
        }
        this.chunkSet.clear();
        this.chunkSet.addAll(newChunkSet);
        this.lastChunk = this.chunkPosition();
        this.setChanged();
      }
    }
  }

  @Override
  public void remove(RemovalReason reason) {
    super.remove(reason);
    if (this.level() instanceof ServerLevel serverLevel) {
      LOGGER.info("Minecart removed");
      for (long chunkPos : this.chunkSet) {
        int x = (int) chunkPos;
        int z = (int) (chunkPos >> 32);
        LOGGER.info("Unload chunk [X: {}, Z: {}]", x, z);
        Railcraft.CHUNK_CONTROLLER.forceChunk(serverLevel, this.uuid, x, z, false, false);
      }
    }
  }

  @Override
  public int getContainerSize() {
    return 0;
  }

  @Override
  protected boolean hasMenu() {
    return false;
  }

  @Override
  protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
    return null;
  }

  @Override
  public Item getDropItem() {
    return RailcraftItems.WORLD_SPIKE_MINECART.get();
  }
}
