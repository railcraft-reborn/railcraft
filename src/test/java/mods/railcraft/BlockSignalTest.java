package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.item.SignalBlockSurveyorItem;
import mods.railcraft.world.level.block.entity.signal.BlockSignalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class BlockSignalTest {

  private static final BlockPos BLOCK_SIGNAL_LEFT = new BlockPos(7, 2, 3);
  private static final BlockPos BLOCK_SIGNAL_RIGHT = new BlockPos(2, 2, 3);


  @GameTest(template = "block_signal")
  public static void blockSignalNoCart(GameTestHelper helper) {
    helper.succeedWhen(() -> {
      if (helper.getBlockEntity(BLOCK_SIGNAL_LEFT) instanceof BlockSignalBlockEntity left &&
          helper.getBlockEntity(BLOCK_SIGNAL_RIGHT) instanceof BlockSignalBlockEntity right) {
        SignalBlockSurveyorItem.tryLinking(left, right);
        if (left.getPrimarySignalAspect().equals(SignalAspect.GREEN) &&
            right.getPrimarySignalAspect().equals(SignalAspect.GREEN)) {
          helper.succeed();
        } else {
          helper.fail("Expected Green on both Signal Block");
        }
      }
    });
  }

  @GameTest(template = "block_signal")
  public static void blockSignalWithCart(GameTestHelper helper) {
    helper.spawn(EntityType.MINECART, new BlockPos(5, 2, 2));
    helper.succeedWhen(() -> {
      if (helper.getBlockEntity(BLOCK_SIGNAL_LEFT) instanceof BlockSignalBlockEntity left &&
          helper.getBlockEntity(BLOCK_SIGNAL_RIGHT) instanceof BlockSignalBlockEntity right) {
        SignalBlockSurveyorItem.tryLinking(left, right);
        if (left.getPrimarySignalAspect().equals(SignalAspect.RED) &&
            right.getPrimarySignalAspect().equals(SignalAspect.RED)) {
          helper.succeed();
        } else {
          helper.fail("Expected Red on both Signal Block");
        }
      }
    });
  }
}
