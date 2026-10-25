package mods.railcraft.data.gametest.instances.locomotive;

import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * A locomotive pushes along its yaw, so it must keep facing the same way while it moves around.
 */
final class LocomotiveTestHelper {

  static final BlockPos START = new BlockPos(1, 1, 1);
  static final BlockPos BEFORE_CURVE = new BlockPos(1, 1, 2);
  static final BlockPos CURVE = new BlockPos(1, 1, 5);
  static final BlockPos AFTER_CURVE = new BlockPos(4, 1, 5);

  private LocomotiveTestHelper() {}

  /**
   * A north-south line along x = 1 turning east at {@link #CURVE}.
   */
  static Locomotive spawnOnCurvedLine(GameTestHelper helper) {
    for (int z = 0; z <= CURVE.getZ(); z++) {
      helper.setBlock(new BlockPos(1, 1, z), Blocks.RAIL);
    }
    for (int x = 2; x <= 6; x++) {
      helper.setBlock(new BlockPos(x, 1, CURVE.getZ()), Blocks.RAIL);
    }
    return helper.spawn(RailcraftEntityTypes.STEAM_LOCOMOTIVE.get(), START);
  }

  /**
   * An east-west line along z = 3.
   */
  static void buildStraightLine(GameTestHelper helper) {
    for (int x = 0; x <= 6; x++) {
      helper.setBlock(new BlockPos(x, 1, 3), Blocks.RAIL);
    }
  }

  static Locomotive placeLocomotive(GameTestHelper helper, BlockPos pos, float lookYaw) {
    var player = helper.makeMockPlayer(GameType.CREATIVE);
    player.setYRot(lookYaw);
    var stack = new ItemStack(RailcraftItems.STEAM_LOCOMOTIVE.get());
    player.setItemInHand(InteractionHand.MAIN_HAND, stack);
    var railPos = helper.absolutePos(pos);
    var hitResult = new BlockHitResult(Vec3.atCenterOf(railPos), Direction.UP, railPos, false);
    stack.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hitResult));
    return helper.findOneEntity(RailcraftEntityTypes.STEAM_LOCOMOTIVE.get());
  }

  static void push(Locomotive loco, double speed) {
    loco.setDeltaMovement(0, 0, speed);
  }

  /**
   * Keeps the speed up against the locomotive drag, whichever way the track leads it.
   */
  static void keepPushing(Locomotive loco, double speed) {
    var motion = loco.getDeltaMovement();
    if (motion.horizontalDistance() > 1.0E-4) {
      loco.setDeltaMovement(motion.normalize().scale(speed));
    }
  }

  static boolean at(GameTestHelper helper, Locomotive loco, BlockPos pos) {
    return loco.blockPosition().equals(helper.absolutePos(pos));
  }
}
