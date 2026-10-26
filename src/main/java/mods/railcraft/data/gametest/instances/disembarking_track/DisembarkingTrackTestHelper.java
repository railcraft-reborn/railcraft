package mods.railcraft.data.gametest.instances.disembarking_track;

import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.outfitted.OutfittedTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.PoweredOutfittedTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.RailShape;

final class DisembarkingTrackTestHelper {

  private DisembarkingTrackTestHelper() {}

  static void disembarkShulker(GameTestHelper helper) {
    helper.setBlock(4, 1, 3, Blocks.REDSTONE_BLOCK);
    helper.setBlock(3, 1, 3, RailcraftBlocks.IRON_DISEMBARKING_TRACK.get().defaultBlockState()
        .setValue(OutfittedTrackBlock.SHAPE, RailShape.NORTH_SOUTH)
        .setValue(PoweredOutfittedTrackBlock.POWERED, true));
    var cart = helper.spawn(EntityType.MINECART, new BlockPos(3, 1, 3));
    var shulker = helper.spawnWithNoFreeWill(EntityType.SHULKER, new BlockPos(3, 2, 3));
    // Without this a shulker teleports away from blocks it can't attach to
    shulker.setNoAi(true);
    shulker.startRiding(cart, true, false);
  }
}
