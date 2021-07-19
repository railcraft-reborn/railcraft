package mods.railcraft.world.level.block.track.outfitted.kit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import mods.railcraft.api.tracks.ISwitchMotor;
import mods.railcraft.api.tracks.ISwitchMotor.ArrowDirection;
import mods.railcraft.api.tracks.ITrackKitSwitch;
import mods.railcraft.carts.CartTools;
import mods.railcraft.carts.Train;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class TrackKitSwitch extends TrackKitRailcraft implements ITrackKitSwitch {

  private static final int SPRING_DURATION = 30;
  protected boolean mirrored;
  protected boolean shouldSwitch;
  protected Set<UUID> lockingCarts = new HashSet<>();
  protected Set<UUID> springingCarts = new HashSet<>();
  protected Set<UUID> decidingCarts = new HashSet<>();
  private byte sprung;
  private byte locked;
  private @Nullable UUID currentCart;
  private @Nullable ISwitchMotor switchDevice;
  private boolean clientSwitched;

  @Override
  public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
    return Collections.emptyList();
  }

  @Override
  public boolean isMirrored() {
    return mirrored;
  }

  @Override
  public boolean isVisuallySwitched() {
    if (!theWorldAsserted().isClientSide())
      return !isLocked() && (shouldSwitch || isSprung());
    return clientSwitched;
  }

  /**
   * This is a method provided to the subclasses to determine more accurately for the passed in cart
   * whether the switch is sprung or not. It caches the server responses for the clients to use.
   * Note: This method should not modify any variables except the cache, we leave that to update().
   */
  protected boolean shouldSwitchForCart(@Nullable AbstractMinecartEntity cart) {
    if (cart == null || theWorldAsserted().isClientSide())
      return isVisuallySwitched();

    if (springingCarts.contains(cart.getUUID()))
      return true; // Carts at the spring entrance always are on switched tracks

    if (lockingCarts.contains(cart.getUUID()))
      return false; // Carts at the locking entrance always are on locked tracks

    boolean sameTrain = Train.get(cart).map(t -> t.contains(currentCart)).orElse(false);

    boolean shouldSwitch = (switchDevice != null) && switchDevice.shouldSwitch(cart);

    if (isSprung()) {
      // we're either same train or switched so return true
      return shouldSwitch || sameTrain;
      // detected new train, we can safely treat this as not switched
    }

    if (isLocked()) {
      // detected new train, we can safely treat this as switched
      return shouldSwitch && !sameTrain;
      // other cases we obey locked
    }

    // we're not sprung or locked so we should return shouldSwitch
    return shouldSwitch;
  }

  private void springTrack(UUID cartOnTrack) {
    sprung = SPRING_DURATION;
    locked = 0;
    currentCart = cartOnTrack;
  }

  private void lockTrack(UUID cartOnTrack) {
    locked = SPRING_DURATION;
    sprung = 0;
    currentCart = cartOnTrack;
  }

  public boolean isLocked() {
    return locked > 0;
  }

  public boolean isSprung() {
    return sprung > 0;
  }

  @Override
  public void setPlacedBy(BlockState state, @Nullable LivingEntity placer,
      ItemStack stack) {
    determineRailDirection();
    determineMirror(false);

    // Notify any neighboring switches that we exist so they know to register themselves with us
    ((RailcraftBlockEntity) getTile()).notifyBlocksOfNeighborChange();
  }

  @Override
  public void onBlockRemoved() {
    super.onBlockRemoved();
    // Notify any neighboring switches that we exist so they know to register themselves with us
    ((RailcraftBlockEntity) getTile()).notifyBlocksOfNeighborChange();
  }

  protected void determineRailDirection() {
    World world = theWorldAsserted();
    RailShape dir = TrackTools.getTrackDirectionRaw(world.getBlockState(getPos()));
    if (AbstractRailBlock.isRail(world, getPos().east())
        && AbstractRailBlock.isRail(world, getPos().west())) {
      if (dir != RailShape.EAST_WEST)
        TrackTools.setTrackDirection(world, getPos(), RailShape.EAST_WEST);
      // } else if (TrackTools.isRailBlockAt(world, getPos().north()) &&
      // TrackTools.isRailBlockAt(world, getPos().south())) {
      // if (dir != RailShape.NORTH_SOUTH)
      // TrackTools.setTrackDirection(world, getPos(), RailShape.NORTH_SOUTH);
    } else if (dir != RailShape.NORTH_SOUTH)
      TrackTools.setTrackDirection(world, getPos(), RailShape.NORTH_SOUTH);
  }

  protected void determineMirror(boolean updateClient) {
    World world = theWorldAsserted();
    RailShape dir = TrackTools.getTrackDirection(world, getPos());
    boolean prevValue = isMirrored();
    if (dir == RailShape.NORTH_SOUTH) {
      BlockPos offset = getPos();
      if (AbstractRailBlock.isRail(world, offset.west())) {
        offset = offset.west();
        mirrored = true; // West
      } else {
        offset = offset.east();
        mirrored = false; // East
      }
      if (AbstractRailBlock.isRail(world, offset)) {
        RailShape otherDir = TrackTools.getTrackDirection(world, offset);
        if (otherDir == RailShape.NORTH_SOUTH)
          TrackTools.setTrackDirection(world, offset, RailShape.EAST_WEST);
      }
    } else if (dir == RailShape.EAST_WEST)
      mirrored = AbstractRailBlock.isRail(world, getPos().north());

    if (updateClient && prevValue != isMirrored())
      sendUpdateToClient();
  }

  @Override
  public void neighborChanged(BlockState state, @Nullable Block block) {
    if (!theWorldAsserted().isClientSide()) {
      determineRailDirection();
      determineMirror(true);
    }
    super.neighborChanged(state, block);
  }

  private void writeCartsToNBT(String key, Set<UUID> carts, CompoundNBT data) {
    data.putByte(key + "Size", (byte) carts.size());
    int i = 0;
    for (UUID uuid : carts)
      data.putUUID(key + i++, uuid);
  }

  private Set<UUID> readCartsFromNBT(String key, CompoundNBT data) {
    Set<UUID> cartUUIDs = new HashSet<>();
    String sizeKey = key + "Size";
    if (data.contains(sizeKey, Constants.NBT.TAG_BYTE)) {
      byte size = data.getByte(sizeKey);
      cartUUIDs = IntStream.range(0, size)
          .filter(i -> data.hasUUID(key + i))
          .mapToObj(i -> data.getUUID(key + i))
          .collect(Collectors.toSet());
    }
    return cartUUIDs;
  }

  @Override
  public void writeToNBT(CompoundNBT data) {
    super.writeToNBT(data);
    data.putBoolean("Direction", mirrored);
    data.putBoolean("Switched", shouldSwitch);
    data.putByte("sprung", sprung);
    data.putByte("locked", locked);
    writeCartsToNBT("springingCarts", springingCarts, data);
    writeCartsToNBT("lockingCarts", lockingCarts, data);
    writeCartsToNBT("decidingCarts", lockingCarts, data);
    data.putUUID("currentCart", currentCart);
  }

  @Override
  public void readFromNBT(CompoundNBT data) {
    super.readFromNBT(data);
    mirrored = data.getBoolean("Direction");
    shouldSwitch = data.getBoolean("Switched");
    sprung = data.getByte("sprung");
    locked = data.getByte("locked");
    springingCarts = readCartsFromNBT("springingCarts", data);
    lockingCarts = readCartsFromNBT("lockingCarts", data);
    decidingCarts = readCartsFromNBT("decidingCarts", data);
    currentCart = data.getUUID("currentCart");
  }

  @Override
  public void writePacketData(PacketBuffer data) {
    super.writePacketData(data);
    data.writeBoolean(mirrored);
    data.writeBoolean(isVisuallySwitched());
  }

  @Override
  public void readPacketData(PacketBuffer data) {
    super.readPacketData(data);
    boolean changed = false;
    boolean m = data.readBoolean();
    if (m != mirrored) {
      mirrored = m;
      changed = true;
    }
    boolean cs = data.readBoolean();
    if (cs != clientSwitched) {
      clientSwitched = cs;
      changed = true;
    }

    if (changed) {
      switchDevice = getSwitchDevice();
      if (switchDevice != null)
        switchDevice.updateArrows();
      markBlockNeedsUpdate();
    }
  }

  private void updateSet(Set<UUID> setToUpdate, List<UUID> potentialUpdates, Set<UUID> reject1,
      Set<UUID> reject2) {
    for (UUID cartUUID : potentialUpdates) {
      reject1.remove(cartUUID);
      reject2.remove(cartUUID);
      setToUpdate.add(cartUUID);
    }
  }

  @Override
  public void tick() {
    super.tick();

    boolean wasSwitched = isVisuallySwitched();

    if (locked > 0)
      locked--;
    if (sprung > 0)
      sprung--;

    if (locked == 0 && sprung == 0) {
      lockingCarts.clear(); // Clear out our sets so we don't keep
      springingCarts.clear(); // these carts forever
      decidingCarts.clear();
      currentCart = null;
    }

    // updating carts we just found in appropriate sets
    // this keeps exiting carts from getting mixed up with entering carts
    updateSet(lockingCarts, getCartsAtLockEntrance(), springingCarts, decidingCarts);
    updateSet(springingCarts, getCartsAtSpringEntrance(), lockingCarts, decidingCarts);
    updateSet(decidingCarts, getCartsAtDecisionEntrance(), lockingCarts, springingCarts);

    // We only set sprung/locked when a cart enters our track, this is
    // mainly for visual purposes as the subclass's getRailDirectionRaw()
    // determines which direction the carts actually take.
    List<AbstractMinecartEntity> cartsOnTrack =
        EntitySearcher.findMinecarts().around(getPos()).outTo(-0.3f).in(theWorldAsserted());
    Set<UUID> uuidOnTrack =
        cartsOnTrack.stream().map(Entity::getUUID).collect(Collectors.toSet());

    AbstractMinecartEntity bestCart = getBestCartForVisualState(cartsOnTrack);

    // We must ask the switch every tick so we can update shouldSwitch properly
    switchDevice = getSwitchDevice();
    if (switchDevice == null) {
      shouldSwitch = false;
    } else {
      shouldSwitch = switchDevice.shouldSwitch(bestCart);
    }

    // Only allow cartsOnTrack to actually spring or lock the track
    if (bestCart != null && uuidOnTrack.contains(bestCart.getUUID())) {
      if (shouldSwitchForCart(bestCart)) {
        springTrack(bestCart.getUUID());
      } else {
        lockTrack(bestCart.getUUID());
      }
    }

    if (isVisuallySwitched() != wasSwitched) {
      if (switchDevice != null) {
        switchDevice.onSwitch(isVisuallySwitched());
      }
      sendUpdateToClient();
    }
  }

  private double crudeDistance(AbstractMinecartEntity cart) {
    double cx = getPos().getX() + .5; // Why not calc this outside and cache it?
    double cz = getPos().getZ() + .5; // b/c this is a rare occurrence that we need to calc this
    return Math.abs(cart.getX() - cx) + Math.abs(cart.getZ() - cz); // not the real distance
                                                                    // function
    // but enough for us
  }

  // To render the state of the track most accurately, we choose the "best" cart from our set of
  // carts based on distance.
  private @Nullable AbstractMinecartEntity getBestCartForVisualState(
      List<AbstractMinecartEntity> cartsOnTrack) {
    World world = theWorldAsserted();
    if (!cartsOnTrack.isEmpty()) {
      return cartsOnTrack.get(0);
    } else {
      AbstractMinecartEntity closestCart = null;
      ArrayList<UUID> allCarts = new ArrayList<>();
      allCarts.addAll(lockingCarts);
      allCarts.addAll(springingCarts);
      allCarts.addAll(decidingCarts);

      for (UUID testCartUUID : allCarts) {
        if (closestCart == null) {
          closestCart = CartTools.getCartFromUUID(world, testCartUUID);
        } else {
          double closestDist = crudeDistance(closestCart);
          AbstractMinecartEntity testCart = CartTools.getCartFromUUID(world, testCartUUID);
          if (testCart != null) {
            double testDist = crudeDistance(testCart);
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

  public abstract Direction getActuatorLocation();

  public abstract ArrowDirection getRedSignDirection();

  public abstract ArrowDirection getWhiteSignDirection();

  public @Nullable ISwitchMotor getSwitchDevice() {
    TileEntity entity =
        ((RailcraftBlockEntity) getTile()).getAdjacentCache().getTileOnSide(getActuatorLocation());
    if (entity instanceof ISwitchMotor) {
      return (ISwitchMotor) entity;
    }
    return null;
  }
}
