package mods.railcraft.data.gametest.instances.dual_block_signal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import mods.railcraft.api.signal.entity.SignalReceiverEntity;
import mods.railcraft.world.level.block.entity.signal.DualBlockSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalControllerBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class DualBlockSignalSecondary extends GameTestInstance {

  public static final MapCodec<DualBlockSignalSecondary> CODEC =
      RecordCodecBuilder.mapCodec(instance -> instance.group(
          Codec.BOOL.fieldOf("changeControllerAspect").forGetter(t -> t.changeControllerAspect),
          TestData.CODEC.forGetter(t -> t.info)
      ).apply(instance, DualBlockSignalSecondary::new));

  private static final BlockPos DUAL_BLOCK_SIGNAL = new BlockPos(1, 1, 1);
  private static final BlockPos SIGNAL_CONTROLLER_BOX = new BlockPos(5, 1, 1);
  private final boolean changeControllerAspect;

  public DualBlockSignalSecondary(boolean changeControllerAspect,
      TestData<Holder<TestEnvironmentDefinition>> info) {
    super(info);
    this.changeControllerAspect = changeControllerAspect;
  }

  @Override
  public void run(GameTestHelper helper) {
    helper.succeedWhen(() -> {
      var dual = helper.getBlockEntity(DUAL_BLOCK_SIGNAL, DualBlockSignalBlockEntity.class);
      var controller = helper.getBlockEntity(SIGNAL_CONTROLLER_BOX, SignalControllerBoxBlockEntity.class);
      linkTwoController(controller, dual);

      if (changeControllerAspect) {
        controller.setDefaultAspect(SignalAspect.YELLOW);
      }

      var expectedAspect = this.changeControllerAspect ? SignalAspect.YELLOW : SignalAspect.GREEN;

      var secondarySignalAspect = dual.getSecondarySignalAspect();
      if (controller.getSignalController().aspect().equals(secondarySignalAspect) &&
          secondarySignalAspect.equals(expectedAspect)) {
        helper.succeed();
      } else {
        if (changeControllerAspect) {
          helper.fail(Component.literal("Expected Yellow on Block Signal and Signal Controller Box"));
        } else {
          helper.fail(Component.literal("Expected Green on Block Signal and Signal Controller Box"));
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

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    if (this.changeControllerAspect) {
      return Component.literal("Dual Block Signal Secondary Yellow");
    }
    return Component.literal("Dual Block Signal Secondary Green");
  }
}
