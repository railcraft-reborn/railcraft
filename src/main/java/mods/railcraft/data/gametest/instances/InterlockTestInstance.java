package mods.railcraft.data.gametest.instances;

import com.mojang.serialization.MapCodec;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import mods.railcraft.api.signal.entity.SignalReceiverEntity;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.world.level.block.entity.signal.DistantSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalControllerBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalInterlockBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalReceiverBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class InterlockTestInstance extends GameTestInstance {

  public static final MapCodec<InterlockTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(InterlockTestInstance::new);

  private static final BlockPos MASTER_LEVER = new BlockPos(1, 1, 3);
  private static final BlockPos LEVER_1 = new BlockPos(6, 1, 1);
  private static final BlockPos LEVER_2 = new BlockPos(5, 1, 1);
  private static final BlockPos LEVER_3 = new BlockPos(4, 1, 1);

  public InterlockTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    setupTest(helper);
    helper.startSequence()
        .thenIdle(20)
        .thenExecute(() -> {
          helper.pullLever(LEVER_1);
          var distant1 = helper.getBlockEntity(new BlockPos(6, 2, 6), DistantSignalBlockEntity.class);
          var distant2 = helper.getBlockEntity(new BlockPos(5, 2, 6), DistantSignalBlockEntity.class);
          if (distant1.getPrimarySignalAspect() != SignalAspect.RED) {
            helper.fail(Component.literal("Distant 1 should be RED"));
          }
          if (distant2.getPrimarySignalAspect() != SignalAspect.GREEN) {
            helper.fail(Component.literal("Distant 2 should be GREEN"));
          }
        })
        .thenIdle(20)
        .thenExecute(() -> {
          helper.pullLever(LEVER_2);
          var distant2 = helper.getBlockEntity(new BlockPos(5, 2, 6), DistantSignalBlockEntity.class);
          var distant3 = helper.getBlockEntity(new BlockPos(4, 2, 6), DistantSignalBlockEntity.class);
          if (distant2.getPrimarySignalAspect() != SignalAspect.RED) {
            helper.fail(Component.literal("Distant 2 should be RED"));
          }
          if (distant3.getPrimarySignalAspect() != SignalAspect.GREEN) {
            helper.fail(Component.literal("Distant 3 should be GREEN"));
          }
        })
        .thenIdle(20)
        .thenExecute(() -> {
          helper.pullLever(LEVER_3);
          helper.pullLever(LEVER_1);
          var distant3 = helper.getBlockEntity(new BlockPos(4, 2, 6), DistantSignalBlockEntity.class);
          var distant1 = helper.getBlockEntity(new BlockPos(6, 2, 6), DistantSignalBlockEntity.class);
          if (distant3.getPrimarySignalAspect() != SignalAspect.RED) {
            helper.fail(Component.literal("Distant 3 should be RED"));
          }
          if (distant1.getPrimarySignalAspect() != SignalAspect.GREEN) {
            helper.fail(Component.literal("Distant 1 should be GREEN"));
          }
        })
        .thenIdle(20)
        .thenExecute(() -> {
          helper.pullLever(MASTER_LEVER);
          var distant1 = helper.getBlockEntity(new BlockPos(6, 2, 6), DistantSignalBlockEntity.class);
          var distant2 = helper.getBlockEntity(new BlockPos(5, 2, 6), DistantSignalBlockEntity.class);
          var distant3 = helper.getBlockEntity(new BlockPos(4, 2, 6), DistantSignalBlockEntity.class);
          if (distant1.getPrimarySignalAspect() != SignalAspect.RED) {
            helper.fail(Component.literal("Distant 1 should be RED"));
          }
          if (distant2.getPrimarySignalAspect() != SignalAspect.RED) {
            helper.fail(Component.literal("Distant 2 should be RED"));
          }
          if (distant3.getPrimarySignalAspect() != SignalAspect.RED) {
            helper.fail(Component.literal("Distant 3 should be RED"));
          }
        })
        .thenIdle(20)
        .thenExecute(() -> {
          helper.pullLever(MASTER_LEVER);
          var distant1 = helper.getBlockEntity(new BlockPos(6, 2, 6), DistantSignalBlockEntity.class);
          var distant2 = helper.getBlockEntity(new BlockPos(5, 2, 6), DistantSignalBlockEntity.class);
          var distant3 = helper.getBlockEntity(new BlockPos(4, 2, 6), DistantSignalBlockEntity.class);
          if (distant1.getPrimarySignalAspect() != SignalAspect.GREEN) {
            helper.fail(Component.literal("Distant 1 should be GREEN"));
          }
          if (distant2.getPrimarySignalAspect() != SignalAspect.RED) {
            helper.fail(Component.literal("Distant 2 should be RED"));
          }
          if (distant3.getPrimarySignalAspect() != SignalAspect.RED) {
            helper.fail(Component.literal("Distant 3 should be RED"));
          }
        })
        .thenSucceed();
  }

  private static void setupTest(GameTestHelper helper) {
    var controller1 = helper.getBlockEntity(new BlockPos(6, 1, 2), SignalControllerBoxBlockEntity.class);
    var interlock1 = helper.getBlockEntity(new BlockPos(6, 1, 4), SignalInterlockBoxBlockEntity.class);
    link(controller1, interlock1);
    var controller2 = helper.getBlockEntity(new BlockPos(5, 1, 2), SignalControllerBoxBlockEntity.class);
    var interlock2 = helper.getBlockEntity(new BlockPos(5, 1, 4), SignalInterlockBoxBlockEntity.class);
    link(controller2, interlock2);
    var controller3 = helper.getBlockEntity(new BlockPos(4, 1, 2), SignalControllerBoxBlockEntity.class);
    var interlock3 = helper.getBlockEntity(new BlockPos(4, 1, 4), SignalInterlockBoxBlockEntity.class);
    link(controller3, interlock3);

    var distant1 = helper.getBlockEntity(new BlockPos(6, 2, 6), DistantSignalBlockEntity.class);
    var distant2 = helper.getBlockEntity(new BlockPos(5, 2, 6), DistantSignalBlockEntity.class);
    var distant3 = helper.getBlockEntity(new BlockPos(4, 2, 6), DistantSignalBlockEntity.class);
    link(interlock1, distant1);
    link(interlock2, distant2);
    link(interlock3, distant3);

    var controller4 = helper.getBlockEntity(new BlockPos(1, 1, 4), SignalControllerBoxBlockEntity.class);
    var receiver1 = helper.getBlockEntity(new BlockPos(3, 1, 4), SignalReceiverBoxBlockEntity.class);
    link(controller4, receiver1);
  }

  private static void link(SignalControllerEntity target, SignalReceiverEntity peer) {
    var controller = target.getSignalController();
    controller.startLinking();
    controller.addPeer(peer);
    controller.stopLinking();
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Interlock Test");
  }
}
