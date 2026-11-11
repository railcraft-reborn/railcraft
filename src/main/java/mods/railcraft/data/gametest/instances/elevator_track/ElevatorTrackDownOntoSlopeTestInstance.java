package mods.railcraft.data.gametest.instances.elevator_track;

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
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3;

public class ElevatorTrackDownOntoSlopeTestInstance extends GameTestInstance {

  public static final MapCodec<ElevatorTrackDownOntoSlopeTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(ElevatorTrackDownOntoSlopeTestInstance::new);

  public ElevatorTrackDownOntoSlopeTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    ElevatorTrackSlopeTestHelper.build(helper);
    helper.spawn(EntityTypes.MINECART, new Vec3(1.5, 5.2, ElevatorTrackSlopeTestHelper.Z + 0.5));
    helper.succeedWhenEntityPresent(EntityTypes.MINECART,
        new BlockPos(5, 1, ElevatorTrackSlopeTestHelper.Z));
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Elevator Track Down Onto Slope");
  }
}
