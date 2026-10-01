package mods.railcraft.data.gametest.instances.review;

import static mods.railcraft.data.gametest.instances.review.ReviewTestUtil.getField;
import static mods.railcraft.data.gametest.instances.review.ReviewTestUtil.reproduced;
import static mods.railcraft.data.gametest.instances.review.ReviewTestUtil.setField;
import static mods.railcraft.data.gametest.instances.review.ReviewTestUtil.setupError;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.CokeOvenBlockEntity;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockBlockEntity;
import mods.railcraft.world.level.block.entity.tank.TankBlockEntity;
import mods.railcraft.world.level.gameevent.RailcraftGameEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.VanillaGameEvent;

/**
 * Multiblock evaluation findings: C1, C2, H1, L1.
 */
final class MultiblockEvaluationTests {

  private MultiblockEvaluationTests() {}

  static final BlockPos ORIGIN = new BlockPos(1, 1, 1);

  private static final int TANK_HEIGHT = 4;

  static Block tankWall() {
    return RailcraftBlocks.IRON_TANK_WALL.variantFor(DyeColor.WHITE).get();
  }

  static void buildTank(GameTestHelper helper, int size) {
    for (var y = 0; y < TANK_HEIGHT; y++) {
      for (var x = 0; x < size; x++) {
        for (var z = 0; z < size; z++) {
          var wall = y == 0 || y == TANK_HEIGHT - 1
              || x == 0 || x == size - 1
              || z == 0 || z == size - 1;
          if (wall) {
            helper.setBlock(ORIGIN.offset(x, y, z), tankWall());
          }
        }
      }
    }
  }

  static BlockPos tankMasterPos() {
    return ORIGIN.offset(1, 0, 1);
  }

  static void buildCokeOven(GameTestHelper helper) {
    for (var y = 0; y < 3; y++) {
      for (var x = 0; x < 3; x++) {
        for (var z = 0; z < 3; z++) {
          if (x == 1 && y == 1 && z == 1) {
            continue;
          }
          helper.setBlock(ORIGIN.offset(x, y, z), RailcraftBlocks.COKE_OVEN_BRICKS.get());
        }
      }
    }
  }

  static List<CokeOvenBlockEntity> cokeOvenBricks(GameTestHelper helper) {
    var result = new ArrayList<CokeOvenBlockEntity>();
    for (var y = 0; y < 3; y++) {
      for (var x = 0; x < 3; x++) {
        for (var z = 0; z < 3; z++) {
          if (helper.getLevel().getBlockEntity(helper.absolutePos(ORIGIN.offset(x, y, z)))
              instanceof CokeOvenBlockEntity be) {
            result.add(be);
          }
        }
      }
    }
    return result;
  }

  static List<MultiblockBlockEntity<?, ?>> multiblocksInTest(GameTestHelper helper) {
    var result = new ArrayList<MultiblockBlockEntity<?, ?>>();
    helper.forEveryBlockInStructure(pos -> {
      if (helper.getLevel().getBlockEntity(helper.absolutePos(pos))
          instanceof MultiblockBlockEntity<?, ?> be) {
        result.add(be);
      }
    });
    return result;
  }

  /**
   * Replaces the pattern collection of a multiblock block entity with one that counts how often
   * the patterns are streamed, which happens exactly once per pattern resolution.
   */
  static void countResolutions(MultiblockBlockEntity<?, ?> blockEntity, AtomicInteger counter) {
    Collection<?> patterns = getField(blockEntity, "patterns");
    setField(blockEntity, "patterns", new CountingCollection<>(patterns, counter));
  }

  private static final class CountingCollection<E> extends AbstractCollection<E> {

    private final Collection<E> delegate;
    private final AtomicInteger counter;

    CountingCollection(Collection<E> delegate, AtomicInteger counter) {
      this.delegate = delegate;
      this.counter = counter;
    }

    @Override
    public Stream<E> stream() {
      this.counter.incrementAndGet();
      return this.delegate.stream();
    }

