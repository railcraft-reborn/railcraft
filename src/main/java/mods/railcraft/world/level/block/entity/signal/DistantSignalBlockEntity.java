package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signals.IReceiverProvider;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SignalController;
import mods.railcraft.api.signals.SignalReceiver;
import mods.railcraft.api.signals.SimpleSignalReceiver;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class DistantSignalBlockEntity extends AbstractSignalBlockEntity implements IReceiverProvider {

  private final SimpleSignalReceiver receiver =
      new SimpleSignalReceiver("something", this);

  public DistantSignalBlockEntity() {
    super(RailcraftBlockEntityTypes.DISTANT_SIGNAL.get());
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
  public SignalReceiver getReceiver() {
    return receiver;
  }

  @Override
  public SignalAspect getSignalAspect() {
    return receiver.getAspect();
  }
}
