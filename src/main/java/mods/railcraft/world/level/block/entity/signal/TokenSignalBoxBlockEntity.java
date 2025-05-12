package mods.railcraft.world.level.block.entity.signal;

import java.util.Objects;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalController;
import mods.railcraft.api.signal.SimpleSignalController;
import mods.railcraft.api.signal.TokenSignalEntity;
import mods.railcraft.api.signal.TrackLocator;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.TimerBag;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.signal.SimpleTokenRing;
import mods.railcraft.world.signal.TokenRingManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;
import net.minecraft.world.phys.Vec3;

public class TokenSignalBoxBlockEntity extends ActionSignalBoxBlockEntity
    implements TokenSignalEntity, SignalControllerEntity {

  private final SimpleSignalController signalController =
      new SimpleSignalController(1, this::syncToClient, this, false);
  private UUID ringId = UUID.randomUUID();
  @Nullable
  private Vec3 ringCentroidPos;

  private final TimerBag<UUID> cartTimers = new TimerBag<>(8);
  private final TrackLocator trackLocator;

  public TokenSignalBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.TOKEN_SIGNAL_BOX.get(), blockPos, blockState);
    this.trackLocator = new TrackLocator(this::getLevel, blockPos);
  }

  @Override
  public void blockRemoved() {
    super.blockRemoved();
    this.signalController.destroy();
  }

  @Override
  public void onLoad() {
    super.onLoad();
    this.signalController.refresh();
  }

  private void signalAspectChanged(SignalAspect signalAspect) {
    this.signalController.setSignalAspect(signalAspect);
    this.updateNeighborSignalBoxes(false);
    this.level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
  }

  @Override
  public int getRedstoneSignal(Direction side) {
    return this.isActionSignalAspect(this.signalNetwork().aspect())
        ? Redstone.SIGNAL_MAX
        : Redstone.SIGNAL_NONE;
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.putUUID(CompoundTagKeys.TOKEN_RING_ID, this.ringId);
    tag.put(CompoundTagKeys.SIGNAL_CONTROLLER, this.signalController.serializeNBT(provider));
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    this.ringId = tag.getUUID(CompoundTagKeys.TOKEN_RING_ID);
    this.signalController
        .deserializeNBT(provider, tag.getCompound(CompoundTagKeys.SIGNAL_CONTROLLER));
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf data) {
    super.writeToBuf(data);
    this.signalController.writeToBuf(data);
    data.writeNullable(this.ringCentroidPos, FriendlyByteBuf::writeVec3);
    data.writeUUID(this.ringId);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf data) {
    super.readFromBuf(data);
    this.signalController.readFromBuf(data);
    this.ringCentroidPos = data.readNullable(FriendlyByteBuf::readVec3);
    this.ringId = data.readUUID();
  }

  @Override
  public SignalAspect getSignalAspect(Direction direction) {
    return this.signalController.aspect();
  }

  @Override
  public SignalController getSignalController() {
    return this.signalController;
  }

  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState,
      TokenSignalBoxBlockEntity blockEntity) {
    blockEntity.signalController.spawnTuningAuraParticles();
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      TokenSignalBoxBlockEntity blockEntity) {

    var tokenRing = blockEntity.signalNetwork();
    if (!Objects.equals(blockEntity.ringCentroidPos, tokenRing.getCentroid())) {
      blockEntity.ringCentroidPos = tokenRing.getCentroid();
      blockEntity.syncToClient();
    }

    blockEntity.cartTimers.tick();
    if (blockEntity.trackLocator.trackStatus() == TrackLocator.Status.VALID) {
      var trackPos = blockEntity.trackLocator.trackPos();
      if (trackPos != null) {
        EntitySearcher.findMinecarts()
            .at(trackPos)
            .stream(level)
            .filter(cart -> blockEntity.cartTimers.add(cart.getUUID()))
            .forEach(tokenRing::markCart);
      }
    }

    if (blockEntity.signalController.aspect() != tokenRing.aspect()) {
      blockEntity.signalController.setSignalAspect(tokenRing.aspect());
      blockEntity.updateNeighborSignalBoxes(false);
    }
  }

  @Override
  public UUID ringId() {
    return this.ringId;
  }

  public void setRingId(UUID tokenRingId) {
    this.ringId = tokenRingId;
    this.setChanged();
  }

  @Override
  public Vec3 ringCentroidPos() {
    if (this.ringCentroidPos == null)
      return this.getBlockPos().getCenter();
    return this.ringCentroidPos;
  }

  @Override
  public SimpleTokenRing signalNetwork() {
    if (this.level.isClientSide()) {
      throw new IllegalStateException("Token ring is not available on the client.");
    }
    return TokenRingManager.get((ServerLevel) this.level)
        .getTokenRingNetwork(this.ringId, this.getBlockPos());
  }

  @Override
  public TrackLocator trackLocator() {
    return this.trackLocator;
  }
}
