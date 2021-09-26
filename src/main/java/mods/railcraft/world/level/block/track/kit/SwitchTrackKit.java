package mods.railcraft.world.level.block.track.kit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import mods.railcraft.api.tracks.ArrowDirection;
import mods.railcraft.api.tracks.ITrackKitInstance;
import mods.railcraft.carts.CartTools;
import mods.railcraft.carts.Train;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class SwitchTrackKit extends TrackKitRailcraft implements ITrackKitInstance {

  private static final int SPRING_DURATION = 30;
  protected Set<UUID> lockingCarts = new HashSet<>();
  protected Set<UUID> springingCarts = new HashSet<>();
  protected Set<UUID> decidingCarts = new HashSet<>();
  private byte sprung;
  private byte locked;
  private @Nullable UUID currentCart;

  @Override
  public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
    return Collections.emptyList();
  }

  /**
   * This is a method provided to the subclasses to determine more accurately for the passed in cart
   * whether the switch is sprung or not. It caches the server responses for the clients to use.
   * Note: This method should not modify any variables except the cache, we leave that to update().
   */
  protected boolean shouldSwitchForCart(@Nullable AbstractMinecartEntity cart) {
    if (cart == null || theWorldAsserted().isClientSide())
      return false;

    if (springingCarts.contains(cart.getUUID()))
      return true; // Carts at the spring entrance always are on switched tracks

    if (lockingCarts.contains(cart.getUUID()))
      return false; // Carts at the locking entrance always are on locked tracks

    boolean sameTrain = Train.get(cart).map(t -> t.contains(currentCart)).orElse(false);

    BlockState actuatorBlockState = this.theWorldAsserted().getBlockState(
        this.getActuatorBlockPos(this.theWorldAsserted().getBlockState(this.getPos())));
    boolean shouldSwitch = actuatorBlockState.is(RailcraftTags.Blocks.SWITCH_TRACK_ACTUATOR)
        && SwitchTrackActuatorBlock.isSwitched(actuatorBlockState);

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
    ((RailcraftBlockEntity) getBlockEntity()).notifyBlocksOfNeighborChange();
  }

  @Override
  public void onBlockRemoved() {
    super.onBlockRemoved();
    // Notify any neighboring switches that we exist so they know to register themselves with us
    ((RailcraftBlockEntity) getBlockEntity()).notifyBlocksOfNeighborChange();
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
    BlockState blockState = world.getBlockState(getPos());
    boolean wasMirrored = this.isMirrored(blockState);
    boolean mirrored = wasMirrored;
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

    if (updateClient && wasMirrored != mirrored)
      world.setBlockAndUpdate(getPos(), blockState.setValue(SwitchTrackBlock.MIRRORED, mirrored));
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
    sprung = data.getByte("sprung");
    locked = data.getByte("locked");
    springingCarts = readCartsFromNBT("springingCarts", data);
    lockingCarts = readCartsFromNBT("lockingCarts", data);
    decidingCarts = readCartsFromNBT("decidingCarts", data);
    currentCart = data.getUUID("currentCart");
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

    BlockState blockState = this.theWorldAsserted().getBlockState(getPos());

    // updating carts we just found in appropriate sets
    // this keeps exiting carts from getting mixed up with entering carts
    updateSet(lockingCarts, getCartsAtLockEntrance(blockState), springingCarts, decidingCarts);
    updateSet(springingCarts, getCartsAtSpringEntrance(blockState), lockingCarts, decidingCarts);
    updateSet(decidingCarts, getCartsAtDecisionEntrance(blockState), lockingCarts, springingCarts);

    // We only set sprung/locked when a cart enters our track, this is
    // mainly for visual purposes as the subclass's getRailDirectionRaw()
    // determines which direction the carts actually take.
    List<AbstractMinecartEntity> cartsOnTrack =
        EntitySearcher.findMinecarts().around(getPos()).outTo(-0.3f).in(theWorldAsserted());
    Set<UUID> uuidOnTrack =
        cartsOnTrack.stream().map(Entity::getUUID).collect(Collectors.toSet());

    AbstractMinecartEntity bestCart = getBestCartForVisualState(cartsOnTrack);

    boolean wasSwitched = this.isSwitched(blockState);
    BlockPos actuatorBlockPos = this.getActuatorBlockPos(blockState);
    BlockState actuatorBlockState = this.theWorldAsserted().getBlockState(actuatorBlockPos);
    boolean switched =
        !isLocked() && (actuatorBlockState.is(RailcraftTags.Blocks.SWITCH_TRACK_ACTUATOR)
            && SwitchTrackActuatorBlock.isSwitched(actuatorBlockState) || isSprung());

    // Only allow cartsOnTrack to actually spring or lock the track
    if (bestCart != null && uuidOnTrack.contains(bestCart.getUUID())) {
      if (shouldSwitchForCart(bestCart)) {
        springTrack(bestCart.getUUID());
      } else {
        lockTrack(bestCart.getUUID());
      }
    }

    if (switched != wasSwitched) {
      if (actuatorBlockState.is(RailcraftTags.Blocks.SWITCH_TRACK_ACTUATOR)) {
        this.theWorldAsserted().blockEvent(actuatorBlockPos, actuatorBlockState.getBlock(), 0,
            switched ? 1 : 0);
      }

      this.theWorldAsserted().setBlockAndUpdate(this.getPos(),
          blockState.setValue(SwitchTrackBlock.SWITCHED, switched));
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

  protected abstract List<UUID> getCartsAtLockEntrance(BlockState blockState);

  protected abstract List<UUID> getCartsAtSpringEntrance(BlockState blockState);

  protected abstract List<UUID> getCartsAtDecisionEntrance(BlockState blockState);

  public abstract Direction getActuatorDirection(BlockState blockState);

  public BlockPos getActuatorBlockPos(BlockState blockState) {
    return this.getPos().relative(this.getActuatorDirection(blockState));
  }

  public abstract ArrowDirection getRedArrowDirection(BlockState blockState);

  public abstract ArrowDirection getWhiteArrowDirection(BlockState blockState);

  public boolean isMirrored(BlockState blockState) {
    return blockState.getValue(SwitchTrackBlock.MIRRORED);
  }

  public boolean isSwitched(BlockState blockState) {
    return blockState.getValue(SwitchTrackBlock.SWITCHED);
  }
}
