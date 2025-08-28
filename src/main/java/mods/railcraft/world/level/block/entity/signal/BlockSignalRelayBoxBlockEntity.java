package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.signal.BlockSignalEntity;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalController;
import mods.railcraft.api.signal.SimpleBlockSignalNetwork;
import mods.railcraft.api.signal.SimpleSignalController;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;

public class BlockSignalRelayBoxBlockEntity extends ActionSignalBoxBlockEntity
    implements BlockSignalEntity, SignalControllerEntity {

  private final SimpleSignalController signalController =
      new SimpleSignalController(1, this::syncToClient, this, false);
  private final SimpleBlockSignalNetwork blockSignal =
      new SimpleBlockSignalNetwork(2, this::syncToClient, this::signalAspectChanged, this);

  public BlockSignalRelayBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.BLOCK_SIGNAL_RELAY_BOX.get(), blockPos, blockState);
  }

  @Override
  protected void blockRemoved() {
    super.blockRemoved();
    this.signalController.destroy();
    this.blockSignal.destroy();
  }

  @Override
  public void onLoad() {
    super.onLoad();
    this.signalController.refresh();
    this.blockSignal.refresh();
  }

  private void signalAspectChanged(SignalAspect signalAspect) {
    this.signalController.setSignalAspect(signalAspect);
    this.updateNeighborSignalBoxes(false);
    this.level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
  }

  @Override
  public int getRedstoneSignal(Direction side) {
    return this.isActionSignalAspect(this.blockSignal.aspect())
        ? Redstone.SIGNAL_MAX
        : Redstone.SIGNAL_NONE;
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.put(CompoundTagKeys.BLOCK_SIGNAL, this.blockSignal.serializeNBT(provider));
    tag.put(CompoundTagKeys.SIGNAL_CONTROLLER, this.signalController.serializeNBT(provider));
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    tag.getCompound(CompoundTagKeys.BLOCK_SIGNAL).ifPresent(compoundTag -> {
      this.blockSignal.deserializeNBT(provider, compoundTag);
    });
    tag.getCompound(CompoundTagKeys.SIGNAL_CONTROLLER).ifPresent(compoundTag -> {
      this.signalController.deserializeNBT(provider, compoundTag);
    });
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
  public SimpleBlockSignalNetwork signalNetwork() {
    return this.blockSignal;
  }

  @Override
  public SignalAspect getSignalAspect(Direction direction) {
    return this.signalController.aspect();
  }

  @Override
  public SignalController getSignalController() {
    return this.signalController;
  }

  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState,
      BlockSignalRelayBoxBlockEntity blockEntity) {
    blockEntity.signalController.spawnTuningAuraParticles();
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      BlockSignalRelayBoxBlockEntity blockEntity) {
    blockEntity.blockSignal.serverTick();
  }
}
