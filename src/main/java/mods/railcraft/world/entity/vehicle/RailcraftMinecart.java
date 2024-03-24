package mods.railcraft.world.entity.vehicle;

import java.util.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.carts.ItemTransferHandler;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.network.RailcraftDataSerializers;
import mods.railcraft.season.Season;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

/**
 * Base type of RC minecarts. It also contains some generic code that most carts will find useful.
 */
public abstract class RailcraftMinecart extends AbstractMinecartContainer
    implements SeasonalCart, ItemTransferHandler {

  private static final EntityDataAccessor<Season> SEASON =
      SynchedEntityData.defineId(RailcraftMinecart.class, RailcraftDataSerializers.MINECART_SEASON);

  private final Direction[] travelDirectionHistory = new Direction[2];
  @Nullable
  private Direction travelDirection;
  @Nullable
  private Direction verticalTravelDirection;

  protected RailcraftMinecart(EntityType<?> type, Level level) {
    super(type, level);
  }

  protected RailcraftMinecart(EntityType<TunnelBore> type, double x, double y, double z,
      Level level) {
    super(type, x, y, z, level);
  }

  protected RailcraftMinecart(ItemStack itemStack, EntityType<?> type, double x, double y,
      double z, Level level) {
    super(type, x, y, z, level);
    this.loadCustomName(itemStack);
  }

  private void loadCustomName(ItemStack itemStack) {
    if (itemStack.hasCustomHoverName()) {
      this.setCustomName(itemStack.getHoverName());
    }
  }

  protected void loadFromItemStack(ItemStack itemStack) {
  }

  public Optional<Direction> travelDirection() {
    return Optional.ofNullable(this.travelDirection);
  }

  public Optional<Direction> verticalTravelDirection() {
    return Optional.ofNullable(this.verticalTravelDirection);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(SEASON, Season.DEFAULT);
  }

  @Override
  public Season getSeason() {
    return this.entityData.get(SEASON);
  }

  @Override
  public void setSeason(Season season) {
    this.entityData.set(SEASON, season);
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putString("season", this.getSeason().getSerializedName());
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    this.setSeason(Season.fromName(tag.getString("season")));
  }

  @Override
  public InteractionResult interact(Player player, InteractionHand hand) {
    if (player instanceof ServerPlayer serverPlayer) {
      if (this.hasMenu()) {
        serverPlayer.openMenu(this, data -> data.writeVarInt(this.getId()));
      }
      PiglinAi.angerNearbyPiglins(player, true);
    }
    return InteractionResult.sidedSuccess(this.level().isClientSide());
  }

  protected boolean hasMenu() {
    return true;
  }

  @Override
  public void remove(RemovalReason reason) {
    if (this.level().isClientSide()) {
      for (int slot = 0; slot < this.getContainerSize(); slot++) {
        this.setItem(slot, ItemStack.EMPTY);
      }
    }
    super.remove(reason);
  }

  @Override
  public final void destroy(DamageSource source) {
    this.kill();
    if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
      var itemstack = this.getPickResult().copy();
      if (this.hasCustomName()) {
        itemstack.setHoverName(this.getCustomName());
      }
      this.spawnAtLocation(itemstack);
    }
    this.chestVehicleDestroyed(source, this.level(), this);
  }

  @Override
  public ItemStack getPickResult() {
    var itemStack = this.getDropItem().getDefaultInstance();
    if (this.hasCustomName()) {
      itemStack.setHoverName(this.getCustomName());
    }
    return itemStack;
  }

  @Override
  protected Item getDropItem() {
    throw new NotImplementedException();
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
    var shape = TrackUtil.getTrackDirection(this.level(), pos, state);

    var direction = this.determineTravelDirection(shape);
    var lastDirection = this.travelDirectionHistory[1];

    if (lastDirection != null && this.travelDirectionHistory[0] == lastDirection) {
      this.travelDirection = direction;
      this.verticalTravelDirection = this.determineVerticalTravelDirection(shape);
    }
    this.travelDirectionHistory[0] = lastDirection;
    this.travelDirectionHistory[1] = direction;
  }

  @Nullable
  private Direction determineTravelDirection(RailShape shape) {
    return switch (shape) {
      case SOUTH_EAST -> {
        if (this.zo > this.getZ()) {
          yield Direction.EAST;
        } else {
          yield Direction.SOUTH;
        }
      }
      case SOUTH_WEST -> {
        if (this.zo > this.getZ()) {
          yield Direction.WEST;
        } else {
          yield Direction.SOUTH;
        }
      }
      case NORTH_WEST -> {
        if (this.zo > this.getZ()) {
          yield Direction.NORTH;
        } else {
          yield Direction.WEST;
        }
      }
      case NORTH_EAST -> {
        if (this.zo > this.getZ()) {
          yield Direction.NORTH;
        } else {
          yield Direction.EAST;
        }
      }
      default -> {
        if (this.getX() - this.xo > 0) {
          yield Direction.EAST;
        }
        if (this.getX() - this.xo < 0) {
          yield Direction.WEST;
        }
        if (this.getZ() - this.zo > 0) {
          yield Direction.SOUTH;
        }
        if (this.getZ() - this.zo < 0) {
          yield Direction.NORTH;
        }
        yield null;
      }
    };
  }

  @Nullable
  private Direction determineVerticalTravelDirection(RailShape shape) {
    return shape.isAscending() ? this.yo < getY() ? Direction.UP : Direction.DOWN : null;
  }

  @Override
  public boolean canPassItemRequests(ItemStack stack) {
    return false;
  }

  @Override
  public boolean canAcceptPushedItem(RollingStock requester, ItemStack stack) {
    return false;
  }

  @Override
  public boolean canProvidePulledItem(RollingStock requester, ItemStack stack) {
    return false;
  }

  @Override
  public boolean shouldRenderAtSqrDistance(double distance) {
    return MinecartUtil.isInRangeToRenderDist(this, distance);
  }
}
