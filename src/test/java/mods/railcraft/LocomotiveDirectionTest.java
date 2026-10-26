package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * A locomotive pushes along its yaw, so it must keep facing the same way while it moves around.
 */
@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class LocomotiveDirectionTest {

  private static final BlockPos START = new BlockPos(1, 2, 1);
  private static final BlockPos BEFORE_CURVE = new BlockPos(1, 2, 2);
  private static final BlockPos CURVE = new BlockPos(1, 2, 5);
  private static final BlockPos AFTER_CURVE = new BlockPos(4, 2, 5);

  @GameTest(template = "platform_7x7", timeoutTicks = 200)
  public static void locomotiveKeepsFacingThroughCurve(GameTestHelper helper) {
    var loco = spawnOnCurvedLine(helper);
    var speeds = new double[] {0.1, 0.2, 0.4};
    int[] run = {0};
    push(helper, loco, speeds[0]);
    helper.onEachTick(() -> {
      keepPushing(loco, speeds[run[0]]);
      if (!at(helper, loco, AFTER_CURVE)) {
        return;
      }
      // The yaw of an unflipped cart points against its motion, which is east after the curve
      helper.assertFalse(loco.flipped, "Locomotive should not flip at speed " + speeds[run[0]]);
      helper.assertTrue(Mth.degreesDifferenceAbs(loco.getYRot(), 180) < 1,
          "Locomotive yaw should point west at speed %s, got %s"
              .formatted(speeds[run[0]], loco.getYRot()));
      if (++run[0] == speeds.length) {
        helper.succeed();
        return;
      }
      loco.moveTo(Vec3.atBottomCenterOf(helper.absolutePos(START)), 0, 0);
      loco.flipped = false;
      push(helper, loco, speeds[run[0]]);
    });
  }

  /**
   * Issue #339, a locomotive pushed back while on a curve used to turn around.
   */
  @GameTest(template = "platform_7x7", timeoutTicks = 200)
  public static void locomotiveKeepsFacingWhenReversedInCurve(GameTestHelper helper) {
    var loco = spawnOnCurvedLine(helper);
    float[] yawBefore = {Float.NaN};
    boolean[] reversed = {false};
    push(helper, loco, 0.2);
    helper.onEachTick(() -> {
      keepPushing(loco, 0.2);
      if (!reversed[0] && Float.isNaN(yawBefore[0]) && at(helper, loco, BEFORE_CURVE)) {
        yawBefore[0] = loco.getYRot();
      } else if (!reversed[0] && at(helper, loco, CURVE)) {
        reversed[0] = true;
        loco.setDeltaMovement(loco.getDeltaMovement().scale(-1));
      } else if (reversed[0] && at(helper, loco, BEFORE_CURVE)) {
        helper.assertTrue(Mth.degreesDifferenceAbs(yawBefore[0], loco.getYRot()) < 1,
            "Locomotive should still face the same way, was %s, now %s"
                .formatted(yawBefore[0], loco.getYRot()));
        helper.succeed();
      }
    });
  }

  /**
   * Issue #339, reversing a locomotive with a crowbar, standing or moving, must turn it around for
   * good.
   */
  @GameTest(template = "platform_7x7", timeoutTicks = 200)
  public static void locomotiveStaysReversed(GameTestHelper helper) {
    for (int x = 0; x <= 6; x++) {
      helper.setBlock(new BlockPos(x, 2, 3), Blocks.RAIL);
    }
    var loco = helper.spawn(RailcraftEntityTypes.STEAM_LOCOMOTIVE.get(), new BlockPos(3, 2, 3));
    float[] yaw = {Float.NaN};
    helper.runAtTickTime(5, () -> {
      yaw[0] = loco.getYRot();
      loco.reverse();
    });
    helper.runAtTickTime(15, () -> {
      helper.assertTrue(Mth.degreesDifferenceAbs(yaw[0] + 180, loco.getYRot()) < 1,
          "Standing locomotive should stay reversed, was %s, now %s"
              .formatted(yaw[0], loco.getYRot()));
      yaw[0] = loco.getYRot();
      loco.setDeltaMovement(0.2, 0, 0);
    });
    helper.runAtTickTime(18, () -> {
      helper.assertTrue(Mth.degreesDifferenceAbs(yaw[0], loco.getYRot()) < 1,
          "Pushing the locomotive should not turn it, was %s, now %s"
              .formatted(yaw[0], loco.getYRot()));
      loco.reverse();
    });
    helper.runAtTickTime(21, () -> {
      helper.assertTrue(Mth.degreesDifferenceAbs(yaw[0] + 180, loco.getYRot()) < 1,
          "Moving locomotive should stay reversed, was %s, now %s"
              .formatted(yaw[0], loco.getYRot()));
      helper.assertTrue(loco.getDeltaMovement().x() < 0,
          "Moving locomotive should go the other way");
      helper.succeed();
    });
  }

  /**
   * Issue #339, a placed locomotive used to be at right angles with the track, so pushing it
   * turned it either way.
   */
  @GameTest(template = "platform_7x7", timeoutTicks = 100)
  public static void placedLocomotiveFacesAlongTrack(GameTestHelper helper) {
    for (int x = 0; x <= 6; x++) {
      helper.setBlock(new BlockPos(x, 2, 3), Blocks.RAIL);
    }
    // Looking mostly east, a bit north
    var loco = placeLocomotive(helper, new BlockPos(3, 2, 3), 240);
    helper.assertTrue(Mth.degreesDifferenceAbs(loco.getYRot(), 0) < 1,
        "Placed locomotive should face east, got " + loco.getYRot());
    helper.runAtTickTime(5, () -> loco.setDeltaMovement(0.2, 0, 0));
    helper.runAtTickTime(10, () -> {
      helper.assertTrue(Mth.degreesDifferenceAbs(loco.getYRot(), 0) < 1,
          "Locomotive pushed forward should still face east, got " + loco.getYRot());
      loco.setDeltaMovement(-0.2, 0, 0);
    });
    helper.runAtTickTime(15, () -> {
      helper.assertTrue(Mth.degreesDifferenceAbs(loco.getYRot(), 0) < 1,
          "Locomotive pushed back should still face east, got " + loco.getYRot());
      helper.succeed();
    });
  }

  private static Locomotive placeLocomotive(GameTestHelper helper, BlockPos pos, float lookYaw) {
    var player = helper.makeMockPlayer(GameType.CREATIVE);
    player.setYRot(lookYaw);
    var stack = new ItemStack(RailcraftItems.STEAM_LOCOMOTIVE.get());
    player.setItemInHand(InteractionHand.MAIN_HAND, stack);
    var railPos = helper.absolutePos(pos);
    var hitResult = new BlockHitResult(Vec3.atCenterOf(railPos), Direction.UP, railPos, false);
    stack.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hitResult));
    return helper.findOneEntity(RailcraftEntityTypes.STEAM_LOCOMOTIVE.get());
  }

  /**
   * A north-south line along x = 1 turning east at {@link #CURVE}.
   */
  private static Locomotive spawnOnCurvedLine(GameTestHelper helper) {
    for (int z = 0; z <= CURVE.getZ(); z++) {
      helper.setBlock(new BlockPos(1, 2, z), Blocks.RAIL);
    }
    for (int x = 2; x <= 6; x++) {
      helper.setBlock(new BlockPos(x, 2, CURVE.getZ()), Blocks.RAIL);
    }
    return helper.spawn(RailcraftEntityTypes.STEAM_LOCOMOTIVE.get(), START);
  }

  private static void push(GameTestHelper helper, Locomotive loco, double speed) {
    loco.setDeltaMovement(0, 0, speed);
  }

  /**
   * Keeps the speed up against the locomotive drag, whichever way the track leads it.
   */
  private static void keepPushing(Locomotive loco, double speed) {
    var motion = loco.getDeltaMovement();
    if (motion.horizontalDistance() > 1.0E-4) {
      loco.setDeltaMovement(motion.normalize().scale(speed));
    }
  }

  private static boolean at(GameTestHelper helper, Locomotive loco, BlockPos pos) {
    return loco.blockPosition().equals(helper.absolutePos(pos));
  }
}
