package mods.railcraft.data.gametest.instances.wye_track;

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

public class WyeTrackActiveTestInstance extends GameTestInstance {

  public static final MapCodec<WyeTrackActiveTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(WyeTrackActiveTestInstance::new);

  public WyeTrackActiveTestInstance(TestData<Holder<TestEnvironmentDefinition>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    helper.pressButton(0, 2, 3);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 3, 1, 1);
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Wye Track Active");
  }
}
