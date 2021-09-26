package mods.railcraft.world.level.block.track.kit;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import mods.railcraft.api.tracks.ArrowDirection;
import mods.railcraft.api.tracks.ITrackKitReversible;
import mods.railcraft.api.tracks.TrackKit;
import mods.railcraft.carts.CartTools;
import mods.railcraft.world.level.block.track.outfitted.ReversableSwitchTrackBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TurnoutSwitchTrackKit extends SwitchTrackKit implements ITrackKitReversible {

  @Override
  public TrackKit getTrackKit() {
    return TrackKits.TURNOUT.get();
  }

  @Override
  public RailShape getRailDirection(BlockState blockState, @Nullable AbstractMinecartEntity cart) {
    RailShape current = super.getRailDirection(blockState, cart);
    if (cart != null && shouldSwitchForCart(cart)) {
      if (current == RailShape.NORTH_SOUTH) {
        if (this.isMirrored(blockState)) {
          return this.isReversed(blockState) ? RailShape.SOUTH_WEST : RailShape.NORTH_WEST;
        } else {
          return this.isReversed(blockState) ? RailShape.NORTH_EAST : RailShape.SOUTH_EAST;
        }
      } else if (current == RailShape.EAST_WEST) {
        if (this.isMirrored(blockState)) {
          return this.isReversed(blockState) ? RailShape.NORTH_WEST : RailShape.NORTH_EAST;
        } else {
          return this.isReversed(blockState) ? RailShape.SOUTH_EAST : RailShape.SOUTH_WEST;
        }
      }
    }
    return current;
  }

  @Override
  protected List<UUID> getCartsAtLockEntrance(BlockState blockState) {
    RailShape dir = getRailDirectionRaw();
    BlockPos offset = getPos();
    if (dir == RailShape.NORTH_SOUTH) {
      offset = this.isReversed(blockState) != this.isMirrored(blockState)
          ? offset.south()
          : offset.north();
    } else if (dir == RailShape.EAST_WEST) {
      offset = this.isReversed(blockState) == this.isMirrored(blockState)
          ? offset.east()
          : offset.west();
    }
    return CartTools.getMinecartUUIDsAt(theWorldAsserted(), offset, 0.1f);
  }

  @Override
  protected List<UUID> getCartsAtDecisionEntrance(BlockState blockState) {
    RailShape dir = getRailDirectionRaw();
    BlockPos offset = getPos();
    if (dir == RailShape.NORTH_SOUTH) {
      offset = this.isReversed(blockState) != this.isMirrored(blockState)
          ? offset.north()
          : offset.south();
    } else if (dir == RailShape.EAST_WEST) {
      offset = this.isReversed(blockState) == this.isMirrored(blockState)
          ? offset.west()
          : offset.east();
    }
    return CartTools.getMinecartUUIDsAt(theWorldAsserted(), offset, 0.1f);
  }

  @Override
  protected List<UUID> getCartsAtSpringEntrance(BlockState blockState) {
    RailShape dir = getRailDirectionRaw();
    BlockPos offset = getPos();
    if (dir == RailShape.NORTH_SOUTH) {
      offset = this.isMirrored(blockState) ? offset.west() : offset.east();
    } else if (dir == RailShape.EAST_WEST) {
      offset = this.isMirrored(blockState) ? offset.north() : offset.south();
    }
    return CartTools.getMinecartUUIDsAt(theWorldAsserted(), offset, 0.1f);
  }

  @Override
  public boolean isReversed(BlockState blockState) {
    return blockState.getValue(ReversableSwitchTrackBlock.REVERSED);
  }

  @Override
  public void setReversed(BlockState blockState, boolean reversed) {
    this.theWorldAsserted().setBlockAndUpdate(this.getPos(),
        blockState.setValue(ReversableSwitchTrackBlock.REVERSED, reversed));
  }

  @Override
  public ArrowDirection getRedArrowDirection(BlockState blockState) {
    if (getRailDirectionRaw() == RailShape.EAST_WEST) {
      if (this.isSwitched(blockState)) {
        return this.isMirrored(blockState) ? ArrowDirection.NORTH : ArrowDirection.SOUTH;
      }
      return this.isReversed(blockState) != this.isSwitched(blockState)
          ? ArrowDirection.EAST
          : ArrowDirection.WEST;
    }
    if (this.isSwitched(blockState)) {
      return this.isMirrored(blockState) ? ArrowDirection.WEST : ArrowDirection.EAST;
    }
    return this.isReversed(blockState) != this.isMirrored(blockState)
        ? ArrowDirection.NORTH
        : ArrowDirection.SOUTH;
  }

  @Override
  public ArrowDirection getWhiteArrowDirection(BlockState blockState) {
    if (getRailDirectionRaw() == RailShape.EAST_WEST) {
      return this.isSwitched(blockState) ? ArrowDirection.EAST_WEST : ArrowDirection.NORTH_SOUTH;
    }
    return this.isSwitched(blockState) ? ArrowDirection.NORTH_SOUTH : ArrowDirection.EAST_WEST;
  }

  @Override
  public Direction getActuatorDirection(BlockState blockState) {
    Direction face = Direction.NORTH;
    RailShape dir = getRailDirectionRaw();

    if (dir == RailShape.NORTH_SOUTH) {
      face = this.isMirrored(blockState) ? Direction.EAST : Direction.WEST;
    } else if (dir == RailShape.EAST_WEST) {
      face = this.isMirrored(blockState) ? Direction.SOUTH : Direction.NORTH;
    }
    return face;
  }
}
