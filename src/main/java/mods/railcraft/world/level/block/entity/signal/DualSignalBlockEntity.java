package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signals.DualLamp;
import mods.railcraft.api.signals.IReceiverProvider;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SignalController;
import mods.railcraft.api.signals.SimpleSignalReceiver;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class DualSignalBlockEntity extends BlockSignalBlockEntity implements IReceiverProvider, IDualSignal {

  private final SimpleSignalReceiver receiver = new SimpleSignalReceiver("nothing", this);

  public DualSignalBlockEntity() {
    super(RailcraftBlockEntityTypes.DUAL_SIGNAL.get());
  }

  @Override
  public int getLightValue() {
    return Math.max(getSignalAspect().getBlockLight(),
        getSignalAspect(DualLamp.BOTTOM).getBlockLight());
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      receiver.tickClient();
      return;
    }
    receiver.tickServer();
  }

  @Override
  public void onControllerAspectChange(SignalController con, SignalAspect aspect) {
    sendUpdateToClient();
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
  public SimpleSignalReceiver getReceiver() {
    return receiver;
  }

  @Override
  public SignalAspect getSignalAspect(DualLamp lamp) {
    if (lamp == DualLamp.BOTTOM)
      return receiver.getAspect();
    return getSignalAspect();
  }
}
