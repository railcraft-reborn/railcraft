package mods.railcraft;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(Railcraft.ID)
@PrefixGameTestTemplate(false)
public class EmbarkingTrackTest {

  @GameTest(template = "embarking_track_active")
  public static void embarkingTrackActive(GameTestHelper helper) {
    helper.pressButton(0, 3, 3);
    helper.succeedWhen(() -> {
      helper.assertEntityPresent(EntityType.MINECART, 5, 2, 3);
      helper.assertEntityPresent(EntityType.VILLAGER, 5, 2, 3);
    });
  }

  @GameTest(template = "embarking_track_passive")
  public static void embarkingTrackPassive(GameTestHelper helper) {
    helper.pressButton(0, 3, 3);
    helper.succeedWhen(() -> {
      helper.assertEntityPresent(EntityType.MINECART, 5, 2, 3);
      helper.assertEntityPresent(EntityType.VILLAGER, 3, 2, 4);
    });
  }
}
