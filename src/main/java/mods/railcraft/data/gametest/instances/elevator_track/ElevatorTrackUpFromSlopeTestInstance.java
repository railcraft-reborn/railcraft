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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class ElevatorTrackUpFromSlopeTestInstance extends GameTestInstance {

  public static final MapCodec<ElevatorTrackUpFromSlopeTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(ElevatorTrackUpFromSlopeTestInstance::new);

  public ElevatorTrackUpFromSlopeTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    ElevatorTrackSlopeTestHelper.build(helper);
    helper.setBlock(new BlockPos(1, 6, ElevatorTrackSlopeTestHelper.Z), Blocks.REDSTONE_BLOCK);
    var cart = helper.spawn(EntityTypes.MINECART,
        new Vec3(5.5, 1.1, ElevatorTrackSlopeTestHelper.Z + 0.5));
    cart.setDeltaMovement(-0.6, 0, 0);
    helper.succeedWhenEntityPresent(EntityTypes.MINECART,
        new BlockPos(3, 5, ElevatorTrackSlopeTestHelper.Z));
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Elevator Track Up From Slope");
  }
}
