package mods.railcraft.data.gametest.instances.locomotive;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
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
 * Issue #339, a placed locomotive used to be at right angles with the track, so pushing it turned
 * it either way.
 */
public class PlacedLocomotiveFacesAlongTrackTestInstance extends GameTestInstance {

  public static final MapCodec<PlacedLocomotiveFacesAlongTrackTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(PlacedLocomotiveFacesAlongTrackTestInstance::new);

  public PlacedLocomotiveFacesAlongTrackTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    LocomotiveTestHelper.buildStraightLine(helper);
    // Looking mostly east, a bit north
    var loco = LocomotiveTestHelper.placeLocomotive(helper, new BlockPos(3, 1, 3), 240);
    helper.assertTrue(Mth.degreesDifferenceAbs(loco.getYRot(), 0) < 1,
        "Placed locomotive should face east, got " + loco.getYRot());
    helper.runAtTickTime(5, () -> loco.setDeltaMovement(0.2, 0, 0));
    helper.runAtTickTime(10, () -> {
      helper.assertTrue(Mth.degreesDifferenceAbs(loco.getYRot(), 0) < 1,
          "Locomotive pushed forward should still face east, got " + loco.getYRot());
      loco.setDeltaMovement(-0.2, 0, 0);
    });
    helper.runAtTickTime(15, () -> {
      helper.assertTrue(Mth.degreesDifferenceAbs(loco.getYRot(), 0) < 1,
          "Locomotive pushed back should still face east, got " + loco.getYRot());
      helper.succeed();
    });
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Placed Locomotive Faces Along Track");
  }
}
