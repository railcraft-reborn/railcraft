package mods.railcraft.data.gametest.instances.signal_capacitor_box;

import com.mojang.serialization.MapCodec;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RepeaterBlock;

/**
 * A short pulse must power the capacitor for the configured time only, and its countdown must then
 * stop instead of running on until it wraps around and powers the capacitor again.
 */
public class SignalCapacitorBoxPulseTestInstance extends GameTestInstance {

  public static final MapCodec<SignalCapacitorBoxPulseTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(SignalCapacitorBoxPulseTestInstance::new);

  static final BlockPos CAPACITOR = new BlockPos(3, 1, 3);
  private static final BlockPos REPEATER = CAPACITOR.east();
  private static final BlockPos INPUT = REPEATER.east();
  private static final short TICKS_TO_POWER = 20;

  public SignalCapacitorBoxPulseTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    helper.setBlock(CAPACITOR, RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get());
    capacitor(helper).setTicksToPower(TICKS_TO_POWER);
    helper.setBlock(REPEATER, Blocks.REPEATER.defaultBlockState()
        .setValue(RepeaterBlock.FACING, Direction.EAST));
    helper.setBlock(INPUT, Blocks.REDSTONE_BLOCK);

    helper.runAtTickTime(5, () -> {
      helper.assertTrue(isPowered(helper), "Capacitor should be powered by the pulse");
      helper.setBlock(INPUT, Blocks.AIR);
    });
    helper.runAtTickTime(15, () -> helper.assertTrue(isPowered(helper),
        "Capacitor should stay powered after the pulse ends"));
    helper.runAtTickTime(TICKS_TO_POWER + 20, () -> {
      helper.assertFalse(isPowered(helper), "Capacitor should stop once its time is over");
      var tag = capacitor(helper).saveWithoutMetadata(helper.getLevel().registryAccess());
      var ticksPowered = tag.getShortOr(CompoundTagKeys.TICKS_POWERED, (short) -1);
      helper.assertTrue(ticksPowered == 0, "Countdown should stop at zero, got " + ticksPowered);
      helper.succeed();
    });
  }

  static SignalCapacitorBoxBlockEntity capacitor(GameTestHelper helper) {
    return helper.getBlockEntity(CAPACITOR, SignalCapacitorBoxBlockEntity.class);
  }

  private static boolean isPowered(GameTestHelper helper) {
    return capacitor(helper).getRedstoneSignal(Direction.NORTH) > 0;
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Signal Capacitor Box Pulse");
  }
}
