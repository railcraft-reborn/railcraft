package mods.railcraft.data.gametest.instances;

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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.RedstoneLampBlock;

public class DetectorTrackTestInstance extends GameTestInstance {

  public static final MapCodec<DetectorTrackTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(DetectorTrackTestInstance::new);

  public DetectorTrackTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    helper.pressButton(0, 2, 1);
    helper.succeedWhen(() -> {
      helper.assertEntityPresent(EntityType.MINECART, 3, 1, 1);
      var redstoneLampPos = new BlockPos(3, 1, 0);
      helper.assertBlockProperty(redstoneLampPos, RedstoneLampBlock.LIT, true);
    });
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Detector Track");
  }
}
