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

public class BlockSignalRelayBoxTestInstance extends GameTestInstance {

  public static final MapCodec<BlockSignalRelayBoxTestInstance> CODEC =
      RecordCodecBuilder.mapCodec(instance -> instance.group(
          Codec.BOOL.fieldOf("spawnCart").forGetter(t -> t.spawnCart),
          TestData.CODEC.forGetter(t -> t.info)
      ).apply(instance, BlockSignalRelayBoxTestInstance::new));

  private static final BlockPos BLOCK_SIGNAL_LEFT = new BlockPos(5, 1, 3);
  private static final BlockPos BLOCK_SIGNAL_RIGHT = new BlockPos(1, 1, 5);
  private static final BlockPos BLOCK_SIGNAL_RELAY_BOX = new BlockPos(2, 1, 1);
  private final boolean spawnCart;

  public BlockSignalRelayBoxTestInstance(boolean spawnCart, TestData<Holder<TestEnvironmentDefinition>> info) {
    super(info);
    this.spawnCart = spawnCart;
  }

  @Override
  public void run(GameTestHelper helper) {
    if (this.spawnCart) {
      helper.spawn(EntityType.MINECART, new BlockPos(4, 1, 2));
    }
    helper.succeedWhen(() -> {
      var left = helper.getBlockEntity(BLOCK_SIGNAL_LEFT, BlockSignalBlockEntity.class);
      var right = helper.getBlockEntity(BLOCK_SIGNAL_RIGHT, BlockSignalBlockEntity.class);
      var box = helper.getBlockEntity(BLOCK_SIGNAL_RELAY_BOX, BlockSignalRelayBoxBlockEntity.class);

      SignalBlockSurveyorItem.tryLinking(left, box);
      SignalBlockSurveyorItem.tryLinking(right, box);

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
      return Component.literal("Block Signal Relay Box With Cart");
    }
    return Component.literal("Block Signal Relay Box No Cart");
  }
}
