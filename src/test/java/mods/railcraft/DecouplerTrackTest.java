package mods.railcraft;

import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.detector.DetectorBlock;
import mods.railcraft.world.level.block.entity.track.CouplerTrackBlockEntity;
import mods.railcraft.world.level.block.track.outfitted.CouplerTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Issue #185, a decoupler unlinks every cart passing over it while powered, so a locomotive
 * detector must turn it off before the cart next to the locomotive gets there. Placed on the side
 * of the tank carts, it does so whether the locomotive pulls or pushes them.
 */
@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class DecouplerTrackTest {

  private static final BlockPos DECOUPLER = new BlockPos(1, 2, 2);
  private static final double SPEED = 0.1;

  @GameTest(template = "platform_7x7", timeoutTicks = 200)
  public static void decouplerPulledTrain(GameTestHelper helper) {
    placeDetectorOnTankCartsSide(helper);
    var train = spawnTrain(helper, 3.75);
    helper.onEachTick(() -> {
      if (train.locoLinked()) {
        train.loco.setDeltaMovement(0, 0, -SPEED);
      } else {
        // Past the decoupler the platform ends, so the locomotive is moved on by hand, while the
        // tank carts keep rolling after it
        train.loco.setDeltaMovement(Vec3.ZERO);
        train.loco.setPos(train.loco.getX(), train.first.getY(), train.loco.getZ() - SPEED);
        train.first.setDeltaMovement(0, 0, -SPEED);
      }
      if (relativeZ(helper, train.first) < DECOUPLER.getZ() - 0.1) {
        train.assertOnlyLocoDecoupled(helper);
      }
    });
  }

  @GameTest(template = "platform_7x7", timeoutTicks = 200)
  public static void decouplerPushedTrain(GameTestHelper helper) {
    placeDetectorOnTankCartsSide(helper);
    var train = spawnTrain(helper, 0.6);
    helper.onEachTick(() -> {
      train.loco.setDeltaMovement(0, 0, SPEED);
      if (relativeZ(helper, train.loco) > DECOUPLER.getZ() + 1.1) {
        train.assertOnlyLocoDecoupled(helper);
      }
    });
  }

  /**
   * The tank carts are south of the locomotive, so is the detector, it powers the stone next to the
   * decoupler.
   */
  private static void placeDetectorOnTankCartsSide(GameTestHelper helper) {
    helper.setBlock(DECOUPLER.west(), Blocks.STONE);
    helper.setBlock(DECOUPLER.west().south(),
        RailcraftBlocks.LOCOMOTIVE_DETECTOR.get().defaultBlockState()
            .setValue(DetectorBlock.FACING, Direction.NORTH));
  }

  /**
   * A north-south line with a locomotive at the north end of two tank carts.
   */
  private static Train spawnTrain(GameTestHelper helper, double locoZ) {
    for (int z = 0; z <= 6; z++) {
      helper.setBlock(new BlockPos(1, 2, z), Blocks.RAIL);
    }
    helper.setBlock(DECOUPLER, RailcraftBlocks.IRON_COUPLER_TRACK.get().defaultBlockState()
        .setValue(CouplerTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
        .setValue(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.DECOUPLER));
    var level = helper.getLevel();
    var train = new Train(
        spawn(helper, RailcraftEntityTypes.STEAM_LOCOMOTIVE.get().create(level), locoZ),
        spawn(helper, RailcraftEntityTypes.TANK_MINECART.get().create(level), locoZ + 1.25),
        spawn(helper, RailcraftEntityTypes.TANK_MINECART.get().create(level), locoZ + 2.5));
    helper.assertTrue(RollingStock.getOrThrow(train.loco).link(RollingStock.getOrThrow(train.first)),
        "Locomotive should link to the first tank cart");
    helper.assertTrue(
        RollingStock.getOrThrow(train.first).link(RollingStock.getOrThrow(train.second)),
        "First tank cart should link to the second one");
    return train;
  }

  private static <T extends AbstractMinecart> T spawn(GameTestHelper helper, T cart, double z) {
    var pos = helper.absoluteVec(new Vec3(1.5, 2, z));
    cart.moveTo(pos.x(), pos.y(), pos.z(), 0, 0);
    helper.getLevel().addFreshEntity(cart);
    return cart;
  }

  private static double relativeZ(GameTestHelper helper, AbstractMinecart cart) {
    return cart.getZ() - helper.absolutePos(BlockPos.ZERO).getZ();
  }

  private record Train(AbstractMinecart loco, AbstractMinecart first, AbstractMinecart second) {

    boolean locoLinked() {
      return RollingStock.getOrThrow(this.loco).isLinkedWith(RollingStock.getOrThrow(this.first));
    }

    void assertOnlyLocoDecoupled(GameTestHelper helper) {
      helper.assertFalse(this.locoLinked(),
          "Locomotive should be decoupled from the first tank cart");
      helper.assertTrue(RollingStock.getOrThrow(this.first)
              .isLinkedWith(RollingStock.getOrThrow(this.second)),
          "Tank carts should still be coupled");
      helper.succeed();
    }
  }
}
