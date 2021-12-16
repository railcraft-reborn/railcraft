package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signal.BlockSignal;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalControllerProvider;
import mods.railcraft.api.signal.SimpleBlockSignalNetwork;
import mods.railcraft.api.signal.SimpleSignalController;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSignalBlockEntity extends AbstractSignalBlockEntity
    implements SignalControllerProvider, BlockSignal {

  private final SimpleSignalController signalController =
      new SimpleSignalController(1, this::syncToClient, this, false,
          __ -> this.level.getLightEngine().checkBlock(this.getBlockPos()));
  private final SimpleBlockSignalNetwork blockSignal =
      new SimpleBlockSignalNetwork(1, this::syncToClient, this.signalController::setSignalAspect,
          this);

  public BlockSignalBlockEntity(BlockPos blockPos, BlockState blockState) {
    this(RailcraftBlockEntityTypes.BLOCK_SIGNAL.get(), blockPos, blockState);
  }

  public BlockSignalBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
    super(type, blockPos, blockState);
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    this.signalController.removed();
    this.blockSignal.removed();
  }

  @Override
  public void onLoad() {
    super.onLoad();
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
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("blockSignal", this.blockSignal.serializeNBT());
    tag.put("signalController", this.signalController.serializeNBT());
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.blockSignal.deserializeNBT(tag.getCompound("blockSignal"));
    this.signalController.deserializeNBT(tag.getCompound("signalController"));
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    this.blockSignal.writeToBuf(data);
    this.signalController.writeToBuf(data);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.blockSignal.readFromBuf(data);
    this.signalController.readFromBuf(data);
  }

  @Override
  public SimpleSignalController getSignalController() {
    return this.signalController;
  }

  @Override
  public SimpleBlockSignalNetwork getSignalNetwork() {
    return this.blockSignal;
  }

  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState,
      BlockSignalBlockEntity blockEntity) {
    blockEntity.signalController.spawnTuningAuraParticles();
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      BlockSignalBlockEntity blockEntity) {
    blockEntity.blockSignal.serverTick();
  }
}
