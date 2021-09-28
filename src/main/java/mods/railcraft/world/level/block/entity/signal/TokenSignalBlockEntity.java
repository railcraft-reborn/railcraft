package mods.railcraft.world.level.block.entity.signal;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SignalController;
import mods.railcraft.api.signals.SimpleSignalControllerNetwork;
import mods.railcraft.api.signals.TokenSignal;
import mods.railcraft.api.signals.TrackLocator;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.collections.TimerBag;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.signal.SimpleTokenRingNetwork;
import mods.railcraft.world.signal.TokenManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * Created by CovertJaguar on 4/13/2015 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class TokenSignalBlockEntity extends AbstractSignalBlockEntity
    implements SignalController, TokenSignal {

  private final SimpleSignalControllerNetwork signalControllerNetwork =
      new SimpleSignalControllerNetwork(this, this::syncToClient);

  private UUID tokenRingId = UUID.randomUUID();
  private BlockPos tokenRingCentroid;

  private final TimerBag<UUID> cartTimers = new TimerBag<>(8);
  @Nullable
  private TrackLocator trackLocator;

  public TokenSignalBlockEntity() {
    this(RailcraftBlockEntityTypes.TOKEN_SIGNAL.get());
  }

  public TokenSignalBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public void setLevelAndPosition(World level, BlockPos blockPos) {
    super.setLevelAndPosition(level, blockPos);
    this.trackLocator = new TrackLocator(level, blockPos);
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      this.signalControllerNetwork.tickClient();
      return;
    }

    SimpleTokenRingNetwork tokenRing = this.getSignalNetwork();
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

    this.signalControllerNetwork.tickServer();
    SignalAspect prevAspect = this.signalControllerNetwork.getAspect();
    if (this.signalControllerNetwork.isLinking()) {
      this.signalControllerNetwork.setAspect(SignalAspect.BLINK_YELLOW);
    } else {
      this.signalControllerNetwork.setAspect(tokenRing.getAspect());
    }
    if (prevAspect != this.signalControllerNetwork.getAspect()) {
      this.syncToClient();
    }
  }

  @Override
  public SignalAspect getSignalAspect() {
    return this.signalControllerNetwork.getAspect();
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    data = super.save(data);
    data.put("network", this.signalControllerNetwork.serializeNBT());
    data.putUUID("tokenRingId", this.tokenRingId);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.signalControllerNetwork.deserializeNBT(data.getCompound("network"));
    this.tokenRingId = data.getUUID("tokenRingId");
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    this.signalControllerNetwork.writeSyncData(data);
    data.writeBlockPos(this.getSignalNetwork().getCentroid());
    data.writeUUID(this.tokenRingId);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.signalControllerNetwork.readSyncData(data);
    this.tokenRingCentroid = data.readBlockPos();
    this.tokenRingId = data.readUUID();
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    if (!this.level.isClientSide()) {
      this.getSignalNetwork().removePeer(this.getBlockPos());
    }
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
  public SimpleSignalControllerNetwork getSignalControllerNetwork() {
    return this.signalControllerNetwork;
  }

  @Override
  public SimpleTokenRingNetwork getSignalNetwork() {
    if (this.level.isClientSide()) {
      throw new IllegalStateException("Token ring network is not available on the client.");
    }
    return TokenManager.get((ServerWorld) this.level)
        .getTokenRingNetwork(this.tokenRingId, this.getBlockPos());
  }

  @Override
  public TrackLocator getTrackLocator() {
    return this.trackLocator;
  }

  @Override
  public ITextComponent getPrimaryNetworkName() {
    return this.signalControllerNetwork.getName();
  }
}
