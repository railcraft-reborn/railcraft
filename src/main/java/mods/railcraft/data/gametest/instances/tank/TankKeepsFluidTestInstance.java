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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Breaking a block of a formed tank and putting it back must not void the stored fluid, as long as
 * the master block itself survives.
 *
 * @see <a href="https://github.com/railcraft-reborn/railcraft/issues/303">Issue #303</a>
 */
public class TankKeepsFluidTestInstance extends TankTestInstance {

  public static final MapCodec<TankKeepsFluidTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(TankKeepsFluidTestInstance::new);

  private static final int SIZE = 3;
  private static final BlockPos BROKEN_WALL = TANK_ORIGIN.offset(0, 1, 0);

  public TankKeepsFluidTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  private static FluidStack fluid() {
    return new FluidStack(Fluids.WATER, 5000);
  }

  @Override
  public void run(GameTestHelper helper) {
    buildTank(helper, SIZE);
    helper.startSequence()
        .thenIdle(5)
        .thenExecute(() -> {
          assertFormed(helper, "after being built");
          setFluid(helper, fluid());
        })
        .thenIdle(5)
        .thenExecute(() -> helper.setBlock(BROKEN_WALL, Blocks.AIR))
        .thenIdle(5)
        .thenExecute(() -> assertNotFormed(helper, "after a wall block was broken"))
        .thenExecute(() -> helper.setBlock(BROKEN_WALL, wallBlock()))
        .thenIdle(5)
        .thenExecute(() -> {
          assertFormed(helper, "after the wall block was put back");
          assertFluid(helper, fluid(), "after the tank was repaired");
        })
        .thenSucceed();
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Tank Keeps Fluid When Repaired");
  }
}
