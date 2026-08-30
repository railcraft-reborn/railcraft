package mods.railcraft.data.gametest.instances.tank;

import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.tank.TankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Base class for the iron tank multiblock tests. They all share the same empty structure and build
 * their tank in code so that different base sizes can be tested without a template for each.
 */
public abstract class TankTestInstance extends GameTestInstance {

  /**
   * The north west corner of the bottom layer of every tank built by these tests. Leaves one block
   * of room towards the north west so that entities can be placed right outside of the tank.
   */
  protected static final BlockPos TANK_ORIGIN = new BlockPos(1, 1, 1);

  protected static final int TANK_HEIGHT = 4;

  protected TankTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  /**
   * Builds the hollow shell of a tank with the specified base size at {@link #TANK_ORIGIN}.
   */
  protected static void buildTank(GameTestHelper helper, int size) {
    for (var y = 0; y < TANK_HEIGHT; y++) {
      for (var x = 0; x < size; x++) {
        for (var z = 0; z < size; z++) {
          var wall = y == 0 || y == TANK_HEIGHT - 1
              || x == 0 || x == size - 1
              || z == 0 || z == size - 1;
          if (wall) {
            helper.setBlock(TANK_ORIGIN.offset(x, y, z), wallBlock());
          }
        }
      }
    }
  }

  protected static Block wallBlock() {
    return RailcraftBlocks.IRON_TANK_WALL.variantFor(DyeColor.WHITE).get();
  }

  /**
   * The master of a tank built at {@link #TANK_ORIGIN}. It always sits at the pattern's master
   * offset, one block east and one block south of the north west corner of the bottom layer.
   */
  protected static BlockPos masterPos() {
    return TANK_ORIGIN.offset(1, 0, 1);
  }

  protected static TankBlockEntity master(GameTestHelper helper) {
    return helper.getBlockEntity(masterPos(), TankBlockEntity.class);
  }

  protected static void assertFormed(GameTestHelper helper, String context) {
    if (!master(helper).isFormed()) {
      helper.fail(Component.literal("Tank is not formed " + context), masterPos());
    }
  }

  protected static void assertNotFormed(GameTestHelper helper, String context) {
    if (master(helper).isFormed()) {
      helper.fail(Component.literal("Tank is still formed " + context), masterPos());
    }
  }

  protected static void setFluid(GameTestHelper helper, FluidStack fluidStack) {
    master(helper).getModule().getTank().setFluid(fluidStack);
  }

  protected static void assertFluid(GameTestHelper helper, FluidStack expected, String context) {
    var actual = master(helper).getModule().getTank().getFluidStack();
    if (!FluidStack.matches(actual, expected)) {
      helper.fail(Component.literal(
          "Expected " + expected.getAmount() + " of " + expected.getHoverName().getString()
              + " " + context + " but found " + actual.getAmount() + " of "
              + actual.getHoverName().getString()),
          masterPos());
    }
  }

}
