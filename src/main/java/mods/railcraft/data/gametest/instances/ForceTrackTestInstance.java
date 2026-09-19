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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class ForceTrackTestInstance extends GameTestInstance {

  public static final MapCodec<ForceTrackTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(ForceTrackTestInstance::new);

  public ForceTrackTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    var pos =  helper.absolutePos(new BlockPos(4, 1, 2));
    helper.onEachTick(() -> {
      var energyStorage = helper.getLevel()
          .getCapability(Capabilities.Energy.BLOCK, pos, null);
      if (energyStorage != null) {
        try (var tx = Transaction.openRoot()) {
          energyStorage.insert(10000, tx);
          tx.commit();
        }
      }
    });

    helper.pressButton(1, 3, 2);
    helper.succeedWhenEntityPresent(EntityType.MINECART, 10, 2, 2);
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    return Component.literal("Force Track Emitter");
  }
}
