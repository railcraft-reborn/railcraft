package mods.railcraft;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(Railcraft.ID)
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
