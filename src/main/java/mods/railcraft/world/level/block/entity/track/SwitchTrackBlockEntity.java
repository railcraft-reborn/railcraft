package mods.railcraft.world.level.block.entity.track;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;
import com.google.common.collect.Sets;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.track.ArrowDirection;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.RailcraftNBTUtil;
import mods.railcraft.world.entity.vehicle.CartTools;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SwitchTrackBlockEntity extends BlockEntity {

  private static final int SPRING_DURATION = 30;
  protected Set<UUID> lockingCarts = new HashSet<>();
  protected Set<UUID> springingCarts = new HashSet<>();
  protected Set<UUID> decidingCarts = new HashSet<>();
  private byte sprung;
  private byte locked;
  @Nullable
  private RollingStock currentCart;

  @Nullable
  private UUID unresolvedCurrentCart;

  public SwitchTrackBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
    super(type, blockPos, blockState);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      SwitchTrackBlockEntity blockEntity) {
    if (blockEntity.locked > 0) {
      blockEntity.locked--;
    }

    if (blockEntity.sprung > 0) {
      blockEntity.sprung--;
    }

    if (blockEntity.locked == 0 && blockEntity.sprung == 0) {
      blockEntity.lockingCarts.clear(); // Clear out our sets so we don't keep
      blockEntity.springingCarts.clear(); // these carts forever
      blockEntity.decidingCarts.clear();
      blockEntity.currentCart = null;
    }

    // updating carts we just found in appropriate sets
    // blockEntity keeps exiting carts from getting mixed up with entering carts
    blockEntity.updateSet(blockEntity.lockingCarts, blockEntity.getCartsAtLockEntrance(),
        blockEntity.springingCarts, blockEntity.decidingCarts);
    blockEntity.updateSet(blockEntity.springingCarts, blockEntity.getCartsAtSpringEntrance(),
        blockEntity.lockingCarts, blockEntity.decidingCarts);
    blockEntity.updateSet(blockEntity.decidingCarts, blockEntity.getCartsAtDecisionEntrance(),
        blockEntity.lockingCarts, blockEntity.springingCarts);

    // We only set sprung/locked when a cart enters our track, blockEntity is
    // mainly for visual purposes as the subclass's getRailDirectionRaw()
    // determines which direction the carts actually take.
    List<AbstractMinecart> cartsOnTrack = EntitySearcher.findMinecarts()
        .at(blockPos)
        .inflate(-0.3f)
        .list(blockEntity.getLevel());
    Set<UUID> uuidOnTrack = cartsOnTrack.stream()
        .map(Entity::getUUID)
        .collect(Collectors.toSet());

    AbstractMinecart bestCart = blockEntity.getBestCartForVisualState(cartsOnTrack);

    boolean wasSwitched = SwitchTrackBlock.isSwitched(blockState);
    BlockPos actuatorBlockPos = blockEntity.getActuatorBlockPos();
    BlockState actuatorBlockState = blockEntity.level.getBlockState(actuatorBlockPos);
    boolean actuatorPresent = actuatorBlockState.is(RailcraftTags.Blocks.SWITCH_TRACK_ACTUATOR);
    boolean actuatorSwitched = actuatorPresent
        && SwitchTrackActuatorBlock.isSwitched(actuatorBlockState);
    boolean switched = !blockEntity.isLocked() && (actuatorSwitched || blockEntity.isSprung());

    // Only allow cartsOnTrack to actually spring or lock the track
    if (bestCart != null && uuidOnTrack.contains(bestCart.getUUID())) {
      if (blockEntity.shouldSwitchForCart(bestCart)) {
        blockEntity.springTrack(RollingStock.getOrThrow(bestCart));
      } else {
        blockEntity.lockTrack(RollingStock.getOrThrow(bestCart));
      }
    }

    if (switched != wasSwitched) {
      blockEntity.level.setBlockAndUpdate(blockEntity.getBlockPos(),
          blockState.setValue(SwitchTrackBlock.SWITCHED, switched));
      if (actuatorPresent) {
        if (actuatorSwitched != switched) {
          SwitchTrackActuatorBlock.setSwitched(
              actuatorBlockState, blockEntity.level, actuatorBlockPos, switched);

        }
        SwitchTrackActuatorBlock.updateArrowDirections(actuatorBlockState, blockEntity.level,
            actuatorBlockPos, blockEntity.getRedArrowDirection(),
            blockEntity.getWhiteArrowDirection());
      }
    }
  }

  protected abstract ArrowDirection getRedArrowDirection();

  protected abstract ArrowDirection getWhiteArrowDirection();

  // To render the state of the track most accurately, we choose the "best" cart from our set of
  // carts based on distance.
  @Nullable
  private AbstractMinecart getBestCartForVisualState(
      List<AbstractMinecart> cartsOnTrack) {
    if (!cartsOnTrack.isEmpty()) {
      return cartsOnTrack.get(0);
    } else {
      AbstractMinecart closestCart = null;
      List<UUID> allCarts = new ArrayList<>();
      allCarts.addAll(lockingCarts);
      allCarts.addAll(springingCarts);
      allCarts.addAll(decidingCarts);

      for (UUID testCartUUID : allCarts) {
        if (closestCart == null) {
          closestCart = CartTools.getCartFromUUID(this.level, testCartUUID);
        } else {
          double closestDist = crudeDistance(this.getBlockPos(), closestCart);
          AbstractMinecart testCart = CartTools.getCartFromUUID(this.level, testCartUUID);
          if (testCart != null) {
            double testDist = crudeDistance(this.getBlockPos(), testCart);
            if (testDist < closestDist)
              closestCart = testCart;
          }
        }
      }
      return closestCart;
    }
  }

  protected abstract List<UUID> getCartsAtLockEntrance();

  protected abstract List<UUID> getCartsAtSpringEntrance();

  protected abstract List<UUID> getCartsAtDecisionEntrance();

  public abstract Direction getActuatorDirection();

  public final BlockPos getActuatorBlockPos() {
    return this.getBlockPos().relative(this.getActuatorDirection());
  }

  /**
   * This is a method provided to the subclasses to determine more accurately for the passed in cart
   * whether the switch is sprung or not. It caches the server responses for the clients to use.
   * Note: This method should not modify any variables except the cache, we leave that to update().
   */
  public boolean shouldSwitchForCart(@Nullable AbstractMinecart cart) {
    if (cart == null || this.level.isClientSide())
      return false;

    if (this.springingCarts.contains(cart.getUUID()))
      return true; // Carts at the spring entrance always are on switched tracks

    if (this.lockingCarts.contains(cart.getUUID()))
      return false; // Carts at the locking entrance always are on locked tracks

    var sameTrain = RollingStock.getOrThrow(cart).isSameTrainAs(this.currentCart());

    BlockState actuatorBlockState = this.level.getBlockState(this.getActuatorBlockPos());
    boolean shouldSwitch = actuatorBlockState.is(RailcraftTags.Blocks.SWITCH_TRACK_ACTUATOR)
        && SwitchTrackActuatorBlock.isSwitched(actuatorBlockState);

    if (this.isSprung()) {
      // we're either same train or switched so return true
      return shouldSwitch || sameTrain;
      // detected new train, we can safely treat this as not switched
    }

    if (this.isLocked()) {
      // detected new train, we can safely treat this as switched
      return shouldSwitch && !sameTrain;
      // other cases we obey locked
    }

    // we're not sprung or locked so we should return shouldSwitch
    return shouldSwitch;
  }

  @Nullable
  private RollingStock currentCart() {
    if (this.unresolvedCurrentCart != null) {
      var entity = ((ServerLevel) this.level).getEntity(this.unresolvedCurrentCart);
      if (entity instanceof AbstractMinecart minecart) {
        this.currentCart = RollingStock.getOrThrow(minecart);
      }
      this.unresolvedCurrentCart = null;
    }
    return this.currentCart;
  }

  private void springTrack(RollingStock cartOnTrack) {
    this.sprung = SPRING_DURATION;
    this.locked = 0;
    this.currentCart = cartOnTrack;
  }

  private void lockTrack(RollingStock cartOnTrack) {
    this.locked = SPRING_DURATION;
    this.sprung = 0;
    this.currentCart = cartOnTrack;
  }

  public boolean isLocked() {
    return this.locked > 0;
  }

  public boolean isSprung() {
    return this.sprung > 0;
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putByte("sprung", this.sprung);
    tag.putByte("locked", this.locked);
    tag.put("springingCarts", RailcraftNBTUtil.createUUIDArray(this.springingCarts));
    tag.put("lockingCarts", RailcraftNBTUtil.createUUIDArray(this.lockingCarts));
    tag.put("decidingCarts", RailcraftNBTUtil.createUUIDArray(this.decidingCarts));
    if (this.currentCart != null) {
      tag.putUUID("currentCart", this.currentCart.entity().getUUID());
    }
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.sprung = tag.getByte("sprung");
    this.locked = tag.getByte("locked");
    this.springingCarts = Sets.newHashSet(
        RailcraftNBTUtil
            .loadUUIDArray(tag.getList("springingCarts", RailcraftNBTUtil.UUID_TAG_TYPE)));
    this.lockingCarts = Sets.newHashSet(
        RailcraftNBTUtil
            .loadUUIDArray(tag.getList("lockingCarts", RailcraftNBTUtil.UUID_TAG_TYPE)));
    this.decidingCarts = Sets.newHashSet(
        RailcraftNBTUtil
            .loadUUIDArray(tag.getList("decidingCarts", RailcraftNBTUtil.UUID_TAG_TYPE)));
    this.unresolvedCurrentCart = tag.hasUUID("currentCart") ? tag.getUUID("currentCart") : null;
  }

  private void updateSet(Set<UUID> setToUpdate, List<UUID> potentialUpdates, Set<UUID> reject1,
      Set<UUID> reject2) {
    for (UUID cartUUID : potentialUpdates) {
      reject1.remove(cartUUID);
      reject2.remove(cartUUID);
      setToUpdate.add(cartUUID);
    }
  }

  private static double crudeDistance(BlockPos pos, AbstractMinecart cart) {
    double cx = pos.getX() + .5; // Why not calc this outside and cache it?
    double cz = pos.getZ() + .5; // b/c this is a rare occurrence that we need to calc this
    return Math.abs(cart.getX() - cx) + Math.abs(cart.getZ() - cz); // not the real distance
                                                                    // function
    // but enough for us
  }
}
