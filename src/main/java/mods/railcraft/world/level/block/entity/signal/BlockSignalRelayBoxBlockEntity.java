package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signal.BlockSignal;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalController;
import mods.railcraft.api.signal.SignalControllerProvider;
import mods.railcraft.api.signal.SimpleBlockSignalNetwork;
import mods.railcraft.api.signal.SimpleSignalController;
import mods.railcraft.util.PowerUtil;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;

public class BlockSignalRelayBoxBlockEntity extends ActionSignalBoxBlockEntity
    implements BlockSignal, SignalControllerProvider {

  private final SimpleSignalController signalController =
      new SimpleSignalController(1, this::syncToClient, this, false);
  private final SimpleBlockSignalNetwork blockSignal =
      new SimpleBlockSignalNetwork(2, this::syncToClient, this::signalAspectChanged, this);

  public BlockSignalRelayBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_RELAY_BOX.get());
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    this.signalController.removed();
    this.blockSignal.removed();
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      this.signalController.spawnTuningAuraParticles();
      return;
    }
    this.blockSignal.tickServer();
  }

  @Override
  public void load() {
    if (!this.level.isClientSide()) {
      this.signalController.refresh();
      this.blockSignal.refresh();
    }
  }

  private void signalAspectChanged(SignalAspect signalAspect) {
    this.signalController.setSignalAspect(signalAspect);
    this.updateNeighborSignalBoxes(false);
  }

  @Override
  public int getRedstoneSignal(Direction side) {
    return this.isActionAspect(this.blockSignal.getSignalAspect())
        ? PowerUtil.FULL_POWER
        : PowerUtil.NO_POWER;
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.put("blockSignal", this.blockSignal.serializeNBT());
    data.put("signalController", this.signalController.serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.blockSignal.deserializeNBT(data.getCompound("blockSignal"));
    this.signalController.deserializeNBT(data.getCompound("signalController"));
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    this.blockSignal.writeSyncData(data);
    this.signalController.writeSyncData(data);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.blockSignal.readSyncData(data);
    this.signalController.readSyncData(data);
  }

  @Override
  public SimpleBlockSignalNetwork getSignalNetwork() {
    return this.blockSignal;
  }

  @Override
  public SignalAspect getSignalAspect(Direction direction) {
    return this.signalController.getSignalAspect();
  }

  @Override
  public SignalController getSignalController() {
    return this.signalController;
  }
}
