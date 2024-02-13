package mods.railcraft.world.item.tunnelbore;

import mods.railcraft.Translations;
import mods.railcraft.api.track.RailShapeUtil;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.integrations.jei.JeiSearchable;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.world.entity.vehicle.TunnelBore;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.properties.RailShape;

public class TunnelBoreItem extends Item implements JeiSearchable {

  public TunnelBoreItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    var level = context.getLevel();
    var pos = context.getClickedPos();
    var existingState = level.getBlockState(pos);
    if (BaseRailBlock.isRail(existingState)) {
      if (!level.isClientSide() && EntitySearcher.findMinecarts()
          .at(pos)
          .list(level)
          .isEmpty()) {
        var trackShape = TrackUtil.getTrackDirection(level, pos, existingState);
        if (RailShapeUtil.isLevelStraight(trackShape)) {
          var playerFacing = context.getHorizontalDirection().getOpposite();

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

          var bore = new TunnelBore(
              level, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, playerFacing);
          level.addFreshEntity(bore);
        }
      }
      context.getItemInHand().shrink(1);
      return InteractionResult.sidedSuccess(level.isClientSide());
    }
    return InteractionResult.CONSUME;
  }

  @Override
  public Component jeiDescription() {
    return Component.translatable(Translations.Jei.TUNNEL_BORE);
  }
}
