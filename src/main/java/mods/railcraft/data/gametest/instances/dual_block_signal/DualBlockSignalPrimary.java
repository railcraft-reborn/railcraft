package mods.railcraft.data.gametest.instances.dual_block_signal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.item.SignalBlockSurveyorItem;
import mods.railcraft.world.level.block.entity.signal.BlockSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.DualBlockSignalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;

public class DualBlockSignalPrimary extends GameTestInstance {

  public static final MapCodec<DualBlockSignalPrimary> CODEC =
      RecordCodecBuilder.mapCodec(instance -> instance.group(
          Codec.BOOL.fieldOf("spawnCart").forGetter(t -> t.spawnCart),
          TestData.CODEC.forGetter(t -> t.info)
      ).apply(instance, DualBlockSignalPrimary::new));

  private static final BlockPos DUAL_BLOCK_SIGNAL = new BlockPos(1, 1, 1);
  private static final BlockPos BLOCK_SIGNAL = new BlockPos(1, 1, 3);
  private final boolean spawnCart;

  public DualBlockSignalPrimary(boolean spawnCart, TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
    this.spawnCart = spawnCart;
  }

  @Override
  public void run(GameTestHelper helper) {
    if (spawnCart) {
      helper.spawn(EntityType.MINECART, new BlockPos(1, 1, 2));
    }
    helper.succeedWhen(() -> {
      var dual = helper.getBlockEntity(DUAL_BLOCK_SIGNAL, DualBlockSignalBlockEntity.class);
      var single = helper.getBlockEntity(BLOCK_SIGNAL, BlockSignalBlockEntity.class);
      SignalBlockSurveyorItem.tryLinking(dual, single);

      var expectedAspect = this.spawnCart ? SignalAspect.RED : SignalAspect.GREEN;

      var primarySignalAspect = dual.getPrimarySignalAspect();
      if (single.getPrimarySignalAspect().equals(primarySignalAspect) &&
          primarySignalAspect.equals(expectedAspect)) {
        helper.succeed();
      } else {
        if (spawnCart) {
          helper.fail(Component.literal("Expected Red on Block Signal and Dual Block Signal"));
        } else {
          helper.fail(Component.literal("Expected Green on Block Signal and Dual Block Signal"));
        }
      }
    });
  }

  @Override
  public MapCodec<? extends GameTestInstance> codec() {
    return CODEC;
  }

  @Override
  protected MutableComponent typeDescription() {
    if (this.spawnCart) {
      return Component.literal("Dual Block Signal Primary With Cart");
    }
    return Component.literal("Dual Block Signal Primary No Cart");
  }
}
