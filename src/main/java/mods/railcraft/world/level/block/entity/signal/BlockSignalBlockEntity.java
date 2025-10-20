package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.signal.BlockSignalEntity;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SimpleBlockSignalNetwork;
import mods.railcraft.api.signal.SimpleSignalController;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class BlockSignalBlockEntity extends AbstractSignalBlockEntity
    implements SignalControllerEntity, BlockSignalEntity {

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
  public void preRemoveSideEffects(BlockPos pos, BlockState state) {
    super.preRemoveSideEffects(pos, state);
    this.blockRemoved();
  }

  protected void blockRemoved() {
    this.signalController.destroy();
    this.blockSignal.destroy();
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
    return this.blockSignal.aspect();
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.putChild(CompoundTagKeys.BLOCK_SIGNAL, this.blockSignal);
    output.putChild(CompoundTagKeys.SIGNAL_CONTROLLER, this.signalController);
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    input.readChild(CompoundTagKeys.BLOCK_SIGNAL, this.blockSignal);
    input.readChild(CompoundTagKeys.SIGNAL_CONTROLLER, this.signalController);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf data) {
    super.writeToBuf(data);
    this.blockSignal.writeToBuf(data);
    this.signalController.writeToBuf(data);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf data) {
    super.readFromBuf(data);
    this.blockSignal.readFromBuf(data);
    this.signalController.readFromBuf(data);
  }

  @Override
  public SimpleSignalController getSignalController() {
    return this.signalController;
  }

  @Override
  public SimpleBlockSignalNetwork signalNetwork() {
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
