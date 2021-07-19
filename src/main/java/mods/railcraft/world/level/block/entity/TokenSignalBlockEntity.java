package mods.railcraft.world.level.block.entity;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import mods.railcraft.api.signals.IControllerProvider;
import mods.railcraft.api.signals.ITokenSignal;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SimpleSignalController;
import mods.railcraft.api.signals.TrackLocator;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.collections.TimerBag;
import mods.railcraft.world.signal.TokenManager;
import mods.railcraft.world.signal.TokenRing;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * Created by CovertJaguar on 4/13/2015 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class TokenSignalBlockEntity extends AbstractSignalBlockEntity
    implements IControllerProvider, ITokenSignal {

  private final SimpleSignalController controller =
      new SimpleSignalController("nothing", this);
  private UUID tokenRingUUID = UUID.randomUUID();
  private BlockPos centroid;
  private final TrackLocator trackLocator = new TrackLocator(this);
  private final TimerBag<UUID> cartTimers = new TimerBag<>(8);

  public TokenSignalBlockEntity() {
    this(RailcraftBlockEntityTypes.TOKEN_SIGNAL.get());
  }

  public TokenSignalBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      controller.tickClient();
      return;
    }

    TokenRing tokenRing = getTokenRing();
    if (!Objects.equals(centroid, tokenRing.centroid())) {
      centroid = tokenRing.centroid();
      sendUpdateToClient();
    }

    cartTimers.tick();
    if (trackLocator.getTrackStatus() == TrackLocator.Status.VALID) {
      BlockPos trackPos = trackLocator.getTrackLocation();
      if (trackPos != null) {
        List<AbstractMinecartEntity> carts = EntitySearcher.findMinecarts()
            .around(trackPos)
            .in(this.level);
        carts.stream().filter(c -> !cartTimers.contains(c.getUUID()))
            .forEach(tokenRing::markCart);
        carts.forEach(c -> cartTimers.add(c.getUUID()));
      }
    }


    controller.tickServer();
    SignalAspect prevAspect = controller.getAspect();
    if (controller.isLinking()) {
      controller.setAspect(SignalAspect.BLINK_YELLOW);
    } else {
      controller.setAspect(tokenRing.getAspect());
    }
    if (prevAspect != controller.getAspect()) {
      sendUpdateToClient();
    }
  }

  @Override
  public SignalAspect getSignalAspect() {
    return controller.getAspect();
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    data = super.save(data);
    controller.writeToNBT(data);
    data.putUUID("tokenRingUUID", tokenRingUUID);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    controller.readFromNBT(data);
    tokenRingUUID = data.getUUID("tokenRingUUID");
  }

  @Override
  public void writePacketData(PacketBuffer data) {
    super.writePacketData(data);
    controller.writePacketData(data);
    data.writeBlockPos(getTokenRing().centroid());
    data.writeUUID(tokenRingUUID);
  }

  @Override
  public void readPacketData(PacketBuffer data) {
    super.readPacketData(data);
    controller.readPacketData(data);
    centroid = data.readBlockPos();
    tokenRingUUID = data.readUUID();
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    if (!this.level.isClientSide()) {
      this.getTokenRing().removeSignal(getBlockPos());
    }
  }

  public UUID getTokenRingUUID() {
    return tokenRingUUID;
  }

  public void setTokenRingUUID(UUID tokenRingUUID) {
    this.tokenRingUUID = tokenRingUUID;
  }

  public BlockPos getTokenRingCentroid() {
    if (centroid == null)
      return getBlockPos();
    return centroid;
  }

  @Override
  public SimpleSignalController getController() {
    return controller;
  }

  @Override
  public TokenRing getTokenRing() {
    return TokenManager.getManager((ServerWorld) this.level)
        .getTokenRing(tokenRingUUID, this.getBlockPos());
  }

  @Override
  public TrackLocator getTrackLocator() {
    return trackLocator;
  }
}
