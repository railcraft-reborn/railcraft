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

/**
 * Issue #273, a shulker doesn't fall, so it must be dropped straight on the floor next to the
 * track and not one block above it.
 */
public class DisembarkingTrackOntoFloorTestInstance extends GameTestInstance {

  public static final MapCodec<DisembarkingTrackOntoFloorTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(DisembarkingTrackOntoFloorTestInstance::new);

  public DisembarkingTrackOntoFloorTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    helper.setBlock(2, 0, 3, Blocks.DIRT);
    helper.setBlock(2, 1, 3, Blocks.WITHER_ROSE);
    DisembarkingTrackTestHelper.disembarkShulker(helper);
    helper.succeedWhen(() -> helper.assertEntityPresent(EntityType.SHULKER, 2, 1, 3));
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Disembarking Track Onto Floor");
  }
}
