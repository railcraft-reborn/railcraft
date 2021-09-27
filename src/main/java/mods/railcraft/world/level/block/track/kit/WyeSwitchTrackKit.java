package mods.railcraft.world.level.block.track.kit;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import mods.railcraft.api.tracks.ArrowDirection;
import mods.railcraft.api.tracks.TrackKit;
import mods.railcraft.carts.CartTools;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class WyeSwitchTrackKit extends SwitchTrackKit {

  @Override
  public TrackKit getTrackKit() {
    return TrackKits.WYE.get();
  }

  @Override
  public RailShape getRailDirection(BlockState blockState, @Nullable AbstractMinecartEntity cart) {
    RailShape dir = super.getRailDirection(blockState, cart);
    if (cart != null) {
      if (dir == RailShape.NORTH_SOUTH) {
        if (this.isMirrored(blockState)) {
          if (shouldSwitchForCart(cart)) {
            dir = RailShape.NORTH_WEST;
          } else {
            dir = RailShape.SOUTH_WEST;
          }
        } else {
          if (shouldSwitchForCart(cart)) {
            dir = RailShape.SOUTH_EAST;
          } else {
            dir = RailShape.NORTH_EAST;
          }
        }
      } else if (dir == RailShape.EAST_WEST) {
        if (this.isMirrored(blockState)) {
          if (shouldSwitchForCart(cart)) {
            dir = RailShape.NORTH_EAST;
          } else {
            dir = RailShape.NORTH_WEST;
          }
        } else {
          if (shouldSwitchForCart(cart)) {
            dir = RailShape.SOUTH_WEST;
          } else {
            dir = RailShape.SOUTH_EAST;
          }
        }
      }
    }
    return dir;
  }

  @Override
  protected List<UUID> getCartsAtLockEntrance(BlockState blockState) {
    RailShape dir = this.getRailDirectionRaw();
    BlockPos offset = getPos();
    if (dir == RailShape.EAST_WEST) {
      offset = this.isMirrored(blockState) ? offset.west() : offset.east();
    } else if (dir == RailShape.NORTH_SOUTH) {
      offset = this.isMirrored(blockState) ? offset.south() : offset.north();
    }
    return CartTools.getMinecartUUIDsAt(theWorldAsserted(), offset, 0.1f);
  }

  @Override
  protected List<UUID> getCartsAtDecisionEntrance(BlockState blockState) {
    RailShape dir = this.getRailDirectionRaw();
    BlockPos offset = getPos();
    if (dir == RailShape.EAST_WEST) {
      offset = this.isMirrored(blockState) ? offset.north() : offset.south();
    } else if (dir == RailShape.NORTH_SOUTH) {
      offset = this.isMirrored(blockState) ? offset.east() : offset.west();
    }
    return CartTools.getMinecartUUIDsAt(theWorldAsserted(), offset, 0.1f);
  }

  @Override
  protected List<UUID> getCartsAtSpringEntrance(BlockState blockState) {
    RailShape dir = this.getRailDirectionRaw();
    BlockPos offset = getPos();
    if (dir == RailShape.EAST_WEST) {
      offset = this.isMirrored(blockState) ? offset.east() : offset.west();
    } else if (dir == RailShape.NORTH_SOUTH) {
      offset = this.isMirrored(blockState) ? offset.north() : offset.south();
    }
    return CartTools.getMinecartUUIDsAt(theWorldAsserted(), offset, 0.1f);
  }

  @Override
  public ArrowDirection getRedArrowDirection(BlockState blockState) {
    return this.getRailDirectionRaw() == RailShape.EAST_WEST
        ? this.isSwitched(blockState)
            ? this.isMirrored(blockState) ? ArrowDirection.EAST : ArrowDirection.WEST
            : this.isMirrored(blockState) ? ArrowDirection.WEST : ArrowDirection.EAST
        : this.isSwitched(blockState)
            ? this.isMirrored(blockState) ? ArrowDirection.NORTH : ArrowDirection.SOUTH
            : this.isMirrored(blockState) ? ArrowDirection.SOUTH : ArrowDirection.NORTH;
  }

  @Override
  public ArrowDirection getWhiteArrowDirection(BlockState blockState) {
    return this.getRailDirectionRaw() == RailShape.EAST_WEST
        ? this.isMirrored(blockState) ? ArrowDirection.NORTH : ArrowDirection.SOUTH
        : this.isMirrored(blockState) ? ArrowDirection.WEST : ArrowDirection.EAST;
  }

  @Override
  public Direction getActuatorDirection(BlockState blockState) {
    return this.getRailDirectionRaw() == RailShape.EAST_WEST
        ? this.isMirrored(blockState) ? Direction.SOUTH : Direction.NORTH
        : this.isMirrored(blockState) ? Direction.EAST : Direction.WEST;
  }
}
