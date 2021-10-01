package mods.railcraft.world.level.block.entity.track;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.google.common.collect.Sets;
import mods.railcraft.api.track.ArrowDirection;
import mods.railcraft.carts.CartTools;
import mods.railcraft.carts.Train;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.RailcraftNBTUtil;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public abstract class SwitchTrackBlockEntity extends TileEntity implements ITickableTileEntity {

  private static final int SPRING_DURATION = 30;
  protected Set<UUID> lockingCarts = new HashSet<>();
  protected Set<UUID> springingCarts = new HashSet<>();
  protected Set<UUID> decidingCarts = new HashSet<>();
  private byte sprung;
  private byte locked;
  @Nullable
  private UUID currentCart;

  public SwitchTrackBlockEntity(TileEntityType<?> type) {
    super(type);
  }


  @Override
  public void tick() {
    if (this.level.isClientSide()) {
      return;
    }

    if (this.locked > 0)
      this.locked--;
    if (this.sprung > 0)
      this.sprung--;

    if (this.locked == 0 && this.sprung == 0) {
      this.lockingCarts.clear(); // Clear out our sets so we don't keep
      this.springingCarts.clear(); // these carts forever
      this.decidingCarts.clear();
      this.currentCart = null;
    }

    final BlockState blockState = this.getBlockState();

    // updating carts we just found in appropriate sets
    // this keeps exiting carts from getting mixed up with entering carts
    this.updateSet(this.lockingCarts, this.getCartsAtLockEntrance(),
        this.springingCarts, this.decidingCarts);
    this.updateSet(this.springingCarts, this.getCartsAtSpringEntrance(),
        this.lockingCarts, this.decidingCarts);
    this.updateSet(this.decidingCarts, this.getCartsAtDecisionEntrance(),
        this.lockingCarts, this.springingCarts);

    // We only set sprung/locked when a cart enters our track, this is
    // mainly for visual purposes as the subclass's getRailDirectionRaw()
    // determines which direction the carts actually take.
    List<AbstractMinecartEntity> cartsOnTrack = EntitySearcher.findMinecarts()
        .around(getBlockPos())
        .outTo(-0.3f)
        .in(this.level);
    Set<UUID> uuidOnTrack = cartsOnTrack.stream()
        .map(Entity::getUUID)
        .collect(Collectors.toSet());

    AbstractMinecartEntity bestCart = this.getBestCartForVisualState(cartsOnTrack);

    boolean wasSwitched = SwitchTrackBlock.isSwitched(blockState);
    BlockPos actuatorBlockPos = this.getActuatorBlockPos();
    BlockState actuatorBlockState = this.level.getBlockState(actuatorBlockPos);
    boolean actuatorPresent = actuatorBlockState.is(RailcraftTags.Blocks.SWITCH_TRACK_ACTUATOR);
    boolean actuatorSwitched = actuatorPresent
        && SwitchTrackActuatorBlock.isSwitched(actuatorBlockState);
    boolean switched = !this.isLocked() && (actuatorSwitched || this.isSprung());

    // Only allow cartsOnTrack to actually spring or lock the track
    if (bestCart != null && uuidOnTrack.contains(bestCart.getUUID())) {
      if (this.shouldSwitchForCart(bestCart)) {
        this.springTrack(bestCart.getUUID());
      } else {
        this.lockTrack(bestCart.getUUID());
      }
    }

    if (switched != wasSwitched) {
      this.level.setBlockAndUpdate(this.getBlockPos(),
          blockState.setValue(SwitchTrackBlock.SWITCHED, switched));
      if (actuatorPresent) {
        if (actuatorSwitched != switched) {
          SwitchTrackActuatorBlock.setSwitched(
              actuatorBlockState, this.level, actuatorBlockPos, switched);

        }
        SwitchTrackActuatorBlock.updateArrowDirections(actuatorBlockState, this.level,
            actuatorBlockPos, this.getRedArrowDirection(), this.getWhiteArrowDirection());
      }
    }
  }

  protected abstract ArrowDirection getRedArrowDirection();

  protected abstract ArrowDirection getWhiteArrowDirection();

  // To render the state of the track most accurately, we choose the "best" cart from our set of
  // carts based on distance.
  @Nullable
  private AbstractMinecartEntity getBestCartForVisualState(
      List<AbstractMinecartEntity> cartsOnTrack) {
    if (!cartsOnTrack.isEmpty()) {
      return cartsOnTrack.get(0);
    } else {
      AbstractMinecartEntity closestCart = null;
      List<UUID> allCarts = new ArrayList<>();
      allCarts.addAll(lockingCarts);
      allCarts.addAll(springingCarts);
      allCarts.addAll(decidingCarts);

      for (UUID testCartUUID : allCarts) {
        if (closestCart == null) {
          closestCart = CartTools.getCartFromUUID(this.level, testCartUUID);
        } else {
          double closestDist = crudeDistance(this.getBlockPos(), closestCart);
          AbstractMinecartEntity testCart = CartTools.getCartFromUUID(this.level, testCartUUID);
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
  public boolean shouldSwitchForCart(@Nullable AbstractMinecartEntity cart) {
    if (cart == null || this.level.isClientSide())
      return false;

    if (this.springingCarts.contains(cart.getUUID()))
      return true; // Carts at the spring entrance always are on switched tracks

    if (this.lockingCarts.contains(cart.getUUID()))
      return false; // Carts at the locking entrance always are on locked tracks

    boolean sameTrain = Train.get(cart).map(t -> t.contains(currentCart)).orElse(false);

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

  private void springTrack(UUID cartOnTrack) {
    this.sprung = SPRING_DURATION;
    this.locked = 0;
    this.currentCart = cartOnTrack;
  }

  private void lockTrack(UUID cartOnTrack) {
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
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putByte("sprung", this.sprung);
    data.putByte("locked", this.locked);
    data.put("springingCarts", RailcraftNBTUtil.createUUIDArray(this.springingCarts));
    data.put("lockingCarts", RailcraftNBTUtil.createUUIDArray(this.lockingCarts));
    data.put("decidingCarts", RailcraftNBTUtil.createUUIDArray(this.decidingCarts));
    if (this.currentCart != null) {
      data.putUUID("currentCart", this.currentCart);
    }
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.sprung = data.getByte("sprung");
    this.locked = data.getByte("locked");
    this.springingCarts = Sets.newHashSet(
        RailcraftNBTUtil.loadUUIDArray(data.getList("springingCarts", RailcraftNBTUtil.UUID_TAG_TYPE)));
    this.lockingCarts = Sets.newHashSet(
        RailcraftNBTUtil.loadUUIDArray(data.getList("lockingCarts", RailcraftNBTUtil.UUID_TAG_TYPE)));
    this.decidingCarts = Sets.newHashSet(
        RailcraftNBTUtil.loadUUIDArray(data.getList("decidingCarts", RailcraftNBTUtil.UUID_TAG_TYPE)));
    this.currentCart = data.hasUUID("currentCart") ? data.getUUID("currentCart") : null;
  }

  private void updateSet(Set<UUID> setToUpdate, List<UUID> potentialUpdates, Set<UUID> reject1,
      Set<UUID> reject2) {
    for (UUID cartUUID : potentialUpdates) {
      reject1.remove(cartUUID);
      reject2.remove(cartUUID);
      setToUpdate.add(cartUUID);
    }
  }

  private static double crudeDistance(BlockPos pos, AbstractMinecartEntity cart) {
    double cx = pos.getX() + .5; // Why not calc this outside and cache it?
    double cz = pos.getZ() + .5; // b/c this is a rare occurrence that we need to calc this
    return Math.abs(cart.getX() - cx) + Math.abs(cart.getZ() - cz); // not the real distance
                                                                    // function
    // but enough for us
  }
}
