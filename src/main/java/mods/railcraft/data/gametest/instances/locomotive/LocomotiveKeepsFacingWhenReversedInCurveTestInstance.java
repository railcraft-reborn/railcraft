package mods.railcraft.data.gametest.instances.locomotive;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import net.minecraft.util.Mth;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * Issue #339, a locomotive pushed back while on a curve used to turn around.
 */
public class LocomotiveKeepsFacingWhenReversedInCurveTestInstance extends GameTestInstance {

  public static final MapCodec<LocomotiveKeepsFacingWhenReversedInCurveTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(LocomotiveKeepsFacingWhenReversedInCurveTestInstance::new);

  public LocomotiveKeepsFacingWhenReversedInCurveTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    var loco = LocomotiveTestHelper.spawnOnCurvedLine(helper);
    float[] yawBefore = {Float.NaN};
    boolean[] reversed = {false};
    LocomotiveTestHelper.push(loco, 0.2);
    helper.onEachTick(() -> {
      LocomotiveTestHelper.keepPushing(loco, 0.2);
      if (!reversed[0] && Float.isNaN(yawBefore[0])
          && LocomotiveTestHelper.at(helper, loco, LocomotiveTestHelper.BEFORE_CURVE)) {
        yawBefore[0] = loco.getYRot();
      } else if (!reversed[0]
          && LocomotiveTestHelper.at(helper, loco, LocomotiveTestHelper.CURVE)) {
        reversed[0] = true;
        loco.setDeltaMovement(loco.getDeltaMovement().scale(-1));
      } else if (reversed[0]
          && LocomotiveTestHelper.at(helper, loco, LocomotiveTestHelper.BEFORE_CURVE)) {
        helper.assertTrue(Mth.degreesDifferenceAbs(yawBefore[0], loco.getYRot()) < 1,
            "Locomotive should still face the same way, was %s, now %s"
                .formatted(yawBefore[0], loco.getYRot()));
        helper.succeed();
      }
    });
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Locomotive Keeps Facing When Reversed In Curve");
  }
}
