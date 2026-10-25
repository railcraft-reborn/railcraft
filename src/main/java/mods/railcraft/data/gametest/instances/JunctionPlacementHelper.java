package mods.railcraft.data.gametest.instances;

import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Places a track in the middle of every T-junction layout, looking in every direction.
 */
public final class JunctionPlacementHelper {

  public static final BlockPos CENTER = new BlockPos(3, 1, 3);

  private JunctionPlacementHelper() {}

  /**
   * @param check - receives the main line axis and the branch direction, called once the track
   *        has been placed at {@link #CENTER}
   */
  public static void forEachPlacement(GameTestHelper helper, Item track,
      BiConsumer<Direction.Axis, Direction> check) {
    for (var mainLine : List.of(Direction.Axis.X, Direction.Axis.Z)) {
      for (var branch : Direction.Plane.HORIZONTAL) {
        if (branch.getAxis() == mainLine) {
          continue;
        }
        for (var look : Direction.Plane.HORIZONTAL) {
          buildJunction(helper, mainLine, branch);
          placeTrack(helper, track, look);
          check.accept(mainLine, branch);
        }
      }
    }
  }

  public static String describe(GameTestHelper helper, Direction.Axis mainLine,
      Direction branch) {
    return "main line %s, branch %s, state %s"
        .formatted(mainLine, branch, helper.getBlockState(CENTER));
  }

  public static RailShape straightShape(Direction.Axis axis) {
    return axis == Direction.Axis.X ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH;
  }

  public static boolean connects(RailShape shape, Direction side) {
    return shape.getSerializedName().contains(side.getSerializedName());
  }

  private static void buildJunction(GameTestHelper helper, Direction.Axis mainLine,
      Direction branch) {
    for (int x = 0; x < 7; x++) {
      for (int z = 0; z < 7; z++) {
        helper.setBlock(x, 1, z, Blocks.AIR);
        helper.setBlock(x, 2, z, Blocks.AIR);
      }
    }
    var mainLineDirection =
        Direction.fromAxisAndDirection(mainLine, Direction.AxisDirection.POSITIVE);
    for (int i = 1; i <= 2; i++) {
      placeRail(helper, CENTER.relative(mainLineDirection, i), straightShape(mainLine));
      placeRail(helper, CENTER.relative(mainLineDirection, -i), straightShape(mainLine));
      placeRail(helper, CENTER.relative(branch, i), straightShape(branch.getAxis()));
    }
  }

  private static void placeRail(GameTestHelper helper, BlockPos pos, RailShape shape) {
    helper.setBlock(pos, Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, shape));
  }

  private static void placeTrack(GameTestHelper helper, Item track, Direction look) {
    var player = helper.makeMockPlayer(GameType.CREATIVE);
    player.setYRot(look.toYRot());
    var stack = new ItemStack(track);
    player.setItemInHand(InteractionHand.MAIN_HAND, stack);
    var floor = helper.absolutePos(CENTER.below());
    var hitResult = new BlockHitResult(Vec3.upFromBottomCenterOf(floor, 1), Direction.UP, floor,
        false);
    stack.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hitResult));
  }
}
