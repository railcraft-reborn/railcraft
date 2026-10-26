package mods.railcraft;

import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.TrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Issue #379, a high speed cart going over a slope sits in the block above the sloped track, which
 * must not be mistaken for leaving the high speed line.
 */
@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class HighSpeedTrackTest {

  private static final int SLOPE_Z = 3;
  private static final double SPEED = 1.0;

  @GameTest(template = "platform_7x7_tall", timeoutTicks = 100)
  public static void highSpeedCartGoesDownSlope(GameTestHelper helper) {
    buildLine(helper, 3, RailShape.ASCENDING_NORTH, 2);
    runCart(helper, 3, 1.68);
  }

  @GameTest(template = "platform_7x7_tall", timeoutTicks = 100)
  public static void highSpeedCartGoesUpSlope(GameTestHelper helper) {
    buildLine(helper, 2, RailShape.ASCENDING_SOUTH, 3);
    runCart(helper, 2, 1.72);
  }

  /**
   * A north-south line of high speed track, with a slope in the middle going from the height of the
   * northern half to the one of the southern half.
   */
  private static void buildLine(GameTestHelper helper, int northY, RailShape slope, int southY) {
    for (int z = 0; z <= 6; z++) {
      int y = z < SLOPE_Z ? northY : z > SLOPE_Z ? southY : 2;
      var shape = z == SLOPE_Z ? slope : RailShape.NORTH_SOUTH;
      if (y > 2) {
        helper.setBlock(new BlockPos(1, y - 1, z), Blocks.STONE);
      }
      helper.setBlock(new BlockPos(1, y, z), RailcraftBlocks.HIGH_SPEED_TRACK.get()
          .defaultBlockState().setValue(TrackBlock.SHAPE, shape));
    }
  }

  private static void runCart(GameTestHelper helper, int startY, double startZ) {
    var cart = EntityType.MINECART.create(helper.getLevel());
    var pos = helper.absoluteVec(new Vec3(1.5, startY, startZ));
    cart.moveTo(pos.x(), pos.y(), pos.z(), 0, 0);
    cart.setDeltaMovement(0, 0, SPEED);
    helper.getLevel().addFreshEntity(cart);
    var highSpeed = new boolean[1];
    helper.onEachTick(() -> {
      // An exploding cart is stopped, and killed half of the times
      helper.assertTrue(cart.isAlive() && cart.getDeltaMovement().horizontalDistanceSqr() > 0,
          "High speed cart should not explode on the slope");
      if (relativeZ(helper, cart) < SLOPE_Z) {
        cart.setDeltaMovement(0, cart.getDeltaMovement().y(), SPEED);
      }
      highSpeed[0] |= RollingStock.getOrThrow(cart).isHighSpeed();
      if (relativeZ(helper, cart) > SLOPE_Z + 1.5) {
        helper.assertTrue(highSpeed[0], "Cart should have gone at high speed");
        helper.succeed();
      }
    });
  }

  private static double relativeZ(GameTestHelper helper, AbstractMinecart cart) {
    return cart.getZ() - helper.absolutePos(BlockPos.ZERO).getZ();
  }
}
