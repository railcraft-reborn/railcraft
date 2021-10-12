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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author CovertJaguar <https://www.railcraft.info>
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

    if (this.level.isClientSide()) {
      return;
    }

    ForceTrackEmitterState lastState = this.state;
    if (!this.isPowered()) {
      this.state = lastState.uncharged(this);
    } else {
      double draw = getMaintenanceCost(getTrackCount());
      this.state = lastState.charged(this);

      if (Charge.distribution.network(this.level).access(this.worldPosition).useCharge(draw)) {
        this.state = lastState.charged(this);
      } else {
        this.state = lastState.uncharged(this);
      }
    }

    if (this.state != lastState) {
      this.state.load(this);
      this.syncToClient();
    }
  }

  // always logical server
  private void spawnParticles(BlockPos pos) {
    HostEffects.INSTANCE.forceTrackSpawnEffect(this.level, pos,
        this.getBlockState().getValue(ForceTrackEmitterBlock.COLOR).getColorValue());
  }

  @SuppressWarnings("deprecation")
  boolean placeTrack(BlockPos blockPos, BlockState existingBlockState, RailShape railShape) {
    if (existingBlockState.isAir(this.level, blockPos)) {
      this.spawnParticles(blockPos);
      BlockState trackBlockState = RailcraftBlocks.FORCE_TRACK.get().defaultBlockState()
          .setValue(ForceTrackBlock.SHAPE, railShape);
      this.level.setBlockAndUpdate(blockPos, trackBlockState);
      TileEntity blockEntity = this.level.getBlockEntity(blockPos);
      if (blockEntity instanceof ForceTrackBlockEntity) {
        ForceTrackBlockEntity track = (ForceTrackBlockEntity) blockEntity;
        track.setEmitter(this);
        this.trackCount++;
        return true;
      }
    }
    return false;
  }

  public static double getMaintenanceCost(int tracks) {
    return BASE_DRAW + CHARGE_PER_TRACK * tracks;
  }

  void removeFirstTrack() {
    BlockPos toRemove = this.worldPosition.above().relative(
        this.getBlockState().getValue(ForceTrackEmitterBlock.FACING),
        this.getTrackCount());
    this.removeTrack(toRemove);
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    if (!this.level.isClientSide()) {
      this.clearTracks();
    }
  }

  public void clearTracks() {
    this.clearTracks(this.getBlockPos().above().relative(
        ForceTrackEmitterBlock.getFacing(this.getBlockState())));
  }

  public void clearTracks(BlockPos startPos) {
    if (this.removingTrack) {
      return;
    }

    BlockPos endPos = this.getBlockPos().above().relative(
        ForceTrackEmitterBlock.getFacing(this.getBlockState()), this.trackCount);

    if (startPos.getX() == endPos.getX()) {
      BlockPos
          .betweenClosedStream(endPos.getX(), endPos.getY(), startPos.getZ(), endPos.getX(),
              endPos.getY(), endPos.getZ())
          .forEach(this::removeTrack);
    } else if (startPos.getZ() == endPos.getZ()) {
      BlockPos
          .betweenClosedStream(startPos.getX(), endPos.getY(), endPos.getZ(), endPos.getX(),
              endPos.getY(), endPos.getZ())
          .forEach(this::removeTrack);
    } else {
      throw new IllegalStateException("Block not aligned.");
    }

    this.notifyTrackChange();
  }

  private void removeTrack(BlockPos blockPos) {
    this.removingTrack = true;
    if (this.level.isLoaded(blockPos)
        && this.level.getBlockState(blockPos).is(RailcraftBlocks.FORCE_TRACK.get())) {
      this.spawnParticles(blockPos);
      LevelUtil.setAir(this.level, blockPos);
    }
    this.trackCount--;
    this.removingTrack = false;
  }


  public void notifyTrackChange() {
    this.state = ForceTrackEmitterState.HALTED;
  }

  private boolean isPowered() {
    return this.getBlockState().getValue(ForceTrackEmitterBlock.POWERED);
  }

  private void setPowered(boolean powered) {
    BlockState state = this.getBlockState();
    if (state.is(RailcraftBlocks.FORCE_TRACK_EMITTER.get())) {
      this.level.setBlockAndUpdate(this.worldPosition,
          state.setValue(ForceTrackEmitterBlock.POWERED, powered));
    }
  }

  public boolean setColor(DyeColor color) {
    if (this.getBlockState().getValue(ForceTrackEmitterBlock.COLOR) != color) {
      this.level.setBlockAndUpdate(this.worldPosition,
          this.getBlockState().setValue(ForceTrackEmitterBlock.COLOR, color));
      this.clearTracks();
      return true;
    }
    return false;
  }

  @Override
  public void onMagnify(PlayerEntity viewer) {
    viewer.displayClientMessage(
        new TranslationTextComponent("gui.railcraft.force.track.emitter.info",
            this.getTrackCount()),
        true);
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putInt("trackCount", this.getTrackCount());
    data.putString("state", this.state.getSerializedName());
    return data;
  }

  @Override
  public void load(BlockState blockState, CompoundNBT data) {
    super.load(blockState, data);
    this.trackCount = data.getInt("trackCount");
    this.state = ForceTrackEmitterState.getByName(data.getString("state"))
        .orElse(ForceTrackEmitterState.RETRACTED);
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeEnum(this.state);
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
