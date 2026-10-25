package mods.railcraft;

import java.util.List;
import java.util.Map;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.WyeTrackBlock;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class WyeTrackTest {

  @GameTest(template = "wye_track_active")
  public static void wyeTrackActive(GameTestHelper helper) {
    helper.pressButton(0, 3, 3);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 3, 2, 1);
  }

  @GameTest(template = "wye_track_passive")
  public static void wyeTrackPassive(GameTestHelper helper) {
    helper.pressButton(0, 3, 3);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 3, 2, 5);
  }

  @GameTest(template = "platform_7x7")
  public static void wyeTrackPlacement(GameTestHelper helper) {
    JunctionPlacementHelper.forEachPlacement(helper, RailcraftItems.IRON_WYE_TRACK.get(),
        (mainLine, branch) -> {
          var state = helper.getBlockState(JunctionPlacementHelper.CENTER);
          var block = (WyeTrackBlock) state.getBlock();
          var pos = helper.absolutePos(JunctionPlacementHelper.CENTER);
          var left = block.getRailDirection(
              state.setValue(SwitchTrackBlock.SWITCHED, false), helper.getLevel(), pos, null);
          var right = block.getRailDirection(
              state.setValue(SwitchTrackBlock.SWITCHED, true), helper.getLevel(), pos, null);
          var context = JunctionPlacementHelper.describe(helper, mainLine, branch);
          helper.assertTrue(JunctionPlacementHelper.connects(left, branch)
              && JunctionPlacementHelper.connects(right, branch),
              "Both routes must lead to the branch: " + context);
        });
    helper.succeed();
  }

  /**
   * Places a wye between a straight trunk and two zig-zag branches. The wye must keep its routes
   * and the first rail of each branch must curve into it, whichever was built first.
   */
  @GameTest(template = "platform_7x7")
  public static void wyeTrackZigZagBranches(GameTestHelper helper) {
    Map<BaseRailBlock, Item> tracks = Map.of(
        (BaseRailBlock) Blocks.RAIL, RailcraftItems.IRON_WYE_TRACK.get(),
        RailcraftBlocks.REINFORCED_TRACK.get(), RailcraftItems.REINFORCED_WYE_TRACK.get());
    var center = JunctionPlacementHelper.CENTER;
    for (var entry : tracks.entrySet()) {
      var track = entry.getKey();
      for (var trunk : Direction.Plane.HORIZONTAL) {
        var ahead = trunk.getOpposite();
        var sides = List.of(ahead.getClockWise(), ahead.getCounterClockWise());
        for (var look : Direction.Plane.HORIZONTAL) {
          for (boolean branchesFirst : new boolean[] {true, false}) {
            JunctionPlacementHelper.clear(helper);
            for (int i = 1; i <= 3; i++) {
              helper.setBlock(center.relative(trunk, i), TrackUtil.setShape(track,
                  JunctionPlacementHelper.straightShape(trunk.getAxis())));
            }
            if (branchesFirst) {
              sides.forEach(side -> buildZigZag(helper, track, ahead, side));
              JunctionPlacementHelper.placeTrack(helper, entry.getValue(), look);
            } else {
              JunctionPlacementHelper.placeTrack(helper, entry.getValue(), look);
              sides.forEach(side -> buildZigZag(helper, track, ahead, side));
            }

            var state = helper.getBlockState(center);
            var context = "track %s, trunk %s, looking %s, branches first %s: %s"
                .formatted(track, trunk, look, branchesFirst, state);
            helper.assertTrue(state.getBlock() instanceof WyeTrackBlock,
                "Expected a wye: " + context);
            var block = (WyeTrackBlock) state.getBlock();
            var pos = helper.absolutePos(center);
            var left = block.getRailDirection(
                state.setValue(SwitchTrackBlock.SWITCHED, false), helper.getLevel(), pos, null);
            var right = block.getRailDirection(
                state.setValue(SwitchTrackBlock.SWITCHED, true), helper.getLevel(), pos, null);
            helper.assertTrue(JunctionPlacementHelper.connects(left, trunk)
                && JunctionPlacementHelper.connects(right, trunk),
                "Both routes must lead to the trunk, " + context);
            for (var side : sides) {
              helper.assertTrue(JunctionPlacementHelper.connects(left, side)
                  || JunctionPlacementHelper.connects(right, side),
                  "A route must lead to the %s branch, %s".formatted(side, context));
              var firstBranchRail = TrackUtil.getRailShapeRaw(
                  helper.getBlockState(center.relative(side)));
              helper.assertTrue(
                  JunctionPlacementHelper.connects(firstBranchRail, side.getOpposite())
                      && JunctionPlacementHelper.connects(firstBranchRail, ahead),
                  "First rail of the %s branch must curve into the wye, got %s, %s"
                      .formatted(side, firstBranchRail, context));
            }
          }
        }
      }
    }
    helper.succeed();
  }

  /**
   * Places the branch rail by rail from the wye outwards, as a player would.
   */
  private static void buildZigZag(GameTestHelper helper, BaseRailBlock track, Direction ahead,
      Direction side) {
    var pos = JunctionPlacementHelper.CENTER.relative(side);
    for (int i = 0; i < 4; i++) {
      helper.setBlock(pos, TrackUtil.setShape(track, RailShape.NORTH_SOUTH));
      pos = i % 2 == 0 ? pos.relative(ahead) : pos.relative(side);
    }
  }
}
