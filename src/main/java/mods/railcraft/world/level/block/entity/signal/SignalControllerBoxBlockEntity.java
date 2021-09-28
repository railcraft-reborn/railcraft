package mods.railcraft.world.level.block.entity.signal;

import javax.annotation.Nullable;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SignalController;
import mods.railcraft.api.signals.SimpleSignalControllerNetwork;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public class SignalControllerBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements SignalController {

  private final SimpleSignalControllerNetwork signalControllerNetwork =
      new SimpleSignalControllerNetwork(this, this::syncToClient);
  public SignalAspect defaultAspect = SignalAspect.GREEN;
  public SignalAspect poweredAspect = SignalAspect.RED;
  private boolean signal;

  private boolean loaded;

  public SignalControllerBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_CONTROLLER_BOX.get());
  }

  @Override
  public void tick() {
    super.tick();

    if (this.level.isClientSide()) {
      this.signalControllerNetwork.tickClient();
      return;
    }

    // Can't use onLoad as it causes a deadlock for some reason...
    if (!this.loaded) {
      this.loaded = true;
      this.updateSignal();
    }

    this.signalControllerNetwork.tickServer();
    SignalAspect lastAspect = this.signalControllerNetwork.getAspect();
    if (this.signalControllerNetwork.isLinking()) {
      this.signalControllerNetwork.setAspect(SignalAspect.BLINK_YELLOW);
    } else if (this.signalControllerNetwork.hasPeers())
      this.signalControllerNetwork.setAspect(determineAspect());
    else
      this.signalControllerNetwork.setAspect(SignalAspect.BLINK_RED);
    if (lastAspect != this.signalControllerNetwork.getAspect())
      this.syncToClient();
  }

  @Override
  public void neighborChanged() {
    super.neighborChanged();
    if (this.level.isClientSide())
      return;
    this.updateSignal();
  }

  private void updateSignal() {
    boolean signal = this.level.hasNeighborSignal(this.getBlockPos());
    if (signal != this.signal) {
      this.signal = signal;
      this.syncToClient();
    }
  }

  private SignalAspect determineAspect() {
    SignalAspect newAspect = this.signal ? this.poweredAspect : this.defaultAspect;
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      TileEntity blockEntity = this.adjacentCache.getTileOnSide(direction);
      if (blockEntity instanceof AbstractSignalBoxBlockEntity) {
        AbstractSignalBoxBlockEntity signalBox = (AbstractSignalBoxBlockEntity) blockEntity;
        if (signalBox.canTransferAspect()) {
          newAspect = SignalAspect.mostRestrictive(newAspect,
              signalBox.getBoxSignalAspect(direction.getOpposite()));
        }
      }
    }
    return newAspect;
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putBoolean("redstoneSignal", this.signal);
    data.putString("defaultAspect", this.defaultAspect.getName());
    data.putString("poweredAspect", this.poweredAspect.getName());
    data.put("signalControllerNetwork", this.signalControllerNetwork.serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.signal = data.getBoolean("redstoneSignal");
    this.defaultAspect = SignalAspect.getByName(data.getString("defaultAspect")).get();
    this.poweredAspect = SignalAspect.getByName(data.getString("poweredAspect")).get();
    this.signalControllerNetwork.deserializeNBT(data.getCompound("signalControllerNetwork"));
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeEnum(this.defaultAspect);
    data.writeEnum(this.poweredAspect);
    this.signalControllerNetwork.writeSyncData(data);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.defaultAspect = data.readEnum(SignalAspect.class);
    this.poweredAspect = data.readEnum(SignalAspect.class);
    this.signalControllerNetwork.readSyncData(data);
  }

  @Override
  public SignalAspect getBoxSignalAspect(@Nullable Direction direction) {
    return this.signalControllerNetwork.getAspect();
  }

  @Override
  public SimpleSignalControllerNetwork getSignalControllerNetwork() {
    return this.signalControllerNetwork;
  }

  @Override
  public boolean canReceiveAspect() {
    return true;
  }
}
