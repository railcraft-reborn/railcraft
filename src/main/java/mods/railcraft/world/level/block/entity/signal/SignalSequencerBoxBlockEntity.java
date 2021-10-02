package mods.railcraft.world.level.block.entity.signal;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.plugins.PowerPlugin;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public class SignalSequencerBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements SignalEmitter {

  private static final int MAX_ITERATIONS = 64;
  private Direction outputDirection = Direction.NORTH;
  private boolean signal;
  private boolean neighborSignal;

  public SignalSequencerBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_SEQUENCER_BOX.get());
  }

  @Override
  public void neighborChanged() {
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
    boolean signal = neighbor.getRedstoneSignal(side) > 0;
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
      TileEntity blockEntity =
          this.level.getBlockEntity(this.getBlockPos().relative(this.outputDirection));
      if (blockEntity instanceof SignalSequencerBoxBlockEntity
          && !visitedFirstPass.contains(blockEntity)) {
        SignalSequencerBoxBlockEntity signalBox = (SignalSequencerBoxBlockEntity) blockEntity;
        signalBox.incrementSequencer(true, visitedFirstPass, numberOfIterations);
        return;
      }
    }

    Direction outputDirection = this.outputDirection.getClockWise();
    while (outputDirection != this.outputDirection && !this.canOutputToSide(outputDirection)) {
      outputDirection = outputDirection.getClockWise();
    }
    this.outputDirection = outputDirection;
    this.updateNeighbors();

    if (numberOfIterations >= MAX_ITERATIONS)
      return;

    TileEntity blockEntity =
        this.level.getBlockEntity(this.getBlockPos().relative(this.outputDirection));
    if (blockEntity instanceof SignalSequencerBoxBlockEntity) {
      SignalSequencerBoxBlockEntity signalBox = (SignalSequencerBoxBlockEntity) blockEntity;
      signalBox.incrementSequencer(false, visitedFirstPass, numberOfIterations + 1);
    }
  }

  private boolean canOutputToSide(Direction direction) {
    BlockState blockState = this.level.getBlockState(this.getBlockPos().relative(direction));
    if (blockState.is(this.getBlockState().getBlock())
        || blockState.is(RailcraftTags.Blocks.ASPECT_RECEIVER))
      return true;
    Block block = blockState.getBlock();
    if (block == Blocks.REDSTONE_WIRE) {
      return true;
    }
    if (block == Blocks.REPEATER) {
      Direction facing = blockState.getValue(HorizontalBlock.FACING);
      return direction.getOpposite() == facing;
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
  public int getRedstoneSignal(Direction direction) {
    return this.level.getBlockState(this.getBlockPos().relative(direction))
        .is(RailcraftTags.Blocks.SIGNAL_BOX)
            ? PowerPlugin.NO_POWER
            : this.outputDirection.getOpposite() == direction
                ? PowerPlugin.FULL_POWER
                : PowerPlugin.NO_POWER;
  }

  @Override
  public SignalAspect getSignalAspect(@Nullable Direction side) {
    return this.outputDirection == side ? SignalAspect.GREEN : SignalAspect.RED;
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putInt("sideOutput", this.outputDirection.get3DDataValue());
    data.putBoolean("powerState", this.signal);
    data.putBoolean("neighborState", this.neighborSignal);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.outputDirection = Direction.from3DDataValue(data.getInt("sideOutput"));
    this.signal = data.getBoolean("powerState");
    this.neighborSignal = data.getBoolean("neighborState");
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeVarInt(this.outputDirection.get3DDataValue());
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.outputDirection = Direction.from3DDataValue(data.readVarInt());
  }
}
