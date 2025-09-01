package mods.railcraft.data.gametest.instances.junction_track;

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

public class JunctionTrackNorthTestInstance extends GameTestInstance {

  public static final MapCodec<JunctionTrackNorthTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(JunctionTrackNorthTestInstance::new);

  public JunctionTrackNorthTestInstance(TestData<Holder<TestEnvironmentDefinition>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    helper.pressButton(3, 2, 0);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 3, 1, 5);
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Junction Track North");
  }
}
