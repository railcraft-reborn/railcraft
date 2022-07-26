package mods.railcraft.world.level.block.entity.signal;

import java.util.Objects;
import java.util.UUID;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalControllerProvider;
import mods.railcraft.api.signal.SimpleSignalController;
import mods.railcraft.api.signal.TokenSignal;
import mods.railcraft.api.signal.TrackLocator;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.TimerBag;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.signal.SimpleTokenRing;
import mods.railcraft.world.signal.TokenRingManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Created by CovertJaguar on 4/13/2015 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class TokenSignalBlockEntity extends AbstractSignalBlockEntity
    implements SignalControllerProvider, TokenSignal {

  private final SimpleSignalController signalController =
      new SimpleSignalController(1, this::syncToClient, this, false,
          __ -> this.level.getLightEngine().checkBlock(this.getBlockPos()));

  private UUID tokenRingId = UUID.randomUUID();
  private BlockPos tokenRingCentroid;

  private final TimerBag<UUID> cartTimers = new TimerBag<>(8);
  private final TrackLocator trackLocator = new TrackLocator(this);

  public TokenSignalBlockEntity(BlockPos blockPos, BlockState blockState) {
    this(RailcraftBlockEntityTypes.TOKEN_SIGNAL.get(), blockPos, blockState);
  }

  public TokenSignalBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
    super(type, blockPos, blockState);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (!this.level.isClientSide()) {
      this.signalController.refresh();
    }
  }

  public void blockRemoved() {
    this.signalController.destroy();
    if (!this.level.isClientSide()) {
      this.getSignalNetwork().removePeer(this.getBlockPos());
    }
  }

  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState,
      TokenSignalBlockEntity blockEntity) {
    blockEntity.signalController.spawnTuningAuraParticles();
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      TokenSignalBlockEntity blockEntity) {

    var tokenRing = blockEntity.getSignalNetwork();
    if (!Objects.equals(blockEntity.tokenRingCentroid, tokenRing.getCentroid())) {
      blockEntity.tokenRingCentroid = tokenRing.getCentroid();
      blockEntity.syncToClient();
    }

    blockEntity.cartTimers.tick();
    if (blockEntity.trackLocator.getTrackStatus() == TrackLocator.Status.VALID) {
      var trackPos = blockEntity.trackLocator.getTrackPos();
      if (trackPos != null) {
        var carts = EntitySearcher.findMinecarts()
            .around(trackPos)
            .search(level);
        carts.stream().filter(c -> !blockEntity.cartTimers.contains(c.getUUID()))
            .forEach(tokenRing::markCart);
        carts.forEach(c -> blockEntity.cartTimers.add(c.getUUID()));
      }
    }

    blockEntity.signalController.setSignalAspect(blockEntity.getSignalNetwork().getSignalAspect());
  }

  @Override
  public SignalAspect getPrimarySignalAspect() {
    return this.signalController.getSignalAspect();
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("network", this.signalController.serializeNBT());
    tag.putUUID("tokenRingId", this.tokenRingId);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.signalController.deserializeNBT(tag.getCompound("network"));
    this.tokenRingId = tag.getUUID("tokenRingId");
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    this.signalController.writeToBuf(data);
    data.writeBlockPos(this.getSignalNetwork().getCentroid());
    data.writeUUID(this.tokenRingId);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.signalController.readFromBuf(data);
    this.tokenRingCentroid = data.readBlockPos();
    this.tokenRingId = data.readUUID();
  }

  @Override
  public UUID getTokenRingId() {
    return this.tokenRingId;
  }

  public void setTokenRingId(UUID tokenRingId) {
    this.tokenRingId = tokenRingId;
  }

  @Override
  public BlockPos getTokenRingCentroid() {
    if (this.tokenRingCentroid == null)
      return this.getBlockPos();
    return this.tokenRingCentroid;
  }

  @Override
  public SimpleSignalController getSignalController() {
    return this.signalController;
  }

  @Override
  public SimpleTokenRing getSignalNetwork() {
    if (this.level.isClientSide()) {
      throw new IllegalStateException("Token ring is not available on the client.");
    }
    return TokenRingManager.get((ServerLevel) this.level)
        .getTokenRingNetwork(this.tokenRingId, this.getBlockPos());
  }

  @Override
  public TrackLocator getTrackLocator() {
    return this.trackLocator;
  }
}
