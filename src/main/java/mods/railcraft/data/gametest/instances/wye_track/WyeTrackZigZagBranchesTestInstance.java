package mods.railcraft.data.gametest.instances.wye_track;

import java.util.List;
import java.util.Map;
import com.mojang.serialization.MapCodec;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.data.gametest.instances.JunctionPlacementHelper;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.WyeTrackBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.RailShape;

/**
 * Places a wye between a straight trunk and two zig-zag branches. The wye must keep its routes and
 * the first rail of each branch must curve into it, whichever was built first.
 */
public class WyeTrackZigZagBranchesTestInstance extends GameTestInstance {

  public static final MapCodec<WyeTrackZigZagBranchesTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(WyeTrackZigZagBranchesTestInstance::new);

  public WyeTrackZigZagBranchesTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
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

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Wye Track Zig-Zag Branches");
  }
}
