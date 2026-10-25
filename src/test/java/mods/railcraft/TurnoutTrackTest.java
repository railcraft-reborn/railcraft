package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.TurnoutTrackBlock;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
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

  @GameTest(template = "turnout_track_passive")
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
}
