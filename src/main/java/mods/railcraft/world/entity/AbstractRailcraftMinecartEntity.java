package mods.railcraft.world.entity;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import mods.railcraft.api.carts.IItemCart;
import mods.railcraft.carts.CartTools;
import mods.railcraft.carts.RailcraftCart;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.util.TrackTools;
import mods.railcraft.util.inventory.wrappers.InventoryMapper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.ContainerMinecartEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * It also contains some generic code that most carts will find useful.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class AbstractRailcraftMinecartEntity extends ContainerMinecartEntity
    implements RailcraftCart, IItemCart {

  private final Direction[] travelDirectionHistory = new Direction[2];
  protected @Nullable Direction travelDirection;
  protected @Nullable Direction verticalTravelDirection;
  protected List<InventoryMapper> invMappers = new ArrayList<>();

  protected AbstractRailcraftMinecartEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  protected AbstractRailcraftMinecartEntity(EntityType<?> type, double x, double y, double z,
      World world) {
    super(type, x, y, z, world);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    cartInit();
  }

  @Override
  protected void addAdditionalSaveData(CompoundNBT compound) {
    super.addAdditionalSaveData(compound);
    saveToNBT(compound);
  }

  @Override
  protected void readAdditionalSaveData(CompoundNBT compound) {
    super.readAdditionalSaveData(compound);
    loadFromNBT(compound);
  }

  @Override
  public ActionResultType interact(PlayerEntity player, Hand hand) {
    NetworkHooks.openGui((ServerPlayerEntity) player, this,
        data -> data.writeVarInt(this.getId()));
    if (!player.level.isClientSide()) {
      PiglinTasks.angerNearbyPiglins(player, true);
      return ActionResultType.CONSUME;
    } else {
      return ActionResultType.SUCCESS;
    }
  }

  @Override
  public final ItemStack getCartItem() {
    return createCartItem(this);
  }

  @Override
  public void remove() {
    if (this.level.isClientSide())
      for (int slot = 0; slot < getContainerSize(); slot++) {
        setItem(slot, ItemStack.EMPTY);
      }
    super.remove();
  }

  @Override
  public final void destroy(DamageSource par1DamageSource) {
    killAndDrop(this);
  }

  /**
   * {@link net.minecraft.entity.item.EntityArmorStand#IS_RIDEABLE_MINECART}
   */
  @Override
  public AbstractMinecartEntity.Type getMinecartType() {
    throw new RuntimeException("This method should NEVER be called");
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
    RailShape shape =
        TrackTools.getTrackDirection(level, pos, state);
    @Nullable
    Direction facing = determineTravelDirection(shape);
    @Nullable
    Direction previousEnumFacing = travelDirectionHistory[1];
    if (previousEnumFacing != null && travelDirectionHistory[0] == previousEnumFacing) {
      travelDirection = facing;
      verticalTravelDirection = determineVerticalTravelDirection(shape);
    }
    travelDirectionHistory[0] = previousEnumFacing;
    travelDirectionHistory[1] = facing;
  }

  private @Nullable Direction determineTravelDirection(RailShape shape) {
    if (TrackShapeHelper.isStraight(shape)) {
      if (getX() - xo > 0)
        return Direction.EAST;
      if (getX() - xo < 0)
        return Direction.WEST;
      if (getZ() - zo > 0)
        return Direction.SOUTH;
      if (getZ() - zo < 0)
        return Direction.NORTH;
    } else {
      switch (shape) {
        case SOUTH_EAST:
          if (zo > getZ())
            return Direction.EAST;
          else
            return Direction.SOUTH;
        case SOUTH_WEST:
          if (zo > getZ())
            return Direction.WEST;
          else
            return Direction.SOUTH;
        case NORTH_WEST:
          if (zo > getZ())
            return Direction.NORTH;
          else
            return Direction.WEST;
        case NORTH_EAST:
          if (zo > getZ())
            return Direction.NORTH;
          else
            return Direction.EAST;
        default:
          break;
      }
    }
    return null;
  }

  private @Nullable Direction determineVerticalTravelDirection(RailShape shape) {
    if (shape.isAscending())
      return this.yo < getY() ? Direction.UP : Direction.DOWN;
    return null;
  }

  @Override
  public boolean canPassItemRequests(ItemStack stack) {
    return false;
  }

  @Override
  public boolean canAcceptPushedItem(AbstractMinecartEntity requester, ItemStack stack) {
    return false;
  }

  @Override
  public boolean canProvidePulledItem(AbstractMinecartEntity requester, ItemStack stack) {
    return false;
  }

  @Override
  public boolean canPlaceItem(int index, ItemStack stack) {
    return invMappers.stream().filter(m -> m.containsSlot(index))
        .allMatch(m -> m.filter().test(stack));
  }

  /**
   * Checks if the entity is in range to render.
   */
  @Override
  public boolean shouldRenderAtSqrDistance(double distance) {
    return CartTools.isInRangeToRenderDist(this, distance);
  }
}
