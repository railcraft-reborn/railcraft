package mods.railcraft.data.gametest.instances.turnout_track;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;

public class TurnoutTrackPassiveTestInstance extends GameTestInstance {

  public static final MapCodec<TurnoutTrackPassiveTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(TurnoutTrackPassiveTestInstance::new);

  public TurnoutTrackPassiveTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    helper.pressButton(1, 2, 2);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 5, 1, 2);
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Turnout Track Passive");
  }
}
