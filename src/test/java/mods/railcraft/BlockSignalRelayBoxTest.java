package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.item.SignalBlockSurveyorItem;
import mods.railcraft.world.level.block.entity.signal.BlockSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.BlockSignalRelayBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class BlockSignalRelayBoxTest {

  private static final BlockPos BLOCK_SIGNAL_LEFT = new BlockPos(5, 2, 3);
  private static final BlockPos BLOCK_SIGNAL_RIGHT = new BlockPos(1, 2, 5);
  private static final BlockPos BLOCK_SIGNAL_RELAY_BOX = new BlockPos(2, 2, 1);


  @GameTest(template = "block_signal_relay_box")
  public static void blockSignalRelayBoxNoCart(GameTestHelper helper) {
    helper.succeedWhen(() -> {
      if (helper.getBlockEntity(BLOCK_SIGNAL_LEFT) instanceof BlockSignalBlockEntity left &&
          helper.getBlockEntity(BLOCK_SIGNAL_RIGHT) instanceof BlockSignalBlockEntity right &&
          helper.getBlockEntity(BLOCK_SIGNAL_RELAY_BOX) instanceof BlockSignalRelayBoxBlockEntity box) {
        SignalBlockSurveyorItem.tryLinking(left, box);
        SignalBlockSurveyorItem.tryLinking(right, box);
        if (left.getPrimarySignalAspect().equals(SignalAspect.GREEN) &&
            right.getPrimarySignalAspect().equals(SignalAspect.GREEN)) {
          helper.succeed();
        }
      } else {
        helper.fail("Expected Green on both Signal Block");
      }
    });
  }

  @GameTest(template = "block_signal_relay_box")
  public static void blockSignalRelayBoxWithCart(GameTestHelper helper) {
    helper.spawn(EntityType.MINECART, new BlockPos(4, 2, 2));
    helper.succeedWhen(() -> {
      if (helper.getBlockEntity(BLOCK_SIGNAL_LEFT) instanceof BlockSignalBlockEntity left &&
          helper.getBlockEntity(BLOCK_SIGNAL_RIGHT) instanceof BlockSignalBlockEntity right &&
          helper.getBlockEntity(BLOCK_SIGNAL_RELAY_BOX) instanceof BlockSignalRelayBoxBlockEntity box) {
        SignalBlockSurveyorItem.tryLinking(left, box);
        SignalBlockSurveyorItem.tryLinking(right, box);
        if (left.getPrimarySignalAspect().equals(SignalAspect.RED) &&
            right.getPrimarySignalAspect().equals(SignalAspect.RED)) {
          helper.succeed();
        }
      } else {
        helper.fail("Expected Red on both Signal Block");
      }
    });
  }

  private static final BlockPos BLOCK_SIGNAL_LEFT_COMPLEX = new BlockPos(7, 2, 1);
  private static final BlockPos BLOCK_SIGNAL_RIGHT_COMPLEX = new BlockPos(1, 2, 7);
  private static final BlockPos BLOCK_SIGNAL_RELAY_BOX_1_COMPLEX = new BlockPos(6, 2, 4);
  private static final BlockPos BLOCK_SIGNAL_RELAY_BOX_2_COMPLEX = new BlockPos(2, 2, 3);
  private static final BlockPos BLOCK_SIGNAL_RELAY_BOX_3_COMPLEX = new BlockPos(4, 2, 6);

  @GameTest(template = "block_signal_relay_box_complex")
  public static void blockSignalRelayBoxComplexNoCart(GameTestHelper helper) {
    helper.succeedWhen(() -> {
      if (helper.getBlockEntity(BLOCK_SIGNAL_LEFT_COMPLEX) instanceof BlockSignalBlockEntity left &&
          helper.getBlockEntity(BLOCK_SIGNAL_RIGHT_COMPLEX) instanceof BlockSignalBlockEntity right &&
          helper.getBlockEntity(BLOCK_SIGNAL_RELAY_BOX_1_COMPLEX) instanceof BlockSignalRelayBoxBlockEntity box1 &&
          helper.getBlockEntity(BLOCK_SIGNAL_RELAY_BOX_2_COMPLEX) instanceof BlockSignalRelayBoxBlockEntity box2 &&
          helper.getBlockEntity(BLOCK_SIGNAL_RELAY_BOX_3_COMPLEX) instanceof BlockSignalRelayBoxBlockEntity box3) {
        SignalBlockSurveyorItem.tryLinking(left, box1);
        SignalBlockSurveyorItem.tryLinking(box1, box2);
        SignalBlockSurveyorItem.tryLinking(box2, box3);
        SignalBlockSurveyorItem.tryLinking(right, box3);
        if (left.getPrimarySignalAspect().equals(SignalAspect.GREEN) &&
            right.getPrimarySignalAspect().equals(SignalAspect.GREEN)) {
          helper.succeed();
        }
      } else {
        helper.fail("Expected Green on both Signal Block");
      }
    });
  }

  @GameTest(template = "block_signal_relay_box_complex")
  public static void blockSignalRelayBoxComplexWithCart(GameTestHelper helper) {
    helper.spawn(EntityType.MINECART, new BlockPos(6, 2, 2));
    helper.succeedWhen(() -> {
      if (helper.getBlockEntity(BLOCK_SIGNAL_LEFT_COMPLEX) instanceof BlockSignalBlockEntity left &&
          helper.getBlockEntity(BLOCK_SIGNAL_RIGHT_COMPLEX) instanceof BlockSignalBlockEntity right &&
          helper.getBlockEntity(BLOCK_SIGNAL_RELAY_BOX_1_COMPLEX) instanceof BlockSignalRelayBoxBlockEntity box1 &&
          helper.getBlockEntity(BLOCK_SIGNAL_RELAY_BOX_2_COMPLEX) instanceof BlockSignalRelayBoxBlockEntity box2 &&
          helper.getBlockEntity(BLOCK_SIGNAL_RELAY_BOX_3_COMPLEX) instanceof BlockSignalRelayBoxBlockEntity box3) {
        SignalBlockSurveyorItem.tryLinking(left, box1);
        SignalBlockSurveyorItem.tryLinking(box1, box2);
        SignalBlockSurveyorItem.tryLinking(box2, box3);
        SignalBlockSurveyorItem.tryLinking(right, box3);
        if (left.getPrimarySignalAspect().equals(SignalAspect.RED) &&
            right.getPrimarySignalAspect().equals(SignalAspect.RED)) {
          helper.succeed();
        }
      } else {
        helper.fail("Expected Red on both Signal Block");
      }
    });
  }
}
