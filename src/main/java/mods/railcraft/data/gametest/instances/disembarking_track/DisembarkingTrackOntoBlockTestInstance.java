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
import net.minecraft.world.level.block.Blocks;

public class DisembarkingTrackOntoBlockTestInstance extends GameTestInstance {

  public static final MapCodec<DisembarkingTrackOntoBlockTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(DisembarkingTrackOntoBlockTestInstance::new);

  public DisembarkingTrackOntoBlockTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    helper.setBlock(2, 1, 3, Blocks.DIRT);
    DisembarkingTrackTestHelper.disembarkShulker(helper);
    helper.succeedWhen(() -> helper.assertEntityPresent(EntityType.SHULKER, 2, 2, 3));
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Disembarking Track Onto Block");
  }
}
