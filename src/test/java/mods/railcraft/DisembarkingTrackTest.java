package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.outfitted.OutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.PoweredOutfittedTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class DisembarkingTrackTest {

  @GameTest(template = "disembarking_track_active")
  public static void disembarkingTrackActive(GameTestHelper helper) {
    helper.spawnWithNoFreeWill(EntityType.VILLAGER, 1, 2, 3);
    helper.pressButton(0, 3, 3);
    helper.succeedWhen(() -> {
      helper.assertEntityPresent(EntityType.MINECART, 5, 2, 3);
      helper.assertEntityPresent(EntityType.VILLAGER, 3, 2, 4);
    });
  }

  @GameTest(template = "disembarking_track_passive")
  public static void disembarkingTrackPassive(GameTestHelper helper) {
    helper.spawnWithNoFreeWill(EntityType.VILLAGER, 1, 2, 3);
    helper.pressButton(0, 3, 3);
    helper.succeedWhen(() -> {
      helper.assertEntityPresent(EntityType.MINECART, 5, 2, 3);
      helper.assertEntityPresent(EntityType.VILLAGER, 5, 2, 3);
    });
  }

  /**
   * Issue #273, a shulker doesn't fall, so it must be dropped straight on the floor next to the
   * track and not one block above it.
   */
  @GameTest(template = "platform_7x7")
  public static void disembarkingTrackOntoFloor(GameTestHelper helper) {
    helper.setBlock(2, 1, 3, Blocks.DIRT);
    helper.setBlock(2, 2, 3, Blocks.WITHER_ROSE);
    disembarkShulker(helper);
    helper.succeedWhen(() -> helper.assertEntityPresent(EntityType.SHULKER, 2, 2, 3));
  }

  @GameTest(template = "platform_7x7")
  public static void disembarkingTrackOntoBlock(GameTestHelper helper) {
    helper.setBlock(2, 2, 3, Blocks.DIRT);
    disembarkShulker(helper);
    helper.succeedWhen(() -> helper.assertEntityPresent(EntityType.SHULKER, 2, 3, 3));
  }

  private static void disembarkShulker(GameTestHelper helper) {
    helper.setBlock(4, 2, 3, Blocks.REDSTONE_BLOCK);
    helper.setBlock(3, 2, 3, RailcraftBlocks.IRON_DISEMBARKING_TRACK.get().defaultBlockState()
        .setValue(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
        .setValue(PoweredOutfittedTrackBlock.POWERED, true));
    var cart = helper.spawn(EntityType.MINECART, new BlockPos(3, 2, 3));
    var shulker = helper.spawnWithNoFreeWill(EntityType.SHULKER, new BlockPos(3, 3, 3));
    // Without this a shulker teleports away from blocks it can't attach to
    shulker.setNoAi(true);
    shulker.startRiding(cart, true);
  }
}
