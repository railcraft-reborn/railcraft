package mods.railcraft.world.level.block.entity.signal;

import javax.annotation.Nullable;
import mods.railcraft.api.signals.IReceiverProvider;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SignalController;
import mods.railcraft.api.signals.SimpleSignalReceiver;
import mods.railcraft.plugins.PowerPlugin;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public class SignalReceiverBoxBlockEntity extends ActionSignalBoxBlockEntity
    implements IReceiverProvider, IAspectProvider, IRedstoneEmitter {

  private static final int FORCED_UPDATE = 512;
  private final SimpleSignalReceiver receiver =
      new SimpleSignalReceiver("something", this);

  public SignalReceiverBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_RECEIVER_BOX.get());
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      receiver.tickClient();
      return;
    }
    receiver.tickServer();
    if (this.clock(FORCED_UPDATE)) {
      updateNeighbors();
      sendUpdateToClient();
    }
  }

  @Override
  public void onControllerAspectChange(SignalController con, SignalAspect aspect) {
    updateNeighbors();
    sendUpdateToClient();
  }

  private void updateNeighbors() {
    notifyBlocksOfNeighborChange();
    updateNeighborBoxes();
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    receiver.writeToNBT(data);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    receiver.readFromNBT(data);
  }

  @Override
  public void writePacketData(PacketBuffer data) {
    super.writePacketData(data);
    receiver.writePacketData(data);
  }

  @Override
  public void readPacketData(PacketBuffer data) {
    super.readPacketData(data);
    receiver.readPacketData(data);
  }

  @Override
  public int getSignal(Direction side) {
    TileEntity tile = this.level.getBlockEntity(this.getBlockPos().relative(side.getOpposite()));
    if (tile instanceof AbstractSignalBoxBlockEntity)
      return PowerPlugin.NO_POWER;
    return isEmittingRedstone(side) ? PowerPlugin.FULL_POWER : PowerPlugin.NO_POWER;
  }

  @Override
  public boolean isEmittingRedstone(Direction side) {
    return doesActionOnAspect(receiver.getAspect());
  }

  @Override
  public void doActionOnAspect(SignalAspect aspect, boolean trigger) {
    super.doActionOnAspect(aspect, trigger);
    updateNeighbors();
  }

  @Override
  public SignalAspect getBoxSignalAspect(@Nullable Direction side) {
    return receiver.getAspect();
  }

  @Override
  public SimpleSignalReceiver getReceiver() {
    return receiver;
  }

  @Override
  public SignalAspect getTriggerAspect() {
    return getBoxSignalAspect(null);
  }

  @Override
  public boolean canTransferAspect() {
    return true;
  }
}
