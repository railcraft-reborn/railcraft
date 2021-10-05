package mods.railcraft.world.level.block.entity.signal;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalControllerProvider;
import mods.railcraft.api.signal.SimpleSignalController;
import mods.railcraft.api.signal.TokenSignal;
import mods.railcraft.api.signal.TrackLocator;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.collections.TimerBag;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.signal.SimpleTokenRing;
import mods.railcraft.world.signal.TokenRingManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * Created by CovertJaguar on 4/13/2015 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class TokenSignalBlockEntity extends AbstractSignalBlockEntity
    implements SignalControllerProvider, TokenSignal, ITickableTileEntity {

  private final SimpleSignalController signalController =
      new SimpleSignalController(1, this::syncToClient, this, false,
          __ -> this.level.getLightEngine().checkBlock(this.getBlockPos()));

  private UUID tokenRingId = UUID.randomUUID();
  private BlockPos tokenRingCentroid;

  private final TimerBag<UUID> cartTimers = new TimerBag<>(8);
  private final TrackLocator trackLocator = new TrackLocator(this);

  public TokenSignalBlockEntity() {
    this(RailcraftBlockEntityTypes.TOKEN_SIGNAL.get());
  }

  public TokenSignalBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public void load() {
    if (!this.level.isClientSide()) {
      this.signalController.refresh();
    }
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    this.signalController.removed();
    if (!this.level.isClientSide()) {
      this.getSignalNetwork().removePeer(this.getBlockPos());
    }
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      this.signalController.spawnTuningAuraParticles();
      return;
    }

    SimpleTokenRing tokenRing = this.getSignalNetwork();
    if (!Objects.equals(this.tokenRingCentroid, tokenRing.getCentroid())) {
      this.tokenRingCentroid = tokenRing.getCentroid();
      this.syncToClient();
    }

    this.cartTimers.tick();
    if (this.trackLocator.getTrackStatus() == TrackLocator.Status.VALID) {
      BlockPos trackPos = this.trackLocator.getTrackPos();
      if (trackPos != null) {
        List<AbstractMinecartEntity> carts = EntitySearcher.findMinecarts()
            .around(trackPos)
            .in(this.level);
        carts.stream().filter(c -> !this.cartTimers.contains(c.getUUID()))
            .forEach(tokenRing::markCart);
        carts.forEach(c -> this.cartTimers.add(c.getUUID()));
      }
    }

    this.signalController.setSignalAspect(this.getSignalNetwork().getSignalAspect());
  }

  @Override
  public SignalAspect getPrimarySignalAspect() {
    return this.signalController.getSignalAspect();
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    data = super.save(data);
    data.put("network", this.signalController.serializeNBT());
    data.putUUID("tokenRingId", this.tokenRingId);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.signalController.deserializeNBT(data.getCompound("network"));
    this.tokenRingId = data.getUUID("tokenRingId");
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    this.signalController.writeSyncData(data);
    data.writeBlockPos(this.getSignalNetwork().getCentroid());
    data.writeUUID(this.tokenRingId);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.signalController.readSyncData(data);
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
    return TokenRingManager.get((ServerWorld) this.level)
        .getTokenRingNetwork(this.tokenRingId, this.getBlockPos());
  }

  @Override
  public TrackLocator getTrackLocator() {
    return this.trackLocator;
  }
}
