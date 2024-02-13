package mods.railcraft.world.level.block.entity;

import java.util.Optional;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.particle.ForceSpawnParticleOptions;
import mods.railcraft.util.ForwardingEnergyStorage;
import mods.railcraft.util.FunctionalUtil;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.item.Magnifiable;
import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.track.ForceTrackBlockEntity;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class ForceTrackEmitterBlockEntity extends RailcraftBlockEntity implements Magnifiable {

  private static final int BASE_DRAW = 22;
  private static final int CHARGE_PER_TRACK = 2;
  private int trackCount;
  private ForceTrackEmitterState.Instance stateInstance;
  /**
   * Field to prevent recursive removing of tracks when a track is broken by the emitter.
   */
  private boolean removingTrack;
  private final LazyOptional<IEnergyStorage> energyHandler;

  public ForceTrackEmitterBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.FORCE_TRACK_EMITTER.get(), blockPos, blockState);
    this.loadState(ForceTrackEmitterState.RETRACTED);
    this.energyHandler = LazyOptional.of(() -> new ForwardingEnergyStorage(this::storage));
  }

  public ForceTrackEmitterState.Instance getStateInstance() {
    return this.stateInstance;
  }

  public int getTrackCount() {
    return this.trackCount;
  }

  public void checkSignal() {
    if (this.level.isClientSide()) {
      return;
    }
    var powered = this.level.hasNeighborSignal(this.getBlockPos());
    if (this.isPowered() != powered) {
      this.setPowered(powered);
    }
  }

  private void loadState(ForceTrackEmitterState state) {
    this.stateInstance = state.load(this);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      ForceTrackEmitterBlockEntity blockEntity) {
    if (!blockEntity.isPowered()) {
      blockEntity.stateInstance.uncharged().ifPresent(blockEntity::loadState);
    } else {
      var draw = getRequiredEnergy(blockEntity.getTrackCount());
      var access = blockEntity.access();
      if (access.useCharge(draw, false)) {
        blockEntity.stateInstance.charged(access).ifPresent(blockEntity::loadState);
      } else {
        blockEntity.stateInstance.uncharged().ifPresent(blockEntity::loadState);
      }
    }
  }

  private void spawnParticles(BlockPos pos) {
    if (this.level.isClientSide()) {
      return;
    }

    int x = pos.getX();
    int y = pos.getY();
    int z = pos.getZ();
    var color = this.getBlockState().getValue(ForceTrackEmitterBlock.COLOR).getFireworkColor();

    var serverLevel = (ServerLevel) this.level;

    serverLevel.sendParticles(new ForceSpawnParticleOptions(color),
        x + 0.1, y, z + 0.1, 1, 0, 0, 0, 0);
    serverLevel.sendParticles(new ForceSpawnParticleOptions(color),
        x + 0.9, y, z + 0.1, 1, 0, 0, 0, 0);
    serverLevel.sendParticles(new ForceSpawnParticleOptions(color),
        x + 0.1, y, z + 0.9, 1, 0, 0, 0, 0);
    serverLevel.sendParticles(new ForceSpawnParticleOptions(color),
        x + 0.9, y, z + 0.9, 1, 0, 0, 0, 0);

    serverLevel.playSound(
        null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 0.25F, 1);
  }

  boolean placeTrack(BlockPos blockPos, BlockState existingBlockState, RailShape railShape) {
    if (!existingBlockState.isAir()) {
      return false;
    }

    this.spawnParticles(blockPos);
    var trackBlockState = RailcraftBlocks.FORCE_TRACK.get().defaultBlockState()
        .setValue(ForceTrackBlock.SHAPE, railShape);
    if (this.level.setBlockAndUpdate(blockPos, trackBlockState)
        && this.level.getBlockEntity(blockPos) instanceof ForceTrackBlockEntity track) {
      track.setEmitter(this);
      this.trackCount++;
      return true;
    }

    return false;
  }

  public static int getRequiredEnergy(int tracks) {
    return BASE_DRAW + CHARGE_PER_TRACK * tracks;
  }

  void removeFirstTrack() {
    var toRemove = this.worldPosition.above()
        .relative(ForceTrackEmitterBlock.getFacing(this.getBlockState()), this.trackCount);
    this.removeTrack(toRemove);
  }

  public void clearTracks() {
    this.clearTracks(this.getBlockPos().above()
        .relative(ForceTrackEmitterBlock.getFacing(this.getBlockState())));
  }

  public void clearTracks(BlockPos startPos) {
    if (this.removingTrack || this.trackCount <= 0) {
      return;
    }

    var endPos = this.getBlockPos().above()
        .relative(ForceTrackEmitterBlock.getFacing(this.getBlockState()), this.trackCount);

    if (startPos.getX() == endPos.getX()) {
      FunctionalUtil.rangeClosed(startPos.getZ(), endPos.getZ())
          .mapToObj(z -> new BlockPos(endPos.getX(), endPos.getY(), z))
          .forEach(this::removeTrack);
    } else if (startPos.getZ() == endPos.getZ()) {
      FunctionalUtil.rangeClosed(startPos.getX(), endPos.getX())
          .mapToObj(x -> new BlockPos(x, endPos.getY(), endPos.getZ()))
          .forEach(this::removeTrack);
    } else {
      throw new IllegalStateException("Block not aligned.");
    }

    this.notifyTrackChange();
  }

  private void removeTrack(BlockPos blockPos) {
    if (this.trackCount <= 0) {
      throw new IllegalStateException("trackCount must be greater than 0");
    }
    this.removingTrack = true;
    if (this.level.isLoaded(blockPos) &&
        this.level.getBlockState(blockPos).is(RailcraftBlocks.FORCE_TRACK.get())) {
      LevelUtil.setAir(this.level, blockPos);
      this.spawnParticles(blockPos);
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
    var state = this.getBlockState();
    if (state.is(RailcraftBlocks.FORCE_TRACK_EMITTER.get())) {
      this.level.setBlockAndUpdate(this.worldPosition,
          state.setValue(ForceTrackEmitterBlock.POWERED, powered));
    }
  }

  public boolean setColor(DyeColor color) {
    if (this.getBlockState().getValue(ForceTrackEmitterBlock.COLOR) != color) {
      this.level.setBlockAndUpdate(this.worldPosition,
          this.getBlockState().setValue(ForceTrackEmitterBlock.COLOR, color));
      if (!this.level.isClientSide()) {
        this.clearTracks();
      }
      return true;
    }
    return false;
  }

  @Override
  public void onMagnify(Player player) {
    player.displayClientMessage(
        Component.translatable("gui.railcraft.force.track.emitter.info",
            this.getTrackCount()),
        true);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putInt(CompoundTagKeys.TRACK_COUNT, this.getTrackCount());
    tag.putString(CompoundTagKeys.STATE, this.stateInstance.state().getSerializedName());
  }

  @Override
  public void load(CompoundTag tag) {
    this.trackCount = tag.getInt(CompoundTagKeys.TRACK_COUNT);
    ForceTrackEmitterState.fromName(tag.getString(CompoundTagKeys.STATE)).ifPresent(this::loadState);
  }

  private Optional<? extends ChargeStorage> storage() {
    return this.level().isClientSide() ? Optional.empty() : this.access().storage();
  }

  private Charge.Access access() {
    return Charge.distribution
        .network((ServerLevel) this.level)
        .access(this.blockPos());
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    return cap == ForgeCapabilities.ENERGY
        ? this.energyHandler.cast()
        : super.getCapability(cap, side);
  }
}
