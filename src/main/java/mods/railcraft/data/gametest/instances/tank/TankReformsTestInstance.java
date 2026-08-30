package mods.railcraft.data.gametest.instances.tank;

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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * A tank which was formed before re-forms on its own. Evaluation is otherwise only triggered by
 * neighbouring block updates, which left broken tanks waiting for an unrelated block update that
 * may never come.
 *
 * @see <a href="https://github.com/railcraft-reborn/railcraft/issues/189">Issue #189</a>
 */
public class TankReformsTestInstance extends TankTestInstance {

  public static final MapCodec<TankReformsTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(TankReformsTestInstance::new);

  private static final int SIZE = 3;
  private static final BlockPos BROKEN_WALL = TANK_ORIGIN.offset(0, 1, 0);

  public TankReformsTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    buildTank(helper, SIZE);
    helper.startSequence()
        .thenIdle(5)
        .thenExecute(() -> assertFormed(helper, "after being built"))
        .thenExecute(() -> helper.setBlock(BROKEN_WALL, Blocks.AIR))
        .thenIdle(5)
        .thenExecute(() -> {
          assertNotFormed(helper, "after a wall block was broken");
          // Restored without notifying the neighbours, so nothing tells the tank to re-evaluate
          // its pattern, it has to pick the block up on its own.
          helper.getLevel().setBlock(helper.absolutePos(BROKEN_WALL),
              wallBlock().defaultBlockState(), Block.UPDATE_CLIENTS);
        })
        .thenIdle(60)
        .thenExecute(() -> assertFormed(helper, "after re-evaluating itself"))
        .thenSucceed();
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Tank Re-Forms Without Block Update");
  }
}
