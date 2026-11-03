package mods.railcraft;

import java.util.ArrayList;
import java.util.List;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.CokeOvenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * Multiblock members must never keep a master which was broken or unloaded, and a structure must
 * pick its blocks up again once they are loaded.
 *
 * <p>
 * Chunk unloading is simulated the way {@code LevelChunk#clearAllBlockEntities} does it (block
 * entity and ticker dropped from the chunk), and reloading the way a chunk adds its saved block
 * entities back. While a block entity is "unloaded"
 * the tests must not look it up through the level, which would create a fresh one in its place.
 */
@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class MultiblockMembershipTest {

  private static final int COAL = 8;

  /**
   * Breaking the master must release every member, they must not keep exposing the inventory of a
   * removed coke oven.
   */
  @GameTest(template = "platform_7x7", timeoutTicks = 100)
  public static void brokenMasterReleasesMembers(GameTestHelper helper) {
    buildCokeOven(helper);
    BlockPos[] masterPos = new BlockPos[1];
    helper.runAtTickTime(5, () -> {
      var master = master(helper);
      masterPos[0] = relativePos(helper, master.getBlockPos());
      helper.setBlock(masterPos[0], Blocks.AIR);
    });
    helper.runAtTickTime(15, () -> {
      for (var pos : bricks()) {
        if (pos.equals(masterPos[0])) {
          continue;
        }
        CokeOvenBlockEntity member = helper.getBlockEntity(pos);
        helper.assertFalse(member.isFormed(),
            "%s must not be formed after its master was broken".formatted(pos));
        helper.assertTrue(itemHandler(helper, pos) == null,
            "%s must not expose an inventory after its master was broken".formatted(pos));
      }
      helper.succeed();
    });
  }

  /**
   * When the master's chunk unloads the members which stay loaded must stop exposing its
   * inventory right away, items inserted into it would be lost. Once the master is loaded again the
   * structure must re-form around it, with its contents.
   */
  @GameTest(template = "platform_7x7", timeoutTicks = 100)
  public static void unloadedMasterIsReleasedAndReforms(GameTestHelper helper) {
    buildCokeOven(helper);
    BlockPos[] masterPos = new BlockPos[1];
    CompoundTag[] savedMaster = new CompoundTag[1];
    helper.runAtTickTime(5, () -> {
      var master = master(helper);
      masterPos[0] = relativePos(helper, master.getBlockPos());
      insertCoal(helper, masterPos[0]);
      savedMaster[0] = unload(helper, master);

      for (var pos : members(masterPos[0])) {
        helper.assertTrue(itemHandler(helper, pos) == null,
            "%s must not expose the inventory of an unloaded master".formatted(pos));
      }
    });
    CokeOvenBlockEntity[] reloaded = new CokeOvenBlockEntity[1];
    helper.runAtTickTime(10, () -> {
      for (var pos : members(masterPos[0])) {
        helper.assertTrue(itemHandler(helper, pos) == null,
            "%s must not expose the inventory of an unloaded master".formatted(pos));
      }
      reloaded[0] = (CokeOvenBlockEntity) reload(helper, masterPos[0], savedMaster[0]);
    });
    helper.runAtTickTime(20, () -> {
      for (var pos : bricks()) {
        CokeOvenBlockEntity blockEntity = helper.getBlockEntity(pos);
        helper.assertTrue(blockEntity.getMasterBlockEntity().orElse(null) == reloaded[0],
            "%s must belong to the reloaded master".formatted(pos));
      }
      var handler = itemHandler(helper, members(masterPos[0]).getFirst());
      helper.assertTrue(handler != null, "The re-formed coke oven must expose its inventory");
      helper.assertTrue(countItems(handler) == COAL,
          "The coke oven must still hold %s items, holds %s".formatted(COAL, countItems(handler)));
      helper.assertItemEntityNotPresent(Items.COAL);
      helper.succeed();
    });
  }

  /**
   * A member whose chunk unloads must not make the master disband, which would drop its contents,
   * and the member must rejoin the structure once it's loaded again.
   */
  @GameTest(template = "platform_7x7", timeoutTicks = 100)
  public static void unloadedMemberRejoins(GameTestHelper helper) {
    buildCokeOven(helper);
    CokeOvenBlockEntity[] master = new CokeOvenBlockEntity[1];
    BlockPos[] memberPos = new BlockPos[1];
    CompoundTag[] savedMember = new CompoundTag[1];
    helper.runAtTickTime(5, () -> {
      master[0] = master(helper);
      var masterPos = relativePos(helper, master[0].getBlockPos());
      insertCoal(helper, masterPos);
      memberPos[0] = members(masterPos).getFirst();
      savedMember[0] = unload(helper, helper.getBlockEntity(memberPos[0]));
    });
    CokeOvenBlockEntity[] reloaded = new CokeOvenBlockEntity[1];
    helper.runAtTickTime(10, () -> {
      helper.assertTrue(master[0].isFormed(),
          "The master must stay formed while a member is unloaded");
      helper.assertItemEntityNotPresent(Items.COAL);
      reloaded[0] = (CokeOvenBlockEntity) reload(helper, memberPos[0], savedMember[0]);
    });
    helper.runAtTickTime(20, () -> {
      helper.assertTrue(reloaded[0].getMasterBlockEntity().orElse(null) == master[0],
          "The reloaded member must rejoin its master");
      helper.assertTrue(master[0].streamMembers().noneMatch(BlockEntity::isRemoved),
          "The master must not keep removed members");
      var handler = itemHandler(helper, memberPos[0]);
      helper.assertTrue(handler != null, "The reloaded member must expose the inventory");
      helper.assertTrue(countItems(handler) == COAL,
          "The coke oven must still hold %s items, holds %s".formatted(COAL, countItems(handler)));
      helper.assertItemEntityNotPresent(Items.COAL);
      helper.succeed();
    });
  }

  private static void buildCokeOven(GameTestHelper helper) {
    for (var pos : bricks()) {
      helper.setBlock(pos, RailcraftBlocks.COKE_OVEN_BRICKS.get());
    }
  }

  private static List<BlockPos> bricks() {
    List<BlockPos> bricks = new ArrayList<>();
    for (int x = 1; x <= 3; x++) {
      for (int y = 2; y <= 4; y++) {
        for (int z = 1; z <= 3; z++) {
          if (x != 2 || y != 3 || z != 2) {
            bricks.add(new BlockPos(x, y, z));
          }
        }
      }
    }
    return bricks;
  }

  private static List<BlockPos> members(BlockPos masterPos) {
    return bricks().stream().filter(pos -> !pos.equals(masterPos)).toList();
  }

  /**
   * {@link GameTestHelper#relativePos} isn't the inverse of {@link GameTestHelper#absolutePos}.
   */
  private static BlockPos relativePos(GameTestHelper helper, BlockPos absolutePos) {
    return bricks().stream()
        .filter(pos -> helper.absolutePos(pos).equals(absolutePos))
        .findFirst()
        .orElseThrow(() -> new GameTestAssertException(absolutePos + " is not a brick"));
  }

  private static CokeOvenBlockEntity master(GameTestHelper helper) {
    CokeOvenBlockEntity blockEntity = helper.getBlockEntity(new BlockPos(1, 2, 1));
    return blockEntity.getMasterBlockEntity()
        .orElseThrow(() -> new GameTestAssertException("The coke oven should be formed"));
  }

  private static IItemHandler itemHandler(GameTestHelper helper, BlockPos pos) {
    return helper.getLevel()
        .getCapability(Capabilities.ItemHandler.BLOCK, helper.absolutePos(pos), null);
  }

  private static void insertCoal(GameTestHelper helper, BlockPos pos) {
    var handler = itemHandler(helper, pos);
    helper.assertTrue(handler != null, "The coke oven should expose its inventory");
    var remainder = handler.insertItem(0, new ItemStack(Items.COAL, COAL), false);
    helper.assertTrue(remainder.isEmpty(), "The coke oven should accept coal");
  }

  private static int countItems(IItemHandler handler) {
    int count = 0;
    for (int i = 0; i < handler.getSlots(); i++) {
      count += handler.getStackInSlot(i).getCount();
    }
    return count;
  }

  private static CompoundTag unload(GameTestHelper helper, BlockEntity blockEntity) {
    var level = helper.getLevel();
    var tag = blockEntity.saveWithFullMetadata(level.registryAccess());
    blockEntity.onChunkUnloaded();
    // Removes the block entity and its ticker from the chunk, then calls setRemoved
    level.getChunkAt(blockEntity.getBlockPos()).removeBlockEntity(blockEntity.getBlockPos());
    return tag;
  }

  private static BlockEntity reload(GameTestHelper helper, BlockPos pos, CompoundTag tag) {
    var level = helper.getLevel();
    var absolutePos = helper.absolutePos(pos);
    var blockEntity =
        BlockEntity.loadStatic(absolutePos, level.getBlockState(absolutePos), tag,
            level.registryAccess());
    if (blockEntity == null) {
      throw new GameTestAssertException("Failed to reload the block entity at " + pos);
    }
    level.setBlockEntity(blockEntity);
    return blockEntity;
  }
}
