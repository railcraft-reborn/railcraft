package mods.railcraft;

import java.util.List;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class TurnoutTrackTest {

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

  @GameTest(template = "platform_7x7")
  public static void turnoutTrackPlacement(GameTestHelper helper) {
    JunctionPlacementHelper.forEachPlacement(helper, RailcraftItems.IRON_TURNOUT_TRACK.get(),
        (mainLine, branch) -> {
          var state = helper.getBlockState(JunctionPlacementHelper.CENTER);
          var block = (TurnoutTrackBlock) state.getBlock();
          var pos = helper.absolutePos(JunctionPlacementHelper.CENTER);
          var straight = block.getRailDirection(
              state.setValue(SwitchTrackBlock.SWITCHED, false), helper.getLevel(), pos, null);
          var diverging = block.getRailDirection(
              state.setValue(SwitchTrackBlock.SWITCHED, true), helper.getLevel(), pos, null);
          var context = JunctionPlacementHelper.describe(helper, mainLine, branch);
          helper.assertTrue(straight == JunctionPlacementHelper.straightShape(mainLine),
              "Straight route must follow the main line: " + context);
          helper.assertTrue(JunctionPlacementHelper.connects(diverging, branch),
              "Diverging route must lead to the branch: " + context);
        });
    helper.succeed();
  }

  /**
   * A straight main line is turned into a turnout with a spike maul, next to a zig-zag branch
   * (issue #285). The turnout must keep following the main line and the first branch rail must
   * curve into its diverging route, whichever was built first.
   */
  @GameTest(template = "platform_7x7")
  public static void turnoutTrackSpikeMaulBranch(GameTestHelper helper) {
    var tracks = List.of((BaseRailBlock) Blocks.RAIL, RailcraftBlocks.REINFORCED_TRACK.get());
    for (var track : tracks) {
      for (var mainLine : List.of(Direction.Axis.X, Direction.Axis.Z)) {
        var ahead = Direction.fromAxisAndDirection(mainLine, Direction.AxisDirection.NEGATIVE);
        for (var branch : List.of(ahead.getClockWise(), ahead.getCounterClockWise())) {
          for (var look : Direction.Plane.HORIZONTAL) {
            for (boolean branchFirst : new boolean[] {true, false}) {
              JunctionPlacementHelper.clear(helper);
              var center = JunctionPlacementHelper.CENTER;
              for (int i = -3; i <= 3; i++) {
                helper.setBlock(center.relative(ahead, i), TrackUtil.setShape(track,
                    JunctionPlacementHelper.straightShape(mainLine)));
              }
              if (branchFirst) {
                buildZigZag(helper, track, ahead, branch);
                useSpikeMaul(helper, look);
              } else {
                useSpikeMaul(helper, look);
                buildZigZag(helper, track, ahead, branch);
              }

              var state = helper.getBlockState(center);
              var context = "track %s, main line %s, branch %s, looking %s, branch first %s: %s"
                  .formatted(track, mainLine, branch, look, branchFirst, state);
              helper.assertTrue(state.getBlock() instanceof TurnoutTrackBlock,
                  "Expected a turnout: " + context);
              var block = (TurnoutTrackBlock) state.getBlock();
              var pos = helper.absolutePos(center);
              var straight = block.getRailDirection(
                  state.setValue(SwitchTrackBlock.SWITCHED, false), helper.getLevel(), pos, null);
              var diverging = block.getRailDirection(
                  state.setValue(SwitchTrackBlock.SWITCHED, true), helper.getLevel(), pos, null);
              helper.assertTrue(straight == JunctionPlacementHelper.straightShape(mainLine),
                  "Straight route must follow the main line, " + context);
              helper.assertTrue(JunctionPlacementHelper.connects(diverging, branch),
                  "Diverging route must lead to the branch, " + context);
              var firstBranchRail = TrackUtil.getRailShapeRaw(
                  helper.getBlockState(center.relative(branch)));
              helper.assertTrue(
                  JunctionPlacementHelper.connects(firstBranchRail, branch.getOpposite())
                      && JunctionPlacementHelper.connects(firstBranchRail, ahead),
                  "First branch rail must curve into the turnout, got %s, %s"
                      .formatted(firstBranchRail, context));
            }
          }
        }
      }
    }
    helper.succeed();
  }

  /**
   * Places the branch rail by rail from the turnout outwards, as a player would.
   */
  private static void buildZigZag(GameTestHelper helper, BaseRailBlock track, Direction ahead,
      Direction branch) {
    var pos = JunctionPlacementHelper.CENTER.relative(branch);
    for (int i = 0; i < 4; i++) {
      helper.setBlock(pos, TrackUtil.setShape(track, RailShape.NORTH_SOUTH));
      pos = i % 2 == 0 ? pos.relative(ahead) : pos.relative(branch);
    }
  }

  private static void useSpikeMaul(GameTestHelper helper, Direction look) {
    var player = helper.makeMockPlayer(GameType.CREATIVE);
    player.setYRot(look.toYRot());
    var stack = new ItemStack(RailcraftItems.STEEL_SPIKE_MAUL.get());
    player.setItemInHand(InteractionHand.MAIN_HAND, stack);
    var pos = helper.absolutePos(JunctionPlacementHelper.CENTER);
    var hitResult = new BlockHitResult(Vec3.upFromBottomCenterOf(pos, 1), Direction.UP, pos,
        false);
    stack.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hitResult));
  }
}
