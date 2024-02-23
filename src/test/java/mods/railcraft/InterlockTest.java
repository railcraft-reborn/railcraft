package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import mods.railcraft.api.signal.entity.SignalReceiverEntity;
import mods.railcraft.world.level.block.entity.signal.DistantSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalControllerBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalInterlockBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalReceiverBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class InterlockTest {

  private static BlockPos MASTER_LEVER = new BlockPos(1, 2, 3);
  private static BlockPos LEVER_1 = new BlockPos(6, 2, 1);
  private static BlockPos LEVER_2 = new BlockPos(5, 2, 1);
  private static BlockPos LEVER_3 = new BlockPos(4, 2, 1);


  @GameTest(template = "interlock", timeoutTicks = 200)
  public static void interlock(GameTestHelper helper) {
    setupTest(helper);
    helper.startSequence()
        .thenIdle(20)
        .thenExecute(() -> {
          helper.pullLever(LEVER_1);
          var distant1 = (DistantSignalBlockEntity) helper.getBlockEntity(new BlockPos(6, 3, 6));
          var distant2 = (DistantSignalBlockEntity) helper.getBlockEntity(new BlockPos(5, 3, 6));
          if (distant1.getPrimarySignalAspect() != SignalAspect.RED) {
            helper.fail("Distant 1 should be RED");
          }
          if (distant2.getPrimarySignalAspect() != SignalAspect.GREEN) {
            helper.fail("Distant 2 should be GREEN");
          }
        })
        .thenIdle(20)
        .thenExecute(() -> {
          helper.pullLever(LEVER_2);
          var distant2 = (DistantSignalBlockEntity) helper.getBlockEntity(new BlockPos(5, 3, 6));
          var distant3 = (DistantSignalBlockEntity) helper.getBlockEntity(new BlockPos(4, 3, 6));
          if (distant2.getPrimarySignalAspect() != SignalAspect.RED) {
            helper.fail("Distant 2 should be RED");
          }
          if (distant3.getPrimarySignalAspect() != SignalAspect.GREEN) {
            helper.fail("Distant 3 should be GREEN");
          }
        })
        .thenIdle(20)
        .thenExecute(() -> {
          helper.pullLever(LEVER_3);
          helper.pullLever(LEVER_1);
          var distant3 = (DistantSignalBlockEntity) helper.getBlockEntity(new BlockPos(4, 3, 6));
          var distant1 = (DistantSignalBlockEntity) helper.getBlockEntity(new BlockPos(6, 3, 6));
          if (distant3.getPrimarySignalAspect() != SignalAspect.RED) {
            helper.fail("Distant 3 should be RED");
          }
          if (distant1.getPrimarySignalAspect() != SignalAspect.GREEN) {
            helper.fail("Distant 1 should be GREEN");
          }
        })
        .thenIdle(20)
        .thenExecute(() -> {
          helper.pullLever(MASTER_LEVER);
          var distant1 = (DistantSignalBlockEntity) helper.getBlockEntity(new BlockPos(6, 3, 6));
          var distant2 = (DistantSignalBlockEntity) helper.getBlockEntity(new BlockPos(5, 3, 6));
          var distant3 = (DistantSignalBlockEntity) helper.getBlockEntity(new BlockPos(4, 3, 6));
          if (distant1.getPrimarySignalAspect() != SignalAspect.RED) {
            helper.fail("Distant 1 should be RED");
          }
          if (distant2.getPrimarySignalAspect() != SignalAspect.RED) {
            helper.fail("Distant 2 should be RED");
          }
          if (distant3.getPrimarySignalAspect() != SignalAspect.RED) {
            helper.fail("Distant 3 should be RED");
          }
        })
        .thenIdle(20)
        .thenExecute(() -> {
          helper.pullLever(MASTER_LEVER);
          var distant1 = (DistantSignalBlockEntity) helper.getBlockEntity(new BlockPos(6, 3, 6));
          var distant2 = (DistantSignalBlockEntity) helper.getBlockEntity(new BlockPos(5, 3, 6));
          var distant3 = (DistantSignalBlockEntity) helper.getBlockEntity(new BlockPos(4, 3, 6));
          if (distant1.getPrimarySignalAspect() != SignalAspect.GREEN) {
            helper.fail("Distant 1 should be GREEN");
          }
          if (distant2.getPrimarySignalAspect() != SignalAspect.RED) {
            helper.fail("Distant 2 should be RED");
          }
          if (distant3.getPrimarySignalAspect() != SignalAspect.RED) {
            helper.fail("Distant 3 should be RED");
          }
        })
        .thenSucceed();
  }

  private static void setupTest(GameTestHelper helper) {
    var controller1 = (SignalControllerBoxBlockEntity) helper.getBlockEntity(new BlockPos(6, 2, 2));
    var interlock1 = (SignalInterlockBoxBlockEntity) helper.getBlockEntity(new BlockPos(6, 2, 4));
    link(controller1, interlock1);
    var controller2 = (SignalControllerBoxBlockEntity) helper.getBlockEntity(new BlockPos(5, 2, 2));
    var interlock2 = (SignalInterlockBoxBlockEntity) helper.getBlockEntity(new BlockPos(5, 2, 4));
    link(controller2, interlock2);
    var controller3 = (SignalControllerBoxBlockEntity) helper.getBlockEntity(new BlockPos(4, 2, 2));
    var interlock3 = (SignalInterlockBoxBlockEntity) helper.getBlockEntity(new BlockPos(4, 2, 4));
    link(controller3, interlock3);

    var distant1 = (DistantSignalBlockEntity) helper.getBlockEntity(new BlockPos(6, 3, 6));
    var distant2 = (DistantSignalBlockEntity) helper.getBlockEntity(new BlockPos(5, 3, 6));
    var distant3 = (DistantSignalBlockEntity) helper.getBlockEntity(new BlockPos(4, 3, 6));
    link(interlock1, distant1);
    link(interlock2, distant2);
    link(interlock3, distant3);

    var controller4 = (SignalControllerBoxBlockEntity) helper.getBlockEntity(new BlockPos(1, 2, 4));
    var receiver1 = (SignalReceiverBoxBlockEntity) helper.getBlockEntity(new BlockPos(3, 2, 4));
    link(controller4, receiver1);
  }

  private static void link(SignalControllerEntity target, SignalReceiverEntity peer) {
    var controller = target.getSignalController();
    controller.startLinking();
    controller.addPeer(peer);
    controller.stopLinking();
  }
}
