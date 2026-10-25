package mods.railcraft.data.gametest.instances.locomotive;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

/**
 * Issue #339, reversing a locomotive with a crowbar, standing or moving, must turn it around for
 * good.
 */
public class LocomotiveStaysReversedTestInstance extends GameTestInstance {

  public static final MapCodec<LocomotiveStaysReversedTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(LocomotiveStaysReversedTestInstance::new);

  public LocomotiveStaysReversedTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    LocomotiveTestHelper.buildStraightLine(helper);
    var loco = helper.spawn(RailcraftEntityTypes.STEAM_LOCOMOTIVE.get(), new BlockPos(3, 1, 3));
    float[] yaw = {Float.NaN};
    helper.runAtTickTime(5, () -> {
      yaw[0] = loco.getYRot();
      loco.reverse();
    });
    helper.runAtTickTime(15, () -> {
      helper.assertTrue(Mth.degreesDifferenceAbs(yaw[0] + 180, loco.getYRot()) < 1,
          "Standing locomotive should stay reversed, was %s, now %s"
              .formatted(yaw[0], loco.getYRot()));
      yaw[0] = loco.getYRot();
      loco.setDeltaMovement(0.2, 0, 0);
    });
    helper.runAtTickTime(18, () -> {
      helper.assertTrue(Mth.degreesDifferenceAbs(yaw[0], loco.getYRot()) < 1,
          "Pushing the locomotive should not turn it, was %s, now %s"
              .formatted(yaw[0], loco.getYRot()));
      loco.reverse();
    });
    helper.runAtTickTime(21, () -> {
      helper.assertTrue(Mth.degreesDifferenceAbs(yaw[0] + 180, loco.getYRot()) < 1,
          "Moving locomotive should stay reversed, was %s, now %s"
              .formatted(yaw[0], loco.getYRot()));
      helper.assertTrue(loco.getDeltaMovement().x() < 0,
          "Moving locomotive should go the other way");
      helper.succeed();
    });
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Locomotive Stays Reversed");
  }
}
