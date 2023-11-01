package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class OnewayTrackTest {

  @GameTest(template = "one_way_track_active")
  public static void onewayTrackActive(GameTestHelper helper) {
    helper.pressButton(0, 3, 1);
    helper.runAfterDelay(50L, () -> {
      helper.succeedWhenEntityPresent(EntityType.MINECART, 1, 2, 1);
    });
  }

  @GameTest(template = "one_way_track_passive")
  public static void onewayTrackPassive(GameTestHelper helper) {
    helper.pressButton(0, 3, 1);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 6, 2, 1);
  }
}
