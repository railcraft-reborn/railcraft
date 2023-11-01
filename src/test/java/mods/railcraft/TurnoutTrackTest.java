package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
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
}
