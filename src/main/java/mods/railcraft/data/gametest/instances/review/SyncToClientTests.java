package mods.railcraft.data.gametest.instances.review;

import static mods.railcraft.data.gametest.instances.review.ReviewTestUtil.reproduced;
import static mods.railcraft.data.gametest.instances.review.ReviewTestUtil.setupError;

import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.tank.TankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Block entity sync finding: C4, plus a control for the packet spy.
 */
final class SyncToClientTests {

  private SyncToClientTests() {}

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

  /**
   * C4: syncToClient should only reach players tracking the block entity's chunk.
   */
  static void c4SyncToUntrackingPlayer(GameTestHelper helper) {
    buildTank(helper, 3);
    var spy = ReviewTestUtil.spyPlayer(helper);
    helper.startSequence()
        .thenIdle(5)
        .thenExecute(() -> {
          var masterPos = helper.absolutePos(tankMasterPos());
          if (spy.isTracking(masterPos)) {
            spy.remove();
            setupError(helper, "C4", "spy player is tracking the test chunk");
          }
          var master = helper.getBlockEntity(tankMasterPos(), TankBlockEntity.class);
          spy.clear();
          // A real change: pumping fluid into the tank
          master.getModule().getTank().setFluid(new FluidStack(Fluids.WATER, 1000));
          master.syncToClient();
          var received = spy.blockEntityPackets(masterPos);
          spy.remove();
          if (received > 0) {
            reproduced(helper, "C4", "a player "
                + (int) spy.player.position().distanceTo(net.minecraft.world.phys.Vec3.atCenterOf(masterPos))
                + " blocks away that doesn't track the chunk received " + received
                + " block entity packets for the tank");
          }
        })
        .thenSucceed();
  }

  /**
   * Control: vanilla's own block update path only reaches tracking players, so the spy player
   * receives nothing. Shows the packet counts in the other tests are real leaks.
   */
  static void controlSpyIgnoresTrackedUpdates(GameTestHelper helper) {
    var pos = new BlockPos(4, 1, 4);
    var spy = ReviewTestUtil.spyPlayer(helper);
    helper.startSequence()
        .thenExecute(() -> {
          helper.setBlock(pos, Blocks.CHEST);
          var absolutePos = helper.absolutePos(pos);
          var state = helper.getLevel().getBlockState(absolutePos);
          spy.clear();
          helper.getLevel().sendBlockUpdated(absolutePos, state, state, 3);
        })
        .thenIdle(2)
        .thenExecute(() -> {
          spy.remove();
          var absolutePos = helper.absolutePos(pos);
          var blockUpdates = 0L;
          synchronized (spy.packets) {
            blockUpdates = spy.packets.stream()
                .filter(net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket.class
                    ::isInstance)
                .map(net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket.class::cast)
                .filter(p -> p.getPos().equals(absolutePos))
                .count();
          }
          var blockEntityPackets = spy.blockEntityPackets(absolutePos);
          if (spy.isTracking(absolutePos) || blockEntityPackets > 0 || blockUpdates > 0) {
            helper.fail(net.minecraft.network.chat.Component.literal("Spy player received "
                + blockEntityPackets + " block entity and " + blockUpdates
                + " block update packets for the chest from vanilla's tracked update path"));
          }
        })
        .thenSucceed();
  }
}
