package mods.railcraft.data.gametest.instances.disembarking_track;

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

public class DisembarkingTrackPassiveTestInstance extends GameTestInstance {

  public static final MapCodec<DisembarkingTrackPassiveTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(DisembarkingTrackPassiveTestInstance::new);

  public DisembarkingTrackPassiveTestInstance(TestData<Holder<TestEnvironmentDefinition>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    helper.spawnWithNoFreeWill(EntityType.VILLAGER, 1, 1, 3);
    helper.pressButton(0, 2, 3);
    helper.succeedWhen(() -> {
      helper.assertEntityPresent(EntityType.MINECART, 5, 1, 3);
      helper.assertEntityPresent(EntityType.VILLAGER, 5, 1, 3);
    });
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Disembarking Track Passive");
  }
}
