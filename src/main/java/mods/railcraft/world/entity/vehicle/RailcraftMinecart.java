package mods.railcraft.world.entity.vehicle;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.carts.IItemCart;
import mods.railcraft.api.track.RailShapeUtil;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.season.Season;
import mods.railcraft.util.container.ContainerMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.network.NetworkHooks;

/**
 * Basetype of RC minecarts. It also contains some generic code that most carts will find useful.
 *
 * @author CovertJaguar (https://www.railcraft.info)
 */
public abstract class RailcraftMinecart extends AbstractMinecartContainer
    implements SeasonalCart, IItemCart {

  private static final EntityDataAccessor<Byte> SEASON =
      SynchedEntityData.defineId(RailcraftMinecart.class, EntityDataSerializers.BYTE);

  private final Direction[] travelDirectionHistory = new Direction[2];
  protected @Nullable Direction travelDirection;
  protected @Nullable Direction verticalTravelDirection;
  protected List<ContainerMapper> invMappers = new ArrayList<>();

  protected RailcraftMinecart(EntityType<?> type, Level level) {
    super(type, level);
  }

  protected RailcraftMinecart(EntityType<?> type, double x, double y, double z, Level level) {
    super(type, x, y, z, level);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(SEASON, (byte) Season.DEFAULT.ordinal());
  }

  @Override
  public Season getSeason() {
    return Season.values()[this.entityData.get(SEASON)];
  }

  @Override
  public void setSeason(Season season) {
    this.entityData.set(SEASON, (byte) season.ordinal());
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putInt("season", this.getSeason().ordinal());
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    var season = Season.DEFAULT;
    if (tag.contains("season"))
      season = Season.values()[tag.getInt("season")];
    this.setSeason(season);
  }

  @Override
  public InteractionResult interact(Player player, InteractionHand hand) {
    if (!player.level.isClientSide()) {
      if (this.hasMenu()) {
        NetworkHooks.openScreen((ServerPlayer) player, this,
            data -> data.writeVarInt(this.getId()));
      }
      PiglinAi.angerNearbyPiglins(player, true);
    }
    return InteractionResult.sidedSuccess(this.level.isClientSide());
  }

  protected boolean hasMenu() {
    return true;
  }

  @Override
  public void remove(RemovalReason reason) {
    if (this.level.isClientSide()) {
      for (int slot = 0; slot < this.getContainerSize(); slot++) {
        this.setItem(slot, ItemStack.EMPTY);
      }
    }
    super.remove(reason);
  }

  @Override
  public AbstractMinecart.Type getMinecartType() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isPoweredCart() {
    return false;
  }

  @Override
  public boolean canBeRidden() {
    return false;
  }

  protected void updateTravelDirection(BlockPos pos, BlockState state) {
    var shape = TrackUtil.getTrackDirection(this.level, pos, state);

    @Nullable
    Direction facing = this.determineTravelDirection(shape);
    @Nullable
    Direction previousEnumFacing = travelDirectionHistory[1];

    if (previousEnumFacing != null && travelDirectionHistory[0] == previousEnumFacing) {
      travelDirection = facing;
      verticalTravelDirection = determineVerticalTravelDirection(shape);
    }
    travelDirectionHistory[0] = previousEnumFacing;
    travelDirectionHistory[1] = facing;
  }

  @Nullable
  private Direction determineTravelDirection(RailShape shape) {
    if (RailShapeUtil.isStraight(shape)) {
      if (getX() - xo > 0) {
        return Direction.EAST;
      }
      if (getX() - xo < 0) {
        return Direction.WEST;
      }
      if (getZ() - zo > 0) {
        return Direction.SOUTH;
      }
      if (getZ() - zo < 0) {
        return Direction.NORTH;
      }
    } else {
      switch (shape) {
        case SOUTH_EAST:
          if (zo > getZ()) {
            return Direction.EAST;
          } else {
            return Direction.SOUTH;
          }
        case SOUTH_WEST:
          if (zo > getZ()) {
            return Direction.WEST;
          } else {
            return Direction.SOUTH;
          }
        case NORTH_WEST:
          if (zo > getZ()) {
            return Direction.NORTH;
          } else {
            return Direction.WEST;
          }
        case NORTH_EAST:
          if (zo > getZ()) {
            return Direction.NORTH;
          } else {
            return Direction.EAST;
          }
        default:
          break;
      }
    }
    return null;
  }

  private @Nullable Direction determineVerticalTravelDirection(RailShape shape) {
    if (shape.isAscending()) {
      return this.yo < getY() ? Direction.UP : Direction.DOWN;
    }
    return null;
  }

  @Override
  public boolean canPassItemRequests(ItemStack stack) {
    return false;
  }

  @Override
  public boolean canAcceptPushedItem(AbstractMinecart requester, ItemStack stack) {
    return false;
  }

  @Override
  public boolean canProvidePulledItem(AbstractMinecart requester, ItemStack stack) {
    return false;
  }

  @Override
  public boolean canPlaceItem(int index, ItemStack stack) {
    return this.invMappers.stream().filter(m -> m.containsSlot(index))
        .allMatch(m -> m.filter().test(stack));
  }

  /**
   * Checks if the entity is in range to render.
   */
  @Override
  public boolean shouldRenderAtSqrDistance(double distance) {
    return CartTools.isInRangeToRenderDist(this, distance);
  }

  @Override
  public Packet<ClientGamePacketListener> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
