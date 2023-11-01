package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
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
}
