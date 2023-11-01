package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class JunctionTrackTest {

  @GameTest(template = "junction_track")
  public static void junctionTrackWest(GameTestHelper helper) {
    helper.pressButton(0, 3, 3);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 5, 2, 3);
  }

  @GameTest(template = "junction_track")
  public static void junctionTrackNorth(GameTestHelper helper) {
    helper.pressButton(3, 3, 0);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 3, 2, 5);
  }
}
