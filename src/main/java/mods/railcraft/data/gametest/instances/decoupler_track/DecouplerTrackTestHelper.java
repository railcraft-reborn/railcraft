package mods.railcraft.data.gametest.instances.decoupler_track;

import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.detector.DetectorBlock;
import mods.railcraft.world.level.block.entity.track.CouplerTrackBlockEntity;
import mods.railcraft.world.level.block.track.outfitted.CouplerTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

/**
 * Issue #185, a decoupler unlinks every cart passing over it while powered, so a locomotive
 * detector must turn it off before the cart next to the locomotive gets there. Placed on the side
 * of the tank carts, it does so whether the locomotive pulls or pushes them.
 */
final class DecouplerTrackTestHelper {

  static final BlockPos DECOUPLER = new BlockPos(1, 1, 2);
  static final double SPEED = 0.1;

  private DecouplerTrackTestHelper() {}

  /**
   * The tank carts are south of the locomotive, so is the detector, it powers the stone next to the
   * decoupler.
   */
  static void placeDetectorOnTankCartsSide(GameTestHelper helper) {
    helper.setBlock(DECOUPLER.west(), Blocks.STONE);
    helper.setBlock(DECOUPLER.west().south(),
        RailcraftBlocks.LOCOMOTIVE_DETECTOR.get().defaultBlockState()
            .setValue(DetectorBlock.FACING, Direction.NORTH));
  }

  /**
   * A north-south line with a locomotive at the north end of two tank carts.
   */
  static Train spawnTrain(GameTestHelper helper, double locoZ) {
    for (int z = 0; z <= 6; z++) {
      helper.setBlock(new BlockPos(1, 1, z), Blocks.RAIL);
    }
    helper.setBlock(DECOUPLER, RailcraftBlocks.IRON_COUPLER_TRACK.get().defaultBlockState()
        .setValue(CouplerTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
        .setValue(CouplerTrackBlock.MODE, CouplerTrackBlockEntity.Mode.DECOUPLER));
    var train = new Train(
        helper.spawn(RailcraftEntityTypes.STEAM_LOCOMOTIVE.get(), new Vec3(1.5, 1, locoZ)),
        helper.spawn(RailcraftEntityTypes.TANK_MINECART.get(), new Vec3(1.5, 1, locoZ + 1.25)),
        helper.spawn(RailcraftEntityTypes.TANK_MINECART.get(), new Vec3(1.5, 1, locoZ + 2.5)));
    helper.assertTrue(RollingStock.getOrThrow(train.loco).link(RollingStock.getOrThrow(train.first)),
        "Locomotive should link to the first tank cart");
    helper.assertTrue(
        RollingStock.getOrThrow(train.first).link(RollingStock.getOrThrow(train.second)),
        "First tank cart should link to the second one");
    return train;
  }

  static double relativeZ(GameTestHelper helper, AbstractMinecart cart) {
    return cart.getZ() - helper.absolutePos(BlockPos.ZERO).getZ();
  }

  record Train(AbstractMinecart loco, AbstractMinecart first, AbstractMinecart second) {

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
