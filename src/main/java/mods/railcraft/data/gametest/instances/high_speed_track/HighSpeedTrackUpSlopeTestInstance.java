package mods.railcraft.data.gametest.instances.high_speed_track;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.state.properties.RailShape;

public class HighSpeedTrackUpSlopeTestInstance extends GameTestInstance {

  public static final MapCodec<HighSpeedTrackUpSlopeTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(HighSpeedTrackUpSlopeTestInstance::new);

  public HighSpeedTrackUpSlopeTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    HighSpeedTrackTestHelper.buildLine(helper, 1, RailShape.ASCENDING_SOUTH, 2);
    HighSpeedTrackTestHelper.runCart(helper, 1, 1.72);
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("High Speed Track Up Slope");
  }
}
