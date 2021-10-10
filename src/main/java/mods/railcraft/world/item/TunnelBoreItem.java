package mods.railcraft.world.item;

import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.entity.TunnelBoreEntity;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TunnelBoreItem extends Item {

  public TunnelBoreItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType useOn(ItemUseContext context) {
    World world = context.getLevel();
    BlockPos pos = context.getClickedPos();
    BlockState existingState = world.getBlockState(pos);
    if (AbstractRailBlock.isRail(existingState)) {
      if (!world.isClientSide() && EntitySearcher.findMinecarts()
          .around(pos)
          .in(world)
          .isEmpty()) {
        RailShape trackShape =
            TrackTools.getTrackDirection(world, pos, existingState);
        if (TrackShapeHelper.isLevelStraight(trackShape)) {
          Direction playerFacing = context.getHorizontalDirection().getOpposite();

          if (trackShape == RailShape.NORTH_SOUTH) {
            if (playerFacing == Direction.WEST)
              playerFacing = Direction.NORTH;
            else if (playerFacing == Direction.EAST)
              playerFacing = Direction.SOUTH;
          } else if (trackShape == RailShape.EAST_WEST) {
            if (playerFacing == Direction.SOUTH)
              playerFacing = Direction.EAST;
            else if (playerFacing == Direction.NORTH)
              playerFacing = Direction.WEST;
          }

          AbstractMinecartEntity bore = new TunnelBoreEntity(
              world, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, playerFacing);
          world.addFreshEntity(bore);
        }
      }
      context.getItemInHand().shrink(1);
      return ActionResultType.sidedSuccess(world.isClientSide());
    }
    return ActionResultType.CONSUME;
  }
}
