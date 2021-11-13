package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signal.BlockSignal;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalControllerProvider;
import mods.railcraft.api.signal.SimpleBlockSignalNetwork;
import mods.railcraft.api.signal.SimpleSignalController;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;

public class BlockSignalBlockEntity extends AbstractSignalBlockEntity
    implements SignalControllerProvider, BlockSignal, ITickableTileEntity {

  private final SimpleSignalController signalController =
      new SimpleSignalController(1, this::syncToClient, this, false,
          __ -> this.level.getLightEngine().checkBlock(this.getBlockPos()));
  private final SimpleBlockSignalNetwork blockSignal =
      new SimpleBlockSignalNetwork(1, this::syncToClient, this.signalController::setSignalAspect,
          this);

  public BlockSignalBlockEntity() {
    this(RailcraftBlockEntityTypes.BLOCK_SIGNAL.get());
  }

  public BlockSignalBlockEntity(TileEntityType<?> type) {
    super(type);
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

  @Override
  public SignalAspect getPrimarySignalAspect() {
    return this.blockSignal.getSignalAspect();
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
  public SimpleSignalController getSignalController() {
    return this.signalController;
  }

  @Override
  public SimpleBlockSignalNetwork getSignalNetwork() {
    return this.blockSignal;
  }
}
