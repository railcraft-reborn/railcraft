package mods.railcraft.world.level.block.entity;

import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.core.Ownable;
import mods.railcraft.util.HostEffects;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.item.Magnifiable;
import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.track.ForceTrackBlockEntity;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class ForceTrackEmitterBlockEntity extends RailcraftTickableBlockEntity
    implements Ownable, Magnifiable {

  private static final double BASE_DRAW = 22;
  private static final double CHARGE_PER_TRACK = 2;
  private int trackCount;
  private ForceTrackEmitterState state = ForceTrackEmitterState.RETRACTED;
  /**
   * Field to prevent recursive removing of tracks when a track is broken by the emitter
   */
  private boolean removingTrack;

  public ForceTrackEmitterBlockEntity() {
    super(RailcraftBlockEntityTypes.FORCE_TRACK_EMITTER.get());
  }

  public ForceTrackEmitterState getState() {
    return this.state;
  }

  public int getTrackCount() {
    return this.trackCount;
  }

  public void setTrackCount(int trackCount) {
    this.trackCount = trackCount;
  }

  public void checkSignal() {
    if (this.level.isClientSide())
      return;
    boolean powered = this.level.hasNeighborSignal(this.getBlockPos());
    if (this.isPowered() != powered) {
      this.setPowered(powered);
    }
  }

  @Override
  public void tick() {
    super.tick();

    if (this.level.isClientSide())
      return;

    ForceTrackEmitterState lastState = this.state;
    if (!this.isPowered()) {
      this.state = lastState.whenNoCharge(this);
    } else {
      double draw = getMaintenanceCost(getTrackCount());
      if (Charge.distribution.network(this.level).access(this.worldPosition).useCharge(draw)) {
        this.state = lastState.afterUseCharge(this);
      } else {
        this.state = lastState.whenNoCharge(this);
      }
    }

    if (this.state != lastState) {
      this.state.onTransition(this);
      this.syncToClient();
    }
  }

  // always logical server
  private void spawnParticles(BlockPos pos) {
    HostEffects.INSTANCE.forceTrackSpawnEffect(this.level, pos,
        this.getBlockState().getValue(ForceTrackEmitterBlock.COLOR).getColorValue());
  }

  @SuppressWarnings("deprecation")
  boolean placeTrack(BlockPos toPlace, BlockState prevState, RailShape direction) {
    ForceTrackBlock trackForce = RailcraftBlocks.FORCE_TRACK.get();
    if (trackForce != null && prevState.isAir(this.level, toPlace)) {
      this.spawnParticles(toPlace);
      BlockState place =
          trackForce.defaultBlockState().setValue(ForceTrackBlock.SHAPE, direction);
      this.level.setBlockAndUpdate(toPlace, place);
      TileEntity tile = this.level.getBlockEntity(toPlace);
      if (tile instanceof ForceTrackBlockEntity) {
        ForceTrackBlockEntity track = (ForceTrackBlockEntity) tile;
        track.setEmitter(this);
        this.setTrackCount(this.getTrackCount() + 1);
        return true;
      }
    }
    return false;
  }

  public static double getMaintenanceCost(int tracks) {
    return BASE_DRAW + CHARGE_PER_TRACK * tracks;
  }

  public boolean isOutOfPower() {
    return !Charge.distribution.network(this.level)
        .access(this.getBlockPos())
        .hasCapacity(getMaintenanceCost(this.getTrackCount() + 1));
  }

  void removeFirstTrack() {
    BlockPos toRemove = this.worldPosition.above().relative(
        this.getBlockState().getValue(ForceTrackEmitterBlock.FACING),
        this.getTrackCount());
    this.removeTrack(toRemove);
  }

  private void removeTrack(BlockPos toRemove) {
    this.removingTrack = true;
    if (this.level.isLoaded(toRemove)
        && this.level.getBlockState(toRemove).is(RailcraftBlocks.FORCE_TRACK.get())) {
      this.spawnParticles(toRemove);
      LevelUtil.setAir(this.level, toRemove);
    }
    this.setTrackCount(this.getTrackCount() - 1);
    this.removingTrack = false;
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    this.clearTracks();
  }

  public void clearTracks() {
    this.clearTracks(0);
  }

  public void clearTracks(int lastIndex) {
    if (this.removingTrack || lastIndex == getTrackCount()) {
      return;
    }
    Direction facing = this.getBlockState().getValue(ForceTrackEmitterBlock.FACING);
    BlockPos.Mutable toRemove = this.getBlockPos().mutable();
    toRemove.move(Direction.UP);
    toRemove.move(facing, getTrackCount());
    while (getTrackCount() > lastIndex) {
      this.removeTrack(toRemove);
      toRemove.move(facing.getOpposite());
    }
    this.notifyTrackChange();
  }

  public void notifyTrackChange() {
    this.state = ForceTrackEmitterState.HALTED;
  }

  private boolean isPowered() {
    return this.getBlockState().is(RailcraftBlocks.FORCE_TRACK_EMITTER.get())
        && this.getBlockState().getValue(ForceTrackEmitterBlock.POWERED);
  }

  private void setPowered(boolean powered) {
    BlockState state = this.getBlockState();
    if (state.is(RailcraftBlocks.FORCE_TRACK_EMITTER.get())) {
      this.level.setBlockAndUpdate(this.worldPosition,
          state.setValue(ForceTrackEmitterBlock.POWERED, powered));
    }
  }

  public boolean setColor(DyeColor color) {
    BlockState state = this.getBlockState();
    if (state.is(RailcraftBlocks.FORCE_TRACK_EMITTER.get())) {
      if (this.getBlockState().getValue(ForceTrackEmitterBlock.COLOR) != color) {
        this.level.setBlockAndUpdate(this.worldPosition,
            state.setValue(ForceTrackEmitterBlock.COLOR, color));
        this.clearTracks();
        return true;
      }
    }
    return false;
  }

  @Override
  public void onMagnify(PlayerEntity viewer) {
    viewer.sendMessage(new TranslationTextComponent("gui.railcraft.force.track.emitter.info",
        this.getTrackCount()), Util.NIL_UUID);
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putInt("numTracks", this.getTrackCount());
    data.putString("state", this.state.name());
    return data;
  }

  @Override
  public void load(BlockState blockState, CompoundNBT data) {
    super.load(blockState, data);
    this.setTrackCount(data.getInt("numTracks"));
    this.state = ForceTrackEmitterState.valueOf(data.getString("state"));
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeEnum(state);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    ForceTrackEmitterState state = data.readEnum(ForceTrackEmitterState.class);
    if (state != this.state) {
      this.state = state;
    }
  }
}
