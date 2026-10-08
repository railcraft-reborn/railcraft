package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class BedMinecartTest {

  @SuppressWarnings("removal")
  @GameTest(template = "detector_track")
  public static void bedMinecartSleep(GameTestHelper helper) {
    var cart = helper.spawn(RailcraftEntityTypes.BED_MINECART.get(), 1, 2, 1);
    var player = helper.makeMockServerPlayerInLevel();
    Runnable cleanup = () -> {
      helper.setDayTime(1000);
      helper.getLevel().getServer().getPlayerList().remove(player);
    };
    try {
      player.moveTo(cart.position());
      helper.assertTrue(player.startRiding(cart, true), "Player should ride the cart");
    } catch (RuntimeException e) {
      cleanup.run();
      throw e;
    }

    // The sky brightness used to detect the day is updated when the level ticks
    helper.setDayTime(1000);
    helper.runAfterDelay(2, () -> {
      try {
        cart.attemptSleep(player);
        helper.assertFalse(player.isSleeping(), "Player should not sleep during the day");
        helper.assertTrue(player.getVehicle() == cart, "Player should still ride the cart");
        helper.setDayTime(18000);
      } catch (RuntimeException e) {
        cleanup.run();
        throw e;
      }
    });

    helper.runAfterDelay(4, () -> {
      try {
        cart.attemptSleep(player);
        helper.assertTrue(player.isSleeping(), "Player should sleep at night");
        helper.assertTrue(player.getVehicle() == cart, "Player should sleep on the cart");

        // The mock player is not ticked by the server
        for (int i = 0; i < 20; i++) {
          player.doTick();
        }
        helper.assertTrue(player.isSleeping(),
            "Player should keep sleeping without a bed block");
        helper.assertTrue(player.getVehicle() == cart,
            "Player should keep riding while sleeping");

        player.stopSleepInBed(false, true);
        helper.assertFalse(player.isSleeping(), "Player should wake up");
        helper.assertTrue(player.getVehicle() == cart,
            "Player should ride the cart after waking up");

        player.stopRiding();
        player.startSleeping(cart.blockPosition());
        player.doTick();
        helper.assertFalse(player.isSleeping(), "Player should not sleep without the cart");
      } finally {
        cleanup.run();
      }
      helper.succeed();
    });
  }
}
