package mods.railcraft.world.level.block.entity.signal;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import mods.railcraft.api.signals.SignalControllerProvider;
import mods.railcraft.api.signals.TokenSignal;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SimpleSignalController;
import mods.railcraft.api.signals.TrackLocator;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.collections.TimerBag;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.signal.TokenManager;
import mods.railcraft.world.signal.TokenRingImpl;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * Created by CovertJaguar on 4/13/2015 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class TokenSignalBlockEntity extends AbstractSignalBlockEntity
    implements SignalControllerProvider, TokenSignal {

  private final SimpleSignalController controller =
      new SimpleSignalController("nothing", this);
  private UUID tokenRingId = UUID.randomUUID();
  private BlockPos centroid;
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
      this.controller.tickClient();
      return;
    }

    TokenRingImpl tokenRing = getTokenRing();
    if (!Objects.equals(this.centroid, tokenRing.getCentroid())) {
      this.centroid = tokenRing.getCentroid();
      this.syncToClient();
    }

    this.cartTimers.tick();
    if (this.trackLocator.getTrackStatus() == TrackLocator.Status.VALID) {
      BlockPos trackPos = this.trackLocator.getTrackLocation();
      if (trackPos != null) {
        List<AbstractMinecartEntity> carts = EntitySearcher.findMinecarts()
            .around(trackPos)
            .in(this.level);
        carts.stream().filter(c -> !this.cartTimers.contains(c.getUUID()))
            .forEach(tokenRing::markCart);
        carts.forEach(c -> this.cartTimers.add(c.getUUID()));
      }
    }

    this.controller.tickServer();
    SignalAspect prevAspect = this.controller.getAspect();
    if (this.controller.isLinking()) {
      this.controller.setAspect(SignalAspect.BLINK_YELLOW);
    } else {
      this.controller.setAspect(tokenRing.getAspect());
    }
    if (prevAspect != this.controller.getAspect()) {
      this.syncToClient();
    }
  }

  @Override
  public SignalAspect getSignalAspect() {
    return this.controller.getAspect();
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    data = super.save(data);
    this.controller.writeToNBT(data);
    data.putUUID("tokenRingId", this.tokenRingId);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.controller.readFromNBT(data);
    this.tokenRingId = data.getUUID("tokenRingId");
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    this.controller.writeSyncData(data);
    data.writeBlockPos(this.getTokenRing().getCentroid());
    data.writeUUID(this.tokenRingId);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.controller.readSyncData(data);
    this.centroid = data.readBlockPos();
    this.tokenRingId = data.readUUID();
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    if (!this.level.isClientSide()) {
      this.getTokenRing().removeSignal(getBlockPos());
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
    if (this.centroid == null)
      return this.getBlockPos();
    return this.centroid;
  }

  @Override
  public SimpleSignalController getController() {
    return this.controller;
  }

  @Override
  public TokenRingImpl getTokenRing() {
    return TokenManager.get((ServerWorld) this.level)
        .getTokenRing(this.tokenRingId, this.getBlockPos());
  }

  @Override
  public TrackLocator getTrackLocator() {
    return this.trackLocator;
  }
}
