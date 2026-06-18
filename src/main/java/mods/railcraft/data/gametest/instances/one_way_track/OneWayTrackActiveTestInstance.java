package mods.railcraft.data.gametest.instances.one_way_track;

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

public class OneWayTrackActiveTestInstance extends GameTestInstance {

  public static final MapCodec<OneWayTrackActiveTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(OneWayTrackActiveTestInstance::new);

  public OneWayTrackActiveTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    helper.pressButton(0, 2, 1);
    helper.runAfterDelay(50L, () -> {
      helper.succeedWhenEntityPresent(EntityType.MINECART, 1, 1, 1);
    });
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("One Way Track Active");
  }
}