    @Override
    public Iterator<E> iterator() {
      return this.delegate.iterator();
    }

    @Override
    public int size() {
      return this.delegate.size();
    }
  }

  /**
   * C1: a single block change next to a formed tank should cause at most one pattern resolution.
   */
  static void c1MemberEvaluationStorm(GameTestHelper helper) {
    buildTank(helper, 3);
    var counter = new AtomicInteger();
    var members = new int[1];
    helper.startSequence()
        .thenIdle(5)
        .thenExecute(() -> {
          var master = helper.getBlockEntity(tankMasterPos(), TankBlockEntity.class);
          if (!master.isFormed()) {
            setupError(helper, "C1", "tank did not form");
          }
          var blockEntities = multiblocksInTest(helper);
          members[0] = blockEntities.size();
          blockEntities.forEach(be -> countResolutions(be, counter));
          // Two blocks away from the tank, well inside the listener radius.
          helper.setBlock(new BlockPos(6, 2, 2), Blocks.STONE);
        })
        .thenIdle(3)
        .thenExecute(() -> {
          if (counter.get() > 1) {
            reproduced(helper, "C1", "one block change next to a formed 3x3x4 tank with "
                + members[0] + " block entities caused " + counter.get()
                + " full pattern resolutions (expected at most 1)");
          }
        })
        .thenSucceed();
  }

  /**
   * C2: a block update far from any Railcraft block should not turn into a Railcraft game event.
   */
  static void c2GlobalNeighborNotify(GameTestHelper helper) {
    var counter = new AtomicInteger();
    var target = helper.absolutePos(new BlockPos(4, 1, 4));
    Consumer<VanillaGameEvent> listener = event -> {
      if (event.getVanillaEvent().is(RailcraftGameEvents.NEIGHBOR_NOTIFY.getKey())
          && BlockPos.containing(event.getEventPosition()).equals(target)) {
        counter.incrementAndGet();
      }
    };
    NeoForge.EVENT_BUS.addListener(listener);
    helper.startSequence()
        .thenExecute(() -> {
          // Nothing but vanilla blocks in this test's structure
          helper.forEveryBlockInStructure(pos -> {
            if (helper.getLevel().getBlockEntity(helper.absolutePos(pos))
                instanceof MultiblockBlockEntity<?, ?>) {
              setupError(helper, "C2", "structure contains a Railcraft block");
            }
          });
          helper.setBlock(new BlockPos(4, 1, 4), Blocks.STONE);
          helper.setBlock(new BlockPos(4, 1, 4), Blocks.AIR);
        })
        .thenIdle(1)
        .thenExecute(() -> {
          NeoForge.EVENT_BUS.unregister(listener);
          if (counter.get() > 0) {
            reproduced(helper, "C2", "placing and removing a stone block with no Railcraft block "
                + "nearby posted " + counter.get() + " railcraft:neighbor_notify game events");
          }
        })
        .thenSucceed();
  }

  /**
   * Simulates the master's chunk unloading, the same way {@code LevelChunk#clearAllBlockEntities}
   * does it for every block entity in an unloading chunk.
   */
  static void unloadBlockEntity(GameTestHelper helper, BlockEntity blockEntity) {
    blockEntity.onChunkUnloaded();
    helper.getLevel().getChunkAt(blockEntity.getBlockPos())
        .removeBlockEntity(blockEntity.getBlockPos());
  }

  static CokeOvenBlockEntity cokeOvenMaster(GameTestHelper helper) {
    return cokeOvenBricks(helper).stream()
        .filter(MultiblockBlockEntity::isMaster)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Coke oven has no master"));
  }

