package mods.railcraft.data.gametest.instances.multiblock;

import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.CokeOvenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

/**
 * Base class for the tests checking that coke oven members never keep a master which was broken or
 * unloaded, and that the structure picks its blocks up again once they're loaded.
 *
 * <p>
 * Chunk unloading is simulated the way {@code LevelChunk#clearAllBlockEntities} does it (block
 * entity and ticker dropped from the chunk), and reloading the way a chunk adds its saved block
 * entities back. While a block entity is "unloaded" the tests must not look it up through the
 * level, which would create a fresh one in its place.
 */
public abstract class CokeOvenTestInstance extends GameTestInstance {

  protected static final int COAL = 8;

  protected CokeOvenTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  protected static void buildCokeOven(GameTestHelper helper) {
    for (var pos : bricks()) {
      helper.setBlock(pos, RailcraftBlocks.COKE_OVEN_BRICKS.get());
    }
  }

  protected static List<BlockPos> bricks() {
    List<BlockPos> bricks = new ArrayList<>();
    for (int x = 1; x <= 3; x++) {
      for (int y = 1; y <= 3; y++) {
        for (int z = 1; z <= 3; z++) {
          if (x != 2 || y != 2 || z != 2) {
            bricks.add(new BlockPos(x, y, z));
          }
        }
      }
    }
    return bricks;
  }

  protected static List<BlockPos> members(BlockPos masterPos) {
    return bricks().stream().filter(pos -> !pos.equals(masterPos)).toList();
  }

  protected static CokeOvenBlockEntity brick(GameTestHelper helper, BlockPos pos) {
    return helper.getBlockEntity(pos, CokeOvenBlockEntity.class);
  }

  protected static CokeOvenBlockEntity master(GameTestHelper helper) {
    return brick(helper, new BlockPos(1, 1, 1)).getMasterBlockEntity()
        .orElseThrow(() -> helper.assertionException(
            Component.literal("The coke oven should be formed")));
  }

  protected static BlockPos relativePos(GameTestHelper helper, BlockPos absolutePos) {
    return bricks().stream()
        .filter(pos -> helper.absolutePos(pos).equals(absolutePos))
        .findFirst()
        .orElseThrow(() -> helper.assertionException(
            Component.literal(absolutePos + " is not a brick")));
  }

  protected static @Nullable ResourceHandler<ItemResource> itemHandler(GameTestHelper helper,
      BlockPos pos) {
    return helper.getLevel()
        .getCapability(Capabilities.Item.BLOCK, helper.absolutePos(pos), null);
  }

  protected static void insertCoal(GameTestHelper helper, BlockPos pos) {
    var handler = itemHandler(helper, pos);
    helper.assertTrue(handler != null, "The coke oven should expose its inventory");
    try (var tx = Transaction.openRoot()) {
      var inserted = handler.insert(ItemResource.of(Items.COAL), COAL, tx);
      helper.assertTrue(inserted == COAL, "The coke oven should accept coal");
      tx.commit();
    }
  }

  protected static long countItems(ResourceHandler<ItemResource> handler) {
    long count = 0;
    for (int i = 0; i < handler.size(); i++) {
      count += handler.getAmountAsLong(i);
    }
    return count;
  }

  protected static CompoundTag unload(GameTestHelper helper, BlockEntity blockEntity) {
    var level = helper.getLevel();
    var tag = blockEntity.saveWithFullMetadata(level.registryAccess());
    blockEntity.onChunkUnloaded();
    // Removes the block entity and its ticker from the chunk, then calls setRemoved
    level.getChunkAt(blockEntity.getBlockPos()).removeBlockEntity(blockEntity.getBlockPos());
    return tag;
  }

  protected static BlockEntity reload(GameTestHelper helper, BlockPos pos, CompoundTag tag) {
    var level = helper.getLevel();
    var absolutePos = helper.absolutePos(pos);
    var blockEntity = BlockEntity.loadStatic(absolutePos, level.getBlockState(absolutePos), tag,
        level.registryAccess());
    if (blockEntity == null) {
      throw helper.assertionException(
          Component.literal("Failed to reload the block entity at " + pos));
    }
    level.setBlockEntity(blockEntity);
    return blockEntity;
  }
}
