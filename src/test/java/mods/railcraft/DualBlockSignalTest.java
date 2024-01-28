package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import mods.railcraft.api.signal.entity.SignalReceiverEntity;
import mods.railcraft.world.item.SignalBlockSurveyorItem;
import mods.railcraft.world.level.block.entity.signal.BlockSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.DualBlockSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalControllerBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class DualBlockSignalTest {

  private static final BlockPos DUAL_BLOCK_SIGNAL = new BlockPos(1, 2, 1);
  private static final BlockPos BLOCK_SIGNAL = new BlockPos(1, 2, 3);
  private static final BlockPos SIGNAL_CONTROLLER_BOX = new BlockPos(5, 2, 1);

  @GameTest(template = "dual_block_signal")
  public static void dualBlockSignalPrimaryNoCart(GameTestHelper helper) {
    helper.succeedWhen(() -> {
      if (helper.getBlockEntity(DUAL_BLOCK_SIGNAL) instanceof DualBlockSignalBlockEntity dual &&
          helper.getBlockEntity(BLOCK_SIGNAL) instanceof BlockSignalBlockEntity single) {
        SignalBlockSurveyorItem.tryLinking(dual, single);

        var primarySignalAspect = dual.getPrimarySignalAspect();
        if (single.getPrimarySignalAspect().equals(primarySignalAspect) &&
            primarySignalAspect.equals(SignalAspect.GREEN)) {
          helper.succeed();
        } else {
          helper.fail("Expected Green on Block Signal and Dual Block Signal");
        }
      }
    });
  }

  @GameTest(template = "dual_block_signal")
  public static void dualBlockSignalPrimaryWithCart(GameTestHelper helper) {
    helper.spawn(EntityType.MINECART, new BlockPos(1, 2, 2));
    helper.succeedWhen(() -> {
      if (helper.getBlockEntity(DUAL_BLOCK_SIGNAL) instanceof DualBlockSignalBlockEntity dual &&
          helper.getBlockEntity(BLOCK_SIGNAL) instanceof BlockSignalBlockEntity single) {
        SignalBlockSurveyorItem.tryLinking(dual, single);

        var primarySignalAspect = dual.getPrimarySignalAspect();
        if (single.getPrimarySignalAspect().equals(primarySignalAspect) &&
            primarySignalAspect.equals(SignalAspect.RED)) {
          helper.succeed();
        } else {
          helper.fail("Expected Red on Block Signal and Dual Block Signal");
        }
      }
    });
  }

  @GameTest(template = "dual_block_signal")
  public static void dualBlockSignalSecondaryGreen(GameTestHelper helper) {
    helper.succeedWhen(() -> {
      if (helper.getBlockEntity(DUAL_BLOCK_SIGNAL) instanceof DualBlockSignalBlockEntity dual &&
          helper.getBlockEntity(SIGNAL_CONTROLLER_BOX) instanceof SignalControllerBoxBlockEntity controller) {
        linkTwoController(controller, dual);

        var secondarySignalAspect = dual.getSecondarySignalAspect();
        if (controller.getSignalController().aspect().equals(secondarySignalAspect) &&
            secondarySignalAspect.equals(SignalAspect.GREEN)) {
          helper.succeed();
        } else {
          helper.fail("Expected Green on Block Signal and Signal Controller Box");
        }
      }
    });
  }

  @GameTest(template = "dual_block_signal")
  public static void dualBlockSignalSecondaryYellow(GameTestHelper helper) {
    helper.succeedWhen(() -> {
      if (helper.getBlockEntity(DUAL_BLOCK_SIGNAL) instanceof DualBlockSignalBlockEntity dual &&
          helper.getBlockEntity(SIGNAL_CONTROLLER_BOX) instanceof SignalControllerBoxBlockEntity controller) {
        linkTwoController(controller, dual);

        controller.setDefaultAspect(SignalAspect.YELLOW);

        var secondarySignalAspect = dual.getSecondarySignalAspect();
        if (controller.getSignalController().aspect().equals(secondarySignalAspect) &&
            secondarySignalAspect.equals(SignalAspect.YELLOW)) {
          helper.succeed();
        } else {
          helper.fail("Expected Yellow on Block Signal and Signal Controller Box");
        }
      }
    });
  }

  private static boolean linkTwoController(SignalControllerEntity target, SignalReceiverEntity peer) {
    target.getSignalController().startLinking();
    var success = target.getSignalController().addPeer(peer);
    target.getSignalController().stopLinking();
    return success;
  }
}