  /**
   * H1 (a): once the master is unloaded, members must not keep pointing at it.
   */
  static void h1MemberKeepsUnloadedMaster(GameTestHelper helper) {
    buildCokeOven(helper);
    helper.startSequence()
        .thenIdle(5)
        .thenExecute(() -> {
          var master = cokeOvenMaster(helper);
          unloadBlockEntity(helper, master);
          var stale = cokeOvenBricks(helper).stream()
              .filter(be -> be.getMasterBlockEntity().map(BlockEntity::isRemoved).orElse(false))
              .toList();
          var withCap = stale.stream().filter(be -> be.getItemCap(null) != null).count();
          if (!stale.isEmpty()) {
            reproduced(helper, "H1", stale.size() + " loaded coke oven bricks still point at the "
                + "unloaded master and " + withCap + " of them still expose its inventory as "
                + "their item capability", master.getBlockPos());
          }
        })
        .thenSucceed();
  }

  /**
   * H1 (b): a master which is reloaded after its chunk was unloaded should re-form the structure.
   */
  static void h1ReloadedMasterReforms(GameTestHelper helper) {
    buildCokeOven(helper);
    var reloaded = new BlockEntity[1];
    helper.startSequence()
        .thenIdle(5)
        .thenExecute(() -> {
          var master = cokeOvenMaster(helper);
          var level = helper.getLevel();
          var pos = master.getBlockPos();
          var tag = master.saveWithFullMetadata(level.registryAccess());
          unloadBlockEntity(helper, master);
          // Chunk comes back: a fresh block entity loaded from the saved data
          var fresh = BlockEntity.loadStatic(pos, level.getBlockState(pos), tag,
              level.registryAccess());
          level.getChunkAt(pos).addAndRegisterBlockEntity(fresh);
          reloaded[0] = fresh;
        })
        // Retry interval is 40 ticks, give it a few attempts
        .thenIdle(130)
        .thenExecute(() -> {
          var fresh = (CokeOvenBlockEntity) reloaded[0];
          if (!fresh.isFormed()) {
            var stale = cokeOvenBricks(helper).stream()
                .filter(be -> be.getMasterBlockEntity().map(BlockEntity::isRemoved).orElse(false))
                .count();
            reproduced(helper, "H1", "reloaded coke oven master did not re-form within 130 ticks; "
                + stale + " members still hold a membership of the unloaded master",
                fresh.getBlockPos());
          }
        })
        .thenSucceed();
  }

  /**
   * L1: a tank which stays broken should stop retrying eventually, and a formed tank shouldn't
   * register a ticker for every wall block.
   */
  static void l1BrokenTankRetries(GameTestHelper helper) {
    buildTank(helper, 3);
    var counter = new AtomicInteger();
    var tickers = new long[1];
    var members = new int[1];
    helper.startSequence()
        .thenIdle(5)
        .thenExecute(() -> {
          var master = helper.getBlockEntity(tankMasterPos(), TankBlockEntity.class);
          if (!master.isFormed()) {
            setupError(helper, "L1", "tank did not form");
          }
          List<?> levelTickers = getField(helper.getLevel(), "blockEntityTickers");
          var positions = new java.util.HashSet<BlockPos>();
          multiblocksInTest(helper).forEach(be -> positions.add(be.getBlockPos()));
          members[0] = positions.size();
          tickers[0] = levelTickers.stream()
              .map(t -> ((net.minecraft.world.level.block.entity.TickingBlockEntity) t).getPos())
              .filter(positions::contains)
              .count();
          helper.setBlock(ORIGIN.offset(0, 1, 1), Blocks.AIR);
        })
        .thenIdle(10)
        .thenExecute(() -> countResolutions(
            helper.getBlockEntity(tankMasterPos(), TankBlockEntity.class), counter))
        .thenIdle(300)
        .thenExecute(() -> {
          if (counter.get() > 3 || tickers[0] > 1) {
            reproduced(helper, "L1", "a formed 3x3x4 tank with no valves registered " + tickers[0]
                + " block entity tickers for its " + members[0] + " blocks; once broken, its "
                + "master re-resolved its patterns " + counter.get() + " times in 300 ticks");
          }
        })
        .thenSucceed();
  }
}
