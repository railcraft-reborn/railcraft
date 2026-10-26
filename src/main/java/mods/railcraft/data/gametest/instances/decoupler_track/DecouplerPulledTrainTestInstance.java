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
import net.minecraft.world.phys.Vec3;

public class DecouplerPulledTrainTestInstance extends GameTestInstance {

  public static final MapCodec<DecouplerPulledTrainTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(DecouplerPulledTrainTestInstance::new);

  public DecouplerPulledTrainTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    DecouplerTrackTestHelper.placeDetectorOnTankCartsSide(helper);
    var train = DecouplerTrackTestHelper.spawnTrain(helper, 3.75);
    helper.onEachTick(() -> {
      if (train.locoLinked()) {
        train.loco().setDeltaMovement(0, 0, -DecouplerTrackTestHelper.SPEED);
      } else {
        // Past the decoupler the platform ends, so the locomotive is moved on by hand, while the
        // tank carts keep rolling after it
        train.loco().setDeltaMovement(Vec3.ZERO);
        train.loco().setPos(train.loco().getX(), train.first().getY(),
            train.loco().getZ() - DecouplerTrackTestHelper.SPEED);
        train.first().setDeltaMovement(0, 0, -DecouplerTrackTestHelper.SPEED);
      }
      if (DecouplerTrackTestHelper.relativeZ(helper, train.first())
          < DecouplerTrackTestHelper.DECOUPLER.getZ() - 0.1) {
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
    return Component.literal("Decoupler Pulled Train");
  }
}
