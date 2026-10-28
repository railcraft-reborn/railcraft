package mods.railcraft.data.gametest.instances.elevator_track;

import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

/**
 * An elevator whose bottom is reached by a slope rising towards it, so carts go up and come back
 * down on the same line.
 */
final class ElevatorTrackSlopeTestHelper {

  static final int Z = 3;

  private ElevatorTrackSlopeTestHelper() {}

  /**
   * An elevator from y=2 to y=5 on a wall at x=0, with a rail at its top and, at its bottom, a
   * slope rising west from the floor to the height of the elevator.
   */
  static void build(GameTestHelper helper) {
    for (int y = 1; y <= 6; y++) {
      helper.setBlock(new BlockPos(0, y, Z), Blocks.POLISHED_ANDESITE);
    }
    for (int y = 2; y <= 5; y++) {
      helper.setBlock(new BlockPos(1, y, Z), RailcraftBlocks.ELEVATOR_TRACK.get()
          .defaultBlockState().setValue(ElevatorTrackBlock.FACING, Direction.EAST));
    }
    // A slope needs a solid block on its upper side
    helper.setBlock(new BlockPos(1, 1, Z), Blocks.POLISHED_ANDESITE);
    for (int x = 3; x <= 6; x++) {
      helper.setBlock(new BlockPos(x, 1, Z), rail(RailShape.EAST_WEST));
    }
    // Placing a rail recomputes its shape, which flattens a slope with no rail above it, so it is
    // set again once it is already a rail
    helper.setBlock(new BlockPos(2, 1, Z), rail(RailShape.EAST_WEST));
    helper.setBlock(new BlockPos(2, 1, Z), rail(RailShape.ASCENDING_WEST));

    helper.setBlock(new BlockPos(2, 4, Z), Blocks.POLISHED_ANDESITE);
    helper.setBlock(new BlockPos(3, 4, Z), Blocks.POLISHED_ANDESITE);
    helper.setBlock(new BlockPos(2, 5, Z), rail(RailShape.EAST_WEST));
    helper.setBlock(new BlockPos(3, 5, Z), rail(RailShape.EAST_WEST));
  }

  private static BlockState rail(RailShape shape) {
    return Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, shape);
  }
}
