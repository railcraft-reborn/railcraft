package mods.railcraft.world.level.block.entity.signal;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.plugins.PowerPlugin;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class SignalSequencerBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements IRedstoneEmitter {

  private static final int MAX_ITERATIONS = 64;
  private Direction sideOutput = Direction.NORTH;
  private boolean signal;
  private boolean neighborSignal;

  public SignalSequencerBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_SEQUENCER_BOX.get());
  }

  @Override
  public void neighborChanged() {
    super.neighborChanged();
    if (this.level.isClientSide())
      return;
    boolean signal = PowerPlugin.hasRepeaterSignal(this.level, this.getBlockPos());
    if (!this.signal && signal) {
      this.signal = true;
      this.incrementSequencer(true, new HashSet<>(), 0);
    } else {
      this.signal = signal;
    }
  }

  @Override
  public void neighboringSignalBoxChanged(AbstractSignalBoxBlockEntity neighbor, Direction side) {
    if (this.level.isClientSide())
      return;
    if (neighbor instanceof SignalSequencerBoxBlockEntity)
      return;
    if (neighbor instanceof SignalCapacitorBoxBlockEntity)
      return;
    boolean signal = neighbor.isEmittingRedstone(side);
    if (!this.neighborSignal && signal) {
      this.neighborSignal = true;
      this.incrementSequencer(true, new HashSet<>(), 0);
    } else
      this.neighborSignal = signal;
  }

  private void incrementSequencer(boolean firstPass, Set<TileEntity> visitedFirstPass,
      int numberOfIterations) {
    if (firstPass) {
      visitedFirstPass.add(this);
      TileEntity tile = this.adjacentCache.getTileOnSide(this.sideOutput);
      if (tile instanceof SignalSequencerBoxBlockEntity && !visitedFirstPass.contains(tile)) {
        SignalSequencerBoxBlockEntity box = (SignalSequencerBoxBlockEntity) tile;
        box.incrementSequencer(true, visitedFirstPass, numberOfIterations);
        return;
      }
    }

    Direction newSide = this.sideOutput.getClockWise();
    while (newSide != sideOutput && !canOutputToSide(newSide)) {
      newSide = newSide.getClockWise();
    }
    sideOutput = newSide;
    updateNeighbors();

    if (numberOfIterations >= MAX_ITERATIONS)
      return;

    TileEntity tile = this.adjacentCache.getTileOnSide(sideOutput);
    if (tile instanceof SignalSequencerBoxBlockEntity) {
      SignalSequencerBoxBlockEntity box = (SignalSequencerBoxBlockEntity) tile;
      box.incrementSequencer(false, visitedFirstPass, numberOfIterations + 1);
    }
  }

  private boolean canOutputToSide(Direction side) {
    TileEntity tile = this.adjacentCache.getTileOnSide(side);
    if (tile instanceof SignalSequencerBoxBlockEntity)
      return true;
    if (tile instanceof AbstractSignalBoxBlockEntity)
      return ((AbstractSignalBoxBlockEntity) tile).canReceiveAspect();
    BlockState state = this.level.getBlockState(this.getBlockPos().relative(side));
    Block block = state.getBlock();
    if (block == Blocks.REDSTONE_WIRE)
      return true;
    if (block == Blocks.REPEATER) {
      Direction inputSide = state.getValue(HorizontalBlock.FACING);
      return side.getOpposite() == inputSide;
    }
    return false;
  }

  @Override
  public void updateNeighbors() {
    super.updateNeighbors();
    this.syncToClient();
    this.updateNeighborBoxes();
  }

  @Override
  public int getSignal(Direction direction) {
    TileEntity blockEntity = this.adjacentCache.getTileOnSide(direction.getOpposite());
    return blockEntity instanceof AbstractSignalBoxBlockEntity
        ? PowerPlugin.NO_POWER
        : this.sideOutput.getOpposite() == direction
            ? PowerPlugin.FULL_POWER
            : PowerPlugin.NO_POWER;
  }

  @Override
  public boolean isEmittingRedstone(Direction side) {
    return this.sideOutput == side;
  }

  @Override
  public SignalAspect getBoxSignalAspect(@Nullable Direction side) {
    return this.sideOutput == side ? SignalAspect.GREEN : SignalAspect.RED;
  }


  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putInt("sideOutput", this.sideOutput.get3DDataValue());
    data.putBoolean("powerState", this.signal);
    data.putBoolean("neighborState", this.neighborSignal);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.sideOutput = Direction.from3DDataValue(data.getInt("sideOutput"));
    this.signal = data.getBoolean("powerState");
    this.neighborSignal = data.getBoolean("neighborState");
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeVarInt(this.sideOutput.get3DDataValue());
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.sideOutput = Direction.from3DDataValue(data.readVarInt());
  }

  @Override
  public boolean attachesTo(BlockState state, BlockPos pos, Direction side) {
    return this.adjacentCache.getTileOnSide(side) instanceof SignalSequencerBoxBlockEntity
        || super.attachesTo(state, pos, side);
  }

  @Override
  public boolean canTransferAspect() {
    return true;
  }

  @Override
  public boolean canReceiveAspect() {
    return true;
  }
}
