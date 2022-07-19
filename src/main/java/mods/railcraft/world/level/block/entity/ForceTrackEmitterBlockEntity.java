package mods.railcraft.world.level.block.entity;

import mods.railcraft.api.charge.Charge;
import mods.railcraft.util.HostEffects;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.item.Magnifiable;
import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.track.ForceTrackBlockEntity;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

/**
 * .
 *
 * @author CovertJaguar (https://www.railcraft.info)
 */
public class ForceTrackEmitterBlockEntity extends RailcraftBlockEntity implements Magnifiable {

  private static final int BASE_DRAW = 22;
  private static final int CHARGE_PER_TRACK = 2;
  private int trackCount;
  private ForceTrackEmitterState.Instance stateInstance;
  /**
   * Field to prevent recursive removing of tracks when a track is broken by the emitter.
   */
  private boolean removingTrack;

  public ForceTrackEmitterBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.FORCE_TRACK_EMITTER.get(), blockPos, blockState);
    this.loadState(ForceTrackEmitterState.RETRACTED);
  }

  public ForceTrackEmitterState.Instance getStateInstance() {
    return this.stateInstance;
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

  private void loadState(ForceTrackEmitterState state) {
    this.stateInstance = state.load(this);
    this.syncToClient();
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      ForceTrackEmitterBlockEntity blockEntity) {
    if (!blockEntity.isPowered()) {
      blockEntity.stateInstance.uncharged().ifPresent(blockEntity::loadState);
    } else {
      var draw = getMaintenanceCost(blockEntity.getTrackCount());
      if (Charge.distribution
          .network((ServerLevel) level)
          .access(blockPos)
          .useCharge(draw, false)) {
        blockEntity.stateInstance.charged().ifPresent(blockEntity::loadState);
      } else {
        blockEntity.stateInstance.uncharged().ifPresent(blockEntity::loadState);
      }
    }
  }

  // always logical server
  private void spawnParticles(BlockPos pos) {
    HostEffects.INSTANCE.forceTrackSpawnEffect(this.level, pos,
        this.getBlockState().getValue(ForceTrackEmitterBlock.COLOR).getFireworkColor());
  }

  boolean placeTrack(BlockPos blockPos, BlockState existingBlockState, RailShape railShape) {
    if (existingBlockState.isAir()) {
      this.spawnParticles(blockPos);
      BlockState trackBlockState = RailcraftBlocks.FORCE_TRACK.get().defaultBlockState()
          .setValue(ForceTrackBlock.SHAPE, railShape);
      this.level.setBlockAndUpdate(blockPos, trackBlockState);
      BlockEntity blockEntity = this.level.getBlockEntity(blockPos);
      if (blockEntity instanceof ForceTrackBlockEntity) {
        ForceTrackBlockEntity track = (ForceTrackBlockEntity) blockEntity;
        track.setEmitter(this);
        this.trackCount++;
        return true;
      }
    }
    return false;
  }

  public static int getMaintenanceCost(int tracks) {
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
    this.loadState(ForceTrackEmitterState.HALTED);
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
  public void onMagnify(Player viewer) {
    viewer.displayClientMessage(
        Component.translatable("gui.railcraft.force.track.emitter.info",
            this.getTrackCount()),
        true);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putInt("trackCount", this.getTrackCount());
    tag.putString("state", this.stateInstance.getState().getSerializedName());
  }

  @Override
  public void load(CompoundTag tag) {
    this.trackCount = tag.getInt("trackCount");
    ForceTrackEmitterState.getByName(tag.getString("state")).ifPresent(this::loadState);
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeEnum(this.stateInstance.getState());
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    ForceTrackEmitterState state = data.readEnum(ForceTrackEmitterState.class);
    if (state != this.stateInstance.getState()) {
      this.loadState(state);
    }
  }
}
