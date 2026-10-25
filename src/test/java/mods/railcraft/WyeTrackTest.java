package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.WyeTrackBlock;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
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

  @GameTest(template = "wye_track_passive")
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
}
