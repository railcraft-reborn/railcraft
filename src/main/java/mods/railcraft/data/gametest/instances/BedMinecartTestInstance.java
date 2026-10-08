package mods.railcraft.data.gametest.instances;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class BedMinecartTestInstance extends GameTestInstance {

  public static final MapCodec<BedMinecartTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(BedMinecartTestInstance::new);

  public BedMinecartTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @SuppressWarnings("removal")
  @Override
  public void run(GameTestHelper helper) {
    var cart = helper.spawn(RailcraftEntityTypes.BED_MINECART.get(), 1, 2, 1);
    var player = helper.makeMockServerPlayerInLevel();
    Runnable cleanup = () -> {
      setDayTime(helper, 1000);
      helper.getLevel().getServer().getPlayerList().remove(player);
    };
    try {
      player.snapTo(cart.position());
      helper.assertTrue(player.startRiding(cart, true, true), "Player should ride the cart");
    } catch (RuntimeException e) {
      cleanup.run();
      throw e;
    }

    // The sky brightness used to detect the day is updated when the level ticks
    setDayTime(helper, 1000);
    helper.runAfterDelay(2, () -> {
      try {
        cart.attemptSleep(player);
        helper.assertFalse(player.isSleeping(), "Player should not sleep during the day");
        helper.assertTrue(player.getVehicle() == cart, "Player should still ride the cart");
        setDayTime(helper, 18000);
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

  private static void setDayTime(GameTestHelper helper, int time) {
    var level = helper.getLevel();
    level.dimensionType().defaultClock()
        .ifPresent(clock -> level.getServer().clockManager().setTotalTicks(clock, time));
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Bed Minecart");
  }
}
