package mods.railcraft;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RepeaterBlock;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class SignalCapacitorBoxTest {

  private static final BlockPos CAPACITOR = new BlockPos(3, 2, 3);
  private static final BlockPos REPEATER = CAPACITOR.east();
  private static final BlockPos INPUT = REPEATER.east();
  private static final short TICKS_TO_POWER = 20;

  /**
   * A short pulse must power the capacitor for the configured time only, and its countdown must
   * then stop instead of running on until it wraps around and powers the capacitor again.
   */
  @GameTest(template = "platform_7x7")
  public static void signalCapacitorBoxPulse(GameTestHelper helper) {
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
      helper.assertTrue(tag.getShort(CompoundTagKeys.TICKS_POWERED) == 0,
          "Countdown should stop at zero, got " + tag.getShort(CompoundTagKeys.TICKS_POWERED));
      helper.succeed();
    });
  }

  /**
   * The duration can't go below zero, nor above the longest whole number of seconds a short holds.
   */
  @GameTest(template = "platform_7x7")
  public static void signalCapacitorBoxDurationLimits(GameTestHelper helper) {
    helper.setBlock(CAPACITOR, RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get());
    var capacitor = capacitor(helper);
    capacitor.setTicksToPower((short) -20);
    helper.assertTrue(capacitor.getTicksToPower() == 0,
        "Negative duration should be clamped to zero, got " + capacitor.getTicksToPower());
    capacitor.setTicksToPower(Short.MAX_VALUE);
    helper.assertTrue(
        capacitor.getTicksToPower() == SignalCapacitorBoxBlockEntity.MAX_TICKS_TO_POWER,
        "Duration should be clamped to whole seconds, got " + capacitor.getTicksToPower());
    helper.succeed();
  }

  private static SignalCapacitorBoxBlockEntity capacitor(GameTestHelper helper) {
    return helper.getBlockEntity(CAPACITOR);
  }

  private static boolean isPowered(GameTestHelper helper) {
    return capacitor(helper).getRedstoneSignal(Direction.NORTH) > 0;
  }
}
