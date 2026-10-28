package mods.railcraft;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * An elevator whose bottom is reached by a slope rising towards it, so carts go up and come back
 * down on the same line.
 */
@GameTestHolder(RailcraftConstants.ID)
@PrefixGameTestTemplate(false)
public class ElevatorTrackSlopeTest {

  private static final int Z = 3;

  @GameTest(template = "platform_7x7_tall", timeoutTicks = 100)
  public static void elevatorTrackUpFromSlope(GameTestHelper helper) {
    build(helper);
    helper.setBlock(new BlockPos(1, 7, Z), Blocks.REDSTONE_BLOCK);
    var cart = helper.spawn(EntityType.MINECART, new Vec3(5.5, 2.1, Z + 0.5));
    cart.setDeltaMovement(-0.6, 0, 0);
    helper.succeedWhenEntityPresent(EntityType.MINECART, new BlockPos(3, 6, Z));
  }

  @GameTest(template = "platform_7x7_tall", timeoutTicks = 100)
  public static void elevatorTrackDownOntoSlope(GameTestHelper helper) {
    build(helper);
    helper.spawn(EntityType.MINECART, new Vec3(1.5, 6.2, Z + 0.5));
    helper.succeedWhenEntityPresent(EntityType.MINECART, new BlockPos(5, 2, Z));
  }

  /**
   * An elevator from y=3 to y=6 on a wall at x=0, with a rail at its top and, at its bottom, a
   * slope rising west from the floor to the height of the elevator.
   */
  private static void build(GameTestHelper helper) {
    for (int y = 2; y <= 7; y++) {
      helper.setBlock(new BlockPos(0, y, Z), Blocks.POLISHED_ANDESITE);
    }
    for (int y = 3; y <= 6; y++) {
      helper.setBlock(new BlockPos(1, y, Z), RailcraftBlocks.ELEVATOR_TRACK.get()
          .defaultBlockState().setValue(ElevatorTrackBlock.FACING, Direction.EAST));
    }
    // A slope needs a solid block on its upper side
    helper.setBlock(new BlockPos(1, 2, Z), Blocks.POLISHED_ANDESITE);
    for (int x = 3; x <= 6; x++) {
      helper.setBlock(new BlockPos(x, 2, Z), rail(RailShape.EAST_WEST));
    }
    // Placing a rail recomputes its shape, which flattens a slope with no rail above it, so it is
    // set again once it is already a rail
    helper.setBlock(new BlockPos(2, 2, Z), rail(RailShape.EAST_WEST));
    helper.setBlock(new BlockPos(2, 2, Z), rail(RailShape.ASCENDING_WEST));

    helper.setBlock(new BlockPos(2, 5, Z), Blocks.POLISHED_ANDESITE);
    helper.setBlock(new BlockPos(3, 5, Z), Blocks.POLISHED_ANDESITE);
    helper.setBlock(new BlockPos(2, 6, Z), rail(RailShape.EAST_WEST));
    helper.setBlock(new BlockPos(3, 6, Z), rail(RailShape.EAST_WEST));
  }

  private static BlockState rail(RailShape shape) {
    return Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, shape);
  }
}
