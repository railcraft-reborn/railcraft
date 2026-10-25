package mods.railcraft.data.gametest.instances.locomotive;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class LocomotiveKeepsFacingThroughCurveTestInstance extends GameTestInstance {

  public static final MapCodec<LocomotiveKeepsFacingThroughCurveTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(LocomotiveKeepsFacingThroughCurveTestInstance::new);

  public LocomotiveKeepsFacingThroughCurveTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    var loco = LocomotiveTestHelper.spawnOnCurvedLine(helper);
    var speeds = new double[] {0.1, 0.2, 0.4};
    int[] run = {0};
    LocomotiveTestHelper.push(loco, speeds[0]);
    helper.onEachTick(() -> {
      LocomotiveTestHelper.keepPushing(loco, speeds[run[0]]);
      if (!LocomotiveTestHelper.at(helper, loco, LocomotiveTestHelper.AFTER_CURVE)) {
        return;
      }
      // The yaw of an unflipped cart points against its motion, which is east after the curve
      helper.assertFalse(loco.isFlipped(),
          "Locomotive should not flip at speed " + speeds[run[0]]);
      helper.assertTrue(Mth.degreesDifferenceAbs(loco.getYRot(), 180) < 1,
          "Locomotive yaw should point west at speed %s, got %s"
              .formatted(speeds[run[0]], loco.getYRot()));
      if (++run[0] == speeds.length) {
        helper.succeed();
        return;
      }
      loco.snapTo(Vec3.atBottomCenterOf(helper.absolutePos(LocomotiveTestHelper.START)), 0, 0);
      loco.setFlipped(false);
      LocomotiveTestHelper.push(loco, speeds[run[0]]);
    });
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Locomotive Keeps Facing Through Curve");
  }
}
