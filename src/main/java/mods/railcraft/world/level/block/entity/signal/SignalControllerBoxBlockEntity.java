package mods.railcraft.world.level.block.entity.signal;

import javax.annotation.Nullable;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SignalControllerProvider;
import mods.railcraft.api.signals.SimpleSignalController;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public class SignalControllerBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements SignalControllerProvider {

  private final SimpleSignalController controller = new SimpleSignalController("nothing", this);
  public SignalAspect defaultAspect = SignalAspect.GREEN;
  public SignalAspect poweredAspect = SignalAspect.RED;
  private boolean redstoneSignal;

  private boolean loaded;

  public SignalControllerBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_CONTROLLER_BOX.get());
  }

  @Override
  public void tick() {
    super.tick();

    if (this.level.isClientSide()) {
      this.controller.tickClient();
      return;
    }

    // Can't use onLoad as it causes a deadlock for some reason...
    if (!this.loaded) {
      this.loaded = true;
      this.updateRedstoneState();
    }

    this.controller.tickServer();
    SignalAspect prevAspect = this.controller.getAspect();
    if (this.controller.isLinking()) {
      this.controller.setAspect(SignalAspect.BLINK_YELLOW);
    } else if (this.controller.isPaired())
      this.controller.setAspect(determineAspect());
    else
      this.controller.setAspect(SignalAspect.BLINK_RED);
    if (prevAspect != this.controller.getAspect())
      syncToClient();
  }

  @Override
  public void neighborChanged() {
    super.neighborChanged();
    if (this.level.isClientSide())
      return;
    this.updateRedstoneState();
  }

  private void updateRedstoneState() {
    boolean redstoneSignal = this.level.hasNeighborSignal(this.getBlockPos());
    if (redstoneSignal != this.redstoneSignal) {
      this.redstoneSignal = redstoneSignal;
      this.syncToClient();
    }
  }

  private SignalAspect determineAspect() {
    SignalAspect newAspect = this.redstoneSignal ? this.poweredAspect : this.defaultAspect;
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
    data.putBoolean("redstoneSignal", this.redstoneSignal);

    this.defaultAspect.writeToNBT(data, "defaultAspect");
    this.poweredAspect.writeToNBT(data, "poweredAspect");

    this.controller.writeToNBT(data);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.redstoneSignal = data.getBoolean("redstoneSignal");

    this.defaultAspect = SignalAspect.readFromNBT(data, "defaultAspect");
    this.poweredAspect = SignalAspect.readFromNBT(data, "poweredAspect");

    this.controller.readFromNBT(data);
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);

    data.writeVarInt(this.defaultAspect.getId());
    data.writeVarInt(this.poweredAspect.getId());

    this.controller.writeSyncData(data);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);

    this.defaultAspect = SignalAspect.getById(data.readVarInt());
    this.poweredAspect = SignalAspect.getById(data.readVarInt());

    this.controller.readSyncData(data);
  }

  @Override
  public SignalAspect getBoxSignalAspect(@Nullable Direction side) {
    return this.controller.getAspect();
  }

  @Override
  public SimpleSignalController getController() {
    return this.controller;
  }

  @Override
  public boolean canReceiveAspect() {
    return true;
  }
}
