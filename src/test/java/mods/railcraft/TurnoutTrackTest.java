package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class TurnoutTrackTest {

  private static final BlockPos JUNCTION_CENTER = new BlockPos(3, 2, 3);

  @GameTest(template = "turnout_track_active")
  public static void turnoutTrackActive(GameTestHelper helper) {
    helper.pressButton(1, 3, 2);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 4, 2, 4);
  }

  @GameTest(template = "turnout_track_passive")
  public static void turnoutTrackPassive(GameTestHelper helper) {
    helper.pressButton(1, 3, 2);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 5, 2, 2);
  }

  @GameTest(template = "turnout_track_active_reverse")
  public static void turnoutTrackActiveReverse(GameTestHelper helper) {
    helper.pressButton(6, 3, 2);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 2, 2, 2);
  }

  @GameTest(template = "turnout_track_passive_reverse")
  public static void turnoutTrackPassiveReverse(GameTestHelper helper) {
    helper.pressButton(4, 3, 6);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 2, 2, 2);
  }

  /**
   * Places a turnout in the middle of every T-junction layout, looking in every direction, and
   * checks that it follows the main line and diverges towards the branch.
   */
  @GameTest(template = "turnout_track_passive")
  public static void turnoutTrackPlacement(GameTestHelper helper) {
    for (var mainLine : Direction.Axis.values()) {
      if (mainLine == Direction.Axis.Y) {
        continue;
      }
      for (var branch : Direction.Plane.HORIZONTAL) {
        if (branch.getAxis() == mainLine) {
          continue;
        }
        for (var look : Direction.Plane.HORIZONTAL) {
          buildJunction(helper, mainLine, branch);
          placeTurnout(helper, look);

          var state = helper.getBlockState(JUNCTION_CENTER);
          var block = (TurnoutTrackBlock) state.getBlock();
          var level = helper.getLevel();
          var pos = helper.absolutePos(JUNCTION_CENTER);
          var straight = block.getRailDirection(state.setValue(SwitchTrackBlock.SWITCHED, false),
              level, pos, null);
          var diverging = block.getRailDirection(state.setValue(SwitchTrackBlock.SWITCHED, true),
              level, pos, null);
          var context = "main line %s, branch %s, looking %s"
              .formatted(mainLine, branch, look);
          helper.assertTrue(straight == straightShape(mainLine),
              "Expected straight shape along main line (%s), got %s"
                  .formatted(context, straight));
          helper.assertTrue(diverging.getSerializedName().contains(branch.getSerializedName()),
              "Expected diverging shape towards branch (%s), got %s"
                  .formatted(context, diverging));
        }
      }
    }
    helper.succeed();
  }

  private static void buildJunction(GameTestHelper helper, Direction.Axis mainLine,
      Direction branch) {
    for (int x = 0; x < 7; x++) {
      for (int z = 0; z < 7; z++) {
        helper.setBlock(x, 2, z, Blocks.AIR);
        helper.setBlock(x, 3, z, Blocks.AIR);
      }
    }
    var mainLineDirection = Direction.fromAxisAndDirection(mainLine, Direction.AxisDirection.POSITIVE);
    for (int i = 1; i <= 2; i++) {
      placeRail(helper, JUNCTION_CENTER.relative(mainLineDirection, i), straightShape(mainLine));
      placeRail(helper, JUNCTION_CENTER.relative(mainLineDirection, -i), straightShape(mainLine));
      placeRail(helper, JUNCTION_CENTER.relative(branch, i), straightShape(branch.getAxis()));
    }
  }

  private static void placeRail(GameTestHelper helper, BlockPos pos, RailShape shape) {
    helper.setBlock(pos, Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, shape));
  }

  private static void placeTurnout(GameTestHelper helper, Direction look) {
    var player = helper.makeMockPlayer(GameType.CREATIVE);
    player.setYRot(look.toYRot());
    var stack = new ItemStack(RailcraftItems.IRON_TURNOUT_TRACK.get());
    player.setItemInHand(InteractionHand.MAIN_HAND, stack);
    var floor = helper.absolutePos(JUNCTION_CENTER.below());
    var hitResult = new BlockHitResult(Vec3.upFromBottomCenterOf(floor, 1), Direction.UP, floor,
        false);
    stack.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hitResult));
  }

  private static RailShape straightShape(Direction.Axis axis) {
    return axis == Direction.Axis.X ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH;
  }
}
