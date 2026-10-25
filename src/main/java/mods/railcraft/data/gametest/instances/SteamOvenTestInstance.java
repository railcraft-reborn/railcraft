package mods.railcraft.data.gametest.instances;

import com.mojang.serialization.MapCodec;
import mods.railcraft.data.gametest.RailcraftGameTestInstances;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.SteamOvenBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * The lower half of the oven faces north and the upper half faces south: once formed, the door
 * must only be shown on one side.
 */
public class SteamOvenTestInstance extends GameTestInstance {

  public static final MapCodec<SteamOvenTestInstance> CODEC =
      RailcraftGameTestInstances.defaultCodec(SteamOvenTestInstance::new);

  public SteamOvenTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
    super(info);
  }

  @Override
  public void run(GameTestHelper helper) {
    helper.succeedWhen(() -> {
      helper.assertBlockPresent(RailcraftBlocks.STEAM_OVEN.get(), new BlockPos(1, 1, 1));
      var facing = helper.getBlockState(new BlockPos(1, 1, 1)).getValue(SteamOvenBlock.FACING);
      for (int x = 1; x <= 2; x++) {
        for (int y = 1; y <= 2; y++) {
          for (int z = 1; z <= 2; z++) {
            var pos = new BlockPos(x, y, z);
            helper.assertBlockPresent(RailcraftBlocks.STEAM_OVEN.get(), pos);
            var state = helper.getBlockState(pos);
            helper.assertTrue(state.getValue(SteamOvenBlock.TYPE) != SteamOvenBlock.Type.DEFAULT,
                "The oven must be formed, %s is not".formatted(pos));
            // The door is drawn on the facing side of each block
            helper.assertTrue(state.getValue(SteamOvenBlock.FACING) == facing,
                "All oven blocks must face the same way, %s faces %s instead of %s"
                    .formatted(pos, state.getValue(SteamOvenBlock.FACING), facing));
          }
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
    return Component.literal("Steam Oven");
  }
}
