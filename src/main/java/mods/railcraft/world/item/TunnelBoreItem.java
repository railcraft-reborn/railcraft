package mods.railcraft.world.item;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.util.TrackTools;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.world.entity.TunnelBoreEntity;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TunnelBoreItem extends CartItem {

  public TunnelBoreItem(Supplier<? extends EntityType<?>> cart, Properties properties) {
    super(cart, properties);
  }

  @Override
  public ActionResultType useOn(ItemUseContext context) {
    World world = context.getLevel();
    BlockPos pos = context.getClickedPos();
    BlockState existingState = world.getBlockState(pos);
    PlayerEntity player = context.getPlayer();
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
          CartUtil.setCartOwner(bore, player);
          world.addFreshEntity(bore);
        }
      }
      InvTools.dec(context.getItemInHand());
      return ActionResultType.sidedSuccess(world.isClientSide());
    }
    return ActionResultType.CONSUME;
  }

  @Override
  public boolean canBePlacedByNonPlayer(ItemStack cart) {
    return false;
  }

  @Override
  @Nullable
  public AbstractMinecartEntity placeCart(GameProfile owner, ItemStack cartStack, ServerWorld world,
      BlockPos pos) {
    return null;
  }
}
