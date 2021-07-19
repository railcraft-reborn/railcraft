/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.tracks;

import javax.annotation.Nullable;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.RailcraftFakePlayer;
import mods.railcraft.api.items.IToolCrowbar;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * All ITrackKits should extend this class. It contains a number of default functions and standard
 * behavior for Tracks that should greatly simplify implementing new Track Kits when using this API.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 * @see ITrackKitInstance
 * @see TrackRegistry
 * @see TrackKit
 */
public abstract class TrackKitInstance implements ITrackKitInstance {

  private TileEntity tileEntity = new DummyTileEntity();

  protected static RailShape getRailDirectionRaw(BlockState state, IBlockReader world, BlockPos pos,
      @Nullable AbstractMinecartEntity cart) {
    return state.getBlock() instanceof AbstractRailBlock
        ? ((AbstractRailBlock) state.getBlock()).getRailDirection(state, world, pos, cart)
        : RailShape.NORTH_SOUTH;
  }

  private AbstractRailBlock getBlock() {
    return (AbstractRailBlock) getTile().getBlockState().getBlock();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends TileEntity & IOutfittedTrackTile> T getTile() {
    return (T) tileEntity;
  }

  @Override
  public <T extends TileEntity & IOutfittedTrackTile> void setTile(T tileEntity) {
    this.tileEntity = tileEntity;
  }

  @Override
  public RailShape getRailDirection(BlockState state, @Nullable AbstractMinecartEntity cart) {
    return getRailDirectionRaw(state, theWorldAsserted(), getPos(), cart);
  }

  public final RailShape getRailDirectionRaw() {
    World world = theWorldAsserted();
    BlockState state = world.getBlockState(getPos());
    return getRailDirectionRaw(state, world, getPos(), null);
  }

  @Override
  public ActionResultType use(PlayerEntity player, Hand hand) {
    ItemStack heldItem = player.getItemInHand(hand);
    if (heldItem.getItem() instanceof IToolCrowbar) {
      IToolCrowbar crowbar = (IToolCrowbar) heldItem.getItem();
      if (crowbar.canWhack(player, hand, heldItem, getPos())
          && onCrowbarWhack(player, hand, heldItem)) {
        crowbar.onWhack(player, hand, heldItem, getPos());
        return ActionResultType.SUCCESS;
      }
    }
    return ActionResultType.CONSUME;
  }

  public boolean onCrowbarWhack(PlayerEntity player, Hand hand, @Nullable ItemStack heldItem) {
    if (this instanceof ITrackKitReversible) {
      ITrackKitReversible track = (ITrackKitReversible) this;
      track.setReversed(!track.isReversed());
      markBlockNeedsUpdate();
      return true;
    }
    return false;
  }

  @Override
  public void setPlacedBy(BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
    if (placer != null && this instanceof ITrackKitReversible) {
      Direction facing = placer.getDirection();
      ((ITrackKitReversible) this)
          .setReversed(facing == Direction.SOUTH || facing == Direction.WEST);
    }
    switchTrack(state, true);
    testPower(state);
    markBlockNeedsUpdate();
  }

  @Override
  public void sendUpdateToClient() {
    getTile().sendUpdateToClient();
  }

  public void markBlockNeedsUpdate() {
    World world = theWorld();
    if (world != null) {
      BlockState state = world.getBlockState(getTile().getBlockPos());
      world.sendBlockUpdated(getTile().getBlockPos(), state, state, 3);
    }
  }

  @Override
  public void neighborChanged(BlockState state, @Nullable Block neighborBlock) {
    testPower(state);
  }

  @SuppressWarnings("deprecation")
  private void switchTrack(BlockState state, boolean flag) {
    World world = theWorldAsserted();
    BlockPos pos = getTile().getBlockPos();
    AbstractRailBlock blockTrack = getBlock();
    new RailState(world, pos, state).place(world.hasNeighborSignal(pos), flag,
        state.getValue(blockTrack.getShapeProperty()));
  }

  protected final void testPower(BlockState state) {
    if (!(this instanceof ITrackKitPowered))
      return;
    World world = theWorldAsserted();
    ITrackKitPowered r = (ITrackKitPowered) this;
    boolean powered = world.getBestNeighborSignal(getPos()) > 0
        || testPowerPropagation(world, getPos(), getTrackKit(), state, r.getPowerPropagation());
    if (powered != r.isPowered()) {
      r.setPowered(powered);
      world.updateNeighbourForOutputSignal(getPos(), getBlock());
      sendUpdateToClient();
      // System.out.println("Setting power [" + i + ", " + j + ", " + k + "]");
    }
  }

  private boolean testPowerPropagation(World world, BlockPos pos, TrackKit baseSpec,
      BlockState state, int maxDist) {
    return isConnectedRailPowered(world, pos, baseSpec, state, true, 0, maxDist)
        || isConnectedRailPowered(world, pos, baseSpec, state, false, 0, maxDist);
  }

  private boolean isConnectedRailPowered(World world, BlockPos pos, TrackKit baseSpec,
      BlockState state, boolean dir, int dist, int maxDist) {
    if (dist >= maxDist)
      return false;
    boolean powered = true;
    int x = pos.getX();
    int y = pos.getY();
    int z = pos.getZ();
    RailShape railDirection = getBlock().getRailDirection(state, world, pos, null);
    switch (railDirection) {
      case NORTH_SOUTH: // '\0'
        if (dir)
          z++;
        else
          z--;
        break;

      case EAST_WEST: // '\001'
        if (dir)
          x--;
        else
          x++;
        break;

      case ASCENDING_EAST: // '\002'
        if (dir)
          x--;
        else {
          x++;
          y++;
          powered = false;
        }
        railDirection = RailShape.EAST_WEST;
        break;

      case ASCENDING_WEST: // '\003'
        if (dir) {
          x--;
          y++;
          powered = false;
        } else
          x++;
        railDirection = RailShape.EAST_WEST;
        break;

      case ASCENDING_NORTH: // '\004'
        if (dir)
          z++;
        else {
          z--;
          y++;
          powered = false;
        }
        railDirection = RailShape.NORTH_SOUTH;
        break;

      case ASCENDING_SOUTH: // '\005'
        if (dir) {
          z++;
          y++;
          powered = false;
        } else
          z--;
        railDirection = RailShape.NORTH_SOUTH;
        break;
      default:
        break;
    }
    pos = new BlockPos(x, y, z);
    return testPowered(world, pos, baseSpec, dir, dist, maxDist, railDirection)
        || (powered
            && testPowered(world, pos.below(), baseSpec, dir, dist, maxDist, railDirection));
  }

  private boolean testPowered(World world, BlockPos nextPos, TrackKit baseSpec, boolean dir,
      int dist, int maxDist, RailShape prevOrientation) {
    // System.out.println("Testing Power at <" + nextPos + ">");
    BlockState nextBlockState = world.getBlockState(nextPos);
    if (nextBlockState.getBlock() == getBlock()) {
      RailShape nextOrientation = ((AbstractRailBlock) nextBlockState.getBlock())
          .getRailDirection(nextBlockState, world, nextPos, null);
      TileEntity nextTile = world.getBlockEntity(nextPos);
      if (nextTile instanceof IOutfittedTrackTile) {
        ITrackKitInstance nextTrack = ((IOutfittedTrackTile) nextTile).getTrackKitInstance();
        if (!(nextTrack instanceof ITrackKitPowered) || nextTrack.getTrackKit() != baseSpec
            || !((ITrackKitPowered) this).canPropagatePowerTo(nextTrack))
          return false;
        if (prevOrientation == RailShape.EAST_WEST && (nextOrientation == RailShape.NORTH_SOUTH
            || nextOrientation == RailShape.ASCENDING_NORTH
            || nextOrientation == RailShape.ASCENDING_SOUTH))
          return false;
        if (prevOrientation == RailShape.NORTH_SOUTH && (nextOrientation == RailShape.EAST_WEST
            || nextOrientation == RailShape.ASCENDING_EAST
            || nextOrientation == RailShape.ASCENDING_WEST))
          return false;
        if (((ITrackKitPowered) nextTrack).isPowered())
          return world.hasNeighborSignal(nextPos) || world.hasNeighborSignal(nextPos.above())
              || isConnectedRailPowered(world, nextPos, baseSpec, nextBlockState, dir, dist + 1,
                  maxDist);
      }
    }
    return false;
  }

  private class DummyTileEntity extends TileEntity implements IOutfittedTrackTile {

    public DummyTileEntity() {
      super(null);
    }

    @Override
    public TrackType getTrackType() {
      return TrackRegistry.TRACK_TYPE.getFallback();
    }

    @Override
    public ITrackKitInstance getTrackKitInstance() {
      return new TrackKitMissing(false);
    }

    @Override
    public void sendUpdateToClient() {
      throw new RuntimeException();
    }

    @Override
    public GameProfile getOwner() {
      return RailcraftFakePlayer.RAILCRAFT_USER_PROFILE;
    }
  }
}
