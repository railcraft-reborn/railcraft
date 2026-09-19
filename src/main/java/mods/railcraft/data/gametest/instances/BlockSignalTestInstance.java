package mods.railcraft.data.gametest.instances;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.item.SignalBlockSurveyorItem;
import mods.railcraft.world.level.block.entity.signal.BlockSignalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;

public class BlockSignalTestInstance extends GameTestInstance {

  public static final MapCodec<BlockSignalTestInstance> CODEC =
      RecordCodecBuilder.mapCodec(instance -> instance.group(
          Codec.BOOL.fieldOf("spawnCart").forGetter(t -> t.spawnCart),
          TestData.CODEC.forGetter(t -> t.info)
      ).apply(instance, BlockSignalTestInstance::new));

  private static final BlockPos BLOCK_SIGNAL_LEFT = new BlockPos(7, 1, 3);
  private static final BlockPos BLOCK_SIGNAL_RIGHT = new BlockPos(2, 1, 3);
  private final boolean spawnCart;

  public BlockSignalTestInstance(boolean spawnCart, TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
    this.spawnCart = spawnCart;
  }

  @Override
  public void run(GameTestHelper helper) {
    if (this.spawnCart) {
      helper.spawn(EntityType.MINECART, new BlockPos(5, 1, 2));
    }
    helper.succeedWhen(() -> {
      var left = helper.getBlockEntity(BLOCK_SIGNAL_LEFT, BlockSignalBlockEntity.class);
      var right = helper.getBlockEntity(BLOCK_SIGNAL_RIGHT, BlockSignalBlockEntity.class);

      SignalBlockSurveyorItem.tryLinking(left, right);

      var expectedAspect = this.spawnCart ? SignalAspect.RED : SignalAspect.GREEN;

      if (left.getPrimarySignalAspect().equals(expectedAspect) &&
          right.getPrimarySignalAspect().equals(expectedAspect)) {
        helper.succeed();
      } else {
        var expectedText = this.spawnCart ? "Red" : "Green";
        helper.fail(Component.literal("Expected %s on both Signal Block".formatted(expectedText)));
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
      return Component.literal("Block Signal With Cart");
    }
    return Component.literal("Block Signal No Cart");
  }
}
