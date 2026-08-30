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

/**
 * The bounds a tank checks for entities cover its interior only. They used to be centred on the
 * tank instead of on its master block, which for anything bigger than 3x3 reached outside of the
 * tank: a player standing next to the north west corner stopped it from forming.
 *
 * @see <a href="https://github.com/railcraft-reborn/railcraft/issues/189">Issue #189</a>
 */
public class TankEntityOutsideTestInstance extends TankTestInstance {

  public static final MapCodec<TankEntityOutsideTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(TankEntityOutsideTestInstance::new);

  private static final int SIZE = 7;
  private static final BlockPos NORTH_WEST_CORNER = TANK_ORIGIN.offset(-1, 0, -1);

  public TankEntityOutsideTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    helper.spawn(EntityType.ARMOR_STAND, NORTH_WEST_CORNER);
    buildTank(helper, SIZE);
    helper.startSequence()
        .thenIdle(5)
        .thenExecute(() -> assertFormed(helper,
            "while an entity is standing outside of its north west corner"))
        .thenSucceed();
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Tank Ignores Entity Outside");
  }
}
