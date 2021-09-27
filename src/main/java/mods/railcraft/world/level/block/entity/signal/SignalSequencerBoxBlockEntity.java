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
  private boolean powerState;
  private boolean neighborState;

  public SignalSequencerBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_SEQUENCER_BOX.get());
  }

  @Override
  public void neighborChanged(BlockState state, Block neighborBlock, BlockPos pos) {
    super.neighborChanged(state, neighborBlock, pos);
    if (this.level.isClientSide())
      return;
    boolean p = PowerPlugin.isBlockBeingPoweredByRepeater(this.level, this.getBlockPos());
    if (!this.powerState && p) {
      this.powerState = true;
      incrementSequencer(true, new HashSet<>(), 0);
    } else {
      this.powerState = p;
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
    boolean p = neighbor.isEmittingRedstone(side);
    if (!neighborState && p) {
      neighborState = true;
      incrementSequencer(true, new HashSet<>(), 0);
    } else
      neighborState = p;
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

  private void updateNeighbors() {
    sendUpdateToClient();
    notifyBlocksOfNeighborChange();
    updateNeighborBoxes();
  }

  @Override
  public int getSignal(Direction side) {
    TileEntity tile = this.adjacentCache.getTileOnSide(side.getOpposite());
    if (tile instanceof AbstractSignalBoxBlockEntity)
      return PowerPlugin.NO_POWER;
    return sideOutput.getOpposite() == side ? PowerPlugin.FULL_POWER : PowerPlugin.NO_POWER;
  }

  @Override
  public boolean isEmittingRedstone(Direction side) {
    return sideOutput == side;
  }

  @Override
  public SignalAspect getBoxSignalAspect(@Nullable Direction side) {
    return sideOutput == side ? SignalAspect.GREEN : SignalAspect.RED;
  }


  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putInt("sideOutput", sideOutput.get3DDataValue());
    data.putBoolean("powerState", powerState);
    data.putBoolean("neighborState", neighborState);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    sideOutput = Direction.from3DDataValue(data.getInt("sideOutput"));
    powerState = data.getBoolean("powerState");
    neighborState = data.getBoolean("neighborState");
  }

  @Override
  public void writePacketData(PacketBuffer data) {
    super.writePacketData(data);
    data.writeVarInt(sideOutput.get3DDataValue());
  }

  @Override
  public void readPacketData(PacketBuffer data) {
    super.readPacketData(data);
    sideOutput = Direction.from3DDataValue(data.readVarInt());
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
