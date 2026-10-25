package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.SteamOvenBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class SteamOvenTest {

  /**
   * The lower half of the oven faces north and the upper half faces south: once formed, the door
   * must only be shown on one side.
   */
  @GameTest(template = "steam_oven")
  public static void steamOvenDoorOnOneSide(GameTestHelper helper) {
    helper.succeedWhen(() -> {
      helper.assertBlockPresent(RailcraftBlocks.STEAM_OVEN.get(), new BlockPos(1, 2, 1));
      var facing = helper.getBlockState(new BlockPos(1, 2, 1)).getValue(SteamOvenBlock.FACING);
      for (int x = 1; x <= 2; x++) {
        for (int y = 2; y <= 3; y++) {
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
}
