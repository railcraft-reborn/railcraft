package mods.railcraft.data.gametest.instances.decoupler_track;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class DecouplerPushedTrainTestInstance extends GameTestInstance {

  public static final MapCodec<DecouplerPushedTrainTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(DecouplerPushedTrainTestInstance::new);

  public DecouplerPushedTrainTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    DecouplerTrackTestHelper.placeDetectorOnTankCartsSide(helper);
    var train = DecouplerTrackTestHelper.spawnTrain(helper, 0.6);
    helper.onEachTick(() -> {
      train.loco().setDeltaMovement(0, 0, DecouplerTrackTestHelper.SPEED);
      if (DecouplerTrackTestHelper.relativeZ(helper, train.loco())
          > DecouplerTrackTestHelper.DECOUPLER.getZ() + 1.1) {
        train.assertOnlyLocoDecoupled(helper);
      }
    });
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Decoupler Pushed Train");
  }
}
