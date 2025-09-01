package mods.railcraft.data.gametest.instances.block_signal_relay_box;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.item.SignalBlockSurveyorItem;
import mods.railcraft.world.level.block.entity.signal.BlockSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.BlockSignalRelayBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;

public class BlockSignalRelayBoxComplexTestInstance extends GameTestInstance {

  public static final MapCodec<BlockSignalRelayBoxComplexTestInstance> CODEC =
      RecordCodecBuilder.mapCodec(instance -> instance.group(
          Codec.BOOL.fieldOf("spawnCart").forGetter(t -> t.spawnCart),
          TestData.CODEC.forGetter(t -> t.info)
      ).apply(instance, BlockSignalRelayBoxComplexTestInstance::new));

  private static final BlockPos BLOCK_SIGNAL_LEFT_COMPLEX = new BlockPos(7, 1, 1);
  private static final BlockPos BLOCK_SIGNAL_RIGHT_COMPLEX = new BlockPos(1, 1, 7);
  private static final BlockPos BLOCK_SIGNAL_RELAY_BOX_1_COMPLEX = new BlockPos(6, 1, 4);
  private static final BlockPos BLOCK_SIGNAL_RELAY_BOX_2_COMPLEX = new BlockPos(2, 1, 3);
  private static final BlockPos BLOCK_SIGNAL_RELAY_BOX_3_COMPLEX = new BlockPos(4, 1, 6);
  private final boolean spawnCart;

  public BlockSignalRelayBoxComplexTestInstance(boolean spawnCart, TestData<Holder<TestEnvironmentDefinition>> info) {
    super(info);
    this.spawnCart = spawnCart;
  }

  @Override
  public void run(GameTestHelper helper) {
    if (this.spawnCart) {
      helper.spawn(EntityType.MINECART, new BlockPos(6, 1, 2));
    }
    helper.succeedWhen(() -> {
      var left = helper.getBlockEntity(BLOCK_SIGNAL_LEFT_COMPLEX, BlockSignalBlockEntity.class);
      var right = helper.getBlockEntity(BLOCK_SIGNAL_RIGHT_COMPLEX, BlockSignalBlockEntity.class);
      var box1 = helper.getBlockEntity(BLOCK_SIGNAL_RELAY_BOX_1_COMPLEX, BlockSignalRelayBoxBlockEntity.class);
      var box2 = helper.getBlockEntity(BLOCK_SIGNAL_RELAY_BOX_2_COMPLEX, BlockSignalRelayBoxBlockEntity.class);
      var box3 = helper.getBlockEntity(BLOCK_SIGNAL_RELAY_BOX_3_COMPLEX, BlockSignalRelayBoxBlockEntity.class);

      SignalBlockSurveyorItem.tryLinking(left, box1);
      SignalBlockSurveyorItem.tryLinking(box1, box2);
      SignalBlockSurveyorItem.tryLinking(box2, box3);
      SignalBlockSurveyorItem.tryLinking(right, box3);

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
      return Component.literal("Block Signal Relay Box Complex With Cart");
    }
    return Component.literal("Block Signal Relay Box Complex No Cart");
  }
}
