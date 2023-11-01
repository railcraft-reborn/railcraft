package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class CartDispenserTest {

  @GameTest(template = "cart_dispenser_dispense")
  public static void cartDispenserDispense(GameTestHelper helper) {
    helper.pressButton(0, 2, 1);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 1, 2, 0);
  }

  @GameTest(template = "cart_dispenser_pick_up")
  public static void cartDispenserPickup(GameTestHelper helper) {
    helper.pressButton(0, 2, 1);
    helper.succeedWhenEntityNotPresent(EntityType.MINECART, 1, 2, 0);
  }
}
