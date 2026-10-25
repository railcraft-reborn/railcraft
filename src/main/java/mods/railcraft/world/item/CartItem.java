package mods.railcraft.world.item;

import mods.railcraft.api.item.MinecartFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;

public class CartItem extends Item {

  private final MinecartFactory minecartFactory;

  public CartItem(MinecartFactory minecartFactory, Properties properties) {
    super(properties);
    this.minecartFactory = minecartFactory;
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    var level = context.getLevel();
    var railPos = context.getClickedPos();
    if (!BaseRailBlock.isRail(level, railPos)) {
      return InteractionResult.FAIL;
    }
    if (level instanceof ServerLevel serverLevel) {
      var player = context.getPlayer();
      var railState = level.getBlockState(railPos);
      var itemStack = player.getItemInHand(context.getHand());
      var railShape = RailShape.NORTH_SOUTH;
      if (railState.getBlock() instanceof BaseRailBlock baseRailBlock) {
        railShape = baseRailBlock.getRailDirection(railState, level, railPos, null);
      }
      double d0 = railShape.isAscending() ? 0.5 : 0.0;

      var minecart = this.minecartFactory.createMinecart(itemStack,
          railPos.getX() + 0.5, railPos.getY() + d0 + 0.0625, railPos.getZ() + 0.5, serverLevel);
      if (minecart != null) {
        // Minecart yaws are a quarter turn off the ones of the other entities
        minecart.setYRot(alignWithTrack(minecart, context.getRotation() + 90));
        level.addFreshEntity(minecart);
        level.gameEvent(GameEvent.ENTITY_PLACE, railPos,
            GameEvent.Context.of(player, level.getBlockState(railPos.below())));
        itemStack.shrink(1);
      }
    }
    return InteractionResult.sidedSuccess(level.isClientSide());
  }

  /**
   * Turns the yaw along the track, otherwise the first move turns the cart either way.
   */
  private static float alignWithTrack(AbstractMinecart minecart, float yaw) {
    var x = minecart.getX();
    var y = minecart.getY();
    var z = minecart.getZ();
    var front = minecart.getPosOffs(x, y, z, 0.3);
    var back = minecart.getPosOffs(x, y, z, -0.3);
    if (front == null || back == null || front.equals(back)) {
      return yaw;
    }
    var trackYaw = (float) (Mth.atan2(front.z - back.z, front.x - back.x) * Mth.RAD_TO_DEG);
    return Mth.degreesDifferenceAbs(trackYaw, yaw) <= 90 ? trackYaw : trackYaw + 180;
  }

  public MinecartFactory getMinecartFactory() {
    return this.minecartFactory;
  }
}
