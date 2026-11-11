package mods.railcraft.data.gametest.instances.delayed_locking_track;

import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.track.DelayedLockingTrackBlockEntity;
import mods.railcraft.world.level.block.track.outfitted.DelayedLockingTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

/**
 * A straight east-west line with a delayed locking track in the middle, and a cart launched east
 * towards it.
 */
final class DelayedLockingTrackTestHelper {

  static final int Y = 1;
  static final int Z = 3;
  static final BlockPos TRACK_POS = new BlockPos(3, Y, Z);
  static final BlockPos NEXT_POS = new BlockPos(4, Y, Z);
  static final BlockPos END_POS = new BlockPos(6, Y, Z);
  static final int DELAY = 40;

  private DelayedLockingTrackTestHelper() {}

  static void build(GameTestHelper helper, boolean powered) {
    for (int x = 0; x <= 6; x++) {
      if (x != TRACK_POS.getX()) {
        helper.setBlock(new BlockPos(x, Y, Z),
            Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.EAST_WEST));
      }
    }
    helper.setBlock(TRACK_POS, RailcraftBlocks.IRON_DELAYED_LOCKING_TRACK.get()
        .defaultBlockState()
        .setValue(DelayedLockingTrackBlock.SHAPE, RailShape.EAST_WEST));
    // Powered after the track is placed, as a player would do
    if (powered) {
      helper.setBlock(TRACK_POS.below(), Blocks.REDSTONE_BLOCK);
    }
    helper.getBlockEntity(TRACK_POS, DelayedLockingTrackBlockEntity.class).setDelay(DELAY);
  }

  static void launchCart(GameTestHelper helper) {
    var cart = helper.spawn(EntityTypes.MINECART, new Vec3(0.5, Y + 0.1, Z + 0.5));
    cart.setDeltaMovement(0.4, 0, 0);
  }
}
