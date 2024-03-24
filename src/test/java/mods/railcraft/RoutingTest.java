package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.vehicle.locomotive.CreativeLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class RoutingTest {

  private static final BlockPos SPAWN_POINT_A = new BlockPos(3, 2, 1);
  private static final BlockPos SPAWN_POINT_B = new BlockPos(5, 2, 1);
  private static final BlockPos SPAWN_POINT_A_B = new BlockPos(7, 2, 1);
  private static final BlockPos SPAWN_POINT_A_C = new BlockPos(9, 2, 1);

  private static final BlockPos TRAIN_A = new BlockPos(12, 2, 16);
  private static final BlockPos TRAIN_B = new BlockPos(12, 2, 5);
  private static final BlockPos TRAIN_A_B = new BlockPos(12, 2, 12);
  private static final BlockPos TRAIN_A_C = new BlockPos(12, 2, 14);

  @GameTest(template = "complex_routing", timeoutTicks = 300)
  public static void complexRouting(GameTestHelper helper) {
    var train_A = spawnNewLocomotive(helper, SPAWN_POINT_A, "A");
    var train_B = spawnNewLocomotive(helper, SPAWN_POINT_B, "B");
    var train_A_B = spawnNewLocomotive(helper, SPAWN_POINT_A_B, "A/B");
    var train_A_C = spawnNewLocomotive(helper, SPAWN_POINT_A_C, "A/C");

    helper.runAtTickTime(20, () -> {
      train_A.setMode(Locomotive.Mode.RUNNING);
    });
    helper.runAtTickTime(60, () -> {
      train_B.setMode(Locomotive.Mode.RUNNING);
    });
    helper.runAtTickTime(100, () -> {
      train_A_B.setMode(Locomotive.Mode.RUNNING);
    });
    helper.runAtTickTime(140, () -> {
      train_A_C.setMode(Locomotive.Mode.RUNNING);
    });

    helper.succeedWhen(() -> {
      helper.assertEntityInstancePresent(train_A, TRAIN_A);
      helper.assertEntityInstancePresent(train_B, TRAIN_B);
      helper.assertEntityInstancePresent(train_A_B, TRAIN_A_B);
      helper.assertEntityInstancePresent(train_A_C, TRAIN_A_C);
    });
  }

  private static CreativeLocomotive spawnNewLocomotive(GameTestHelper helper, BlockPos pos,
      String dest) {
    var train = helper.spawn(RailcraftEntityTypes.CREATIVE_LOCOMOTIVE.get(), pos);
    train.reverse();
    train.setDestination(dest);
    train.setSpeed(Locomotive.Speed.NORMAL);
    return train;
  }
}
