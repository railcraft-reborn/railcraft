package mods.railcraft.data.gametest.instances.signal_capacitor_box;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * The duration can't go below zero, nor above the longest whole number of seconds a short holds.
 */
public class SignalCapacitorBoxDurationLimitsTestInstance extends GameTestInstance {

  public static final MapCodec<SignalCapacitorBoxDurationLimitsTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(SignalCapacitorBoxDurationLimitsTestInstance::new);

  public SignalCapacitorBoxDurationLimitsTestInstance(
      TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    helper.setBlock(SignalCapacitorBoxPulseTestInstance.CAPACITOR,
        RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get());
    var capacitor = SignalCapacitorBoxPulseTestInstance.capacitor(helper);
    capacitor.setTicksToPower((short) -20);
    helper.assertTrue(capacitor.getTicksToPower() == 0,
        "Negative duration should be clamped to zero, got " + capacitor.getTicksToPower());
    capacitor.setTicksToPower(Short.MAX_VALUE);
    helper.assertTrue(
        capacitor.getTicksToPower() == SignalCapacitorBoxBlockEntity.MAX_TICKS_TO_POWER,
        "Duration should be clamped to whole seconds, got " + capacitor.getTicksToPower());
    helper.succeed();
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Signal Capacitor Box Duration Limits");
  }
}
