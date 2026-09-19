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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * The interior of a tank is enclosed and dark, so mobs do spawn in it. An entity ending up inside
 * of an already formed tank must not disband it, which used to happen on the next block update
 * anywhere near the tank and took the entire contents with it.
 */
public class TankEntityInsideTestInstance extends TankTestInstance {

  public static final MapCodec<TankEntityInsideTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(TankEntityInsideTestInstance::new);

  private static final int SIZE = 3;
  private static final BlockPos INTERIOR = TANK_ORIGIN.offset(1, 1, 1);
  private static final BlockPos NEIGHBOR = TANK_ORIGIN.offset(SIZE, 1, 1);

  public TankEntityInsideTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
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
          helper.spawn(EntityType.ARMOR_STAND, INTERIOR);
        })
        .thenIdle(5)
        // Any block update next to the tank makes it re-evaluate its pattern.
        .thenExecute(() -> helper.setBlock(NEIGHBOR, Blocks.STONE))
        .thenIdle(5)
        .thenExecute(() -> {
          assertFormed(helper, "while an entity is inside of it");
          assertFluid(helper, fluid(), "while an entity is inside of it");
        })
        .thenSucceed();
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Tank Survives Entity Inside");
  }
}
