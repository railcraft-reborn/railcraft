package mods.railcraft.data.gametest.instances.turnout_track;

import java.util.List;
import com.mojang.serialization.MapCodec;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.data.gametest.instances.JunctionPlacementHelper;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * A straight main line is turned into a turnout with a spike maul, next to a zig-zag branch (issue
 * #285). The turnout must keep following the main line and the first branch rail must curve into
 * its diverging route, whichever was built first.
 */
public class TurnoutTrackSpikeMaulBranchTestInstance extends GameTestInstance {

  public static final MapCodec<TurnoutTrackSpikeMaulBranchTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(TurnoutTrackSpikeMaulBranchTestInstance::new);

  public TurnoutTrackSpikeMaulBranchTestInstance(
      TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
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

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Turnout Track Spike Maul Branch");
  }
}
