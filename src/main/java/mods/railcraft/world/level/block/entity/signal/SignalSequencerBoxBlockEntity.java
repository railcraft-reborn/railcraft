package mods.railcraft.world.level.block.entity.signal;

import java.util.HashSet;
import java.util.Set;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.PowerUtil;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public class SignalSequencerBoxBlockEntity extends AbstractSignalBoxBlockEntity {

  private static final int MAX_ITERATIONS = 64;
  private Direction outputDirection = Direction.NORTH;
  private boolean powered;
  private boolean neighborSignal;

  public SignalSequencerBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_SEQUENCER_BOX.get());
  }

  @Override
  public void neighborChanged() {
    if (this.level.isClientSide()) {
      return;
    }
    boolean powered = PowerUtil.hasRepeaterSignal(this.level, this.getBlockPos());
    if (!this.powered && powered) {
      this.powered = true;
      this.incrementSequencer(true, new HashSet<>(), 0);
    } else {
      this.powered = powered;
    }
  }

  @Override
  public void neighborSignalBoxChanged(AbstractSignalBoxBlockEntity neighborSignalBox,
      Direction neighborDirection, boolean removed) {
    if (this.level.isClientSide()
        || neighborSignalBox instanceof SignalSequencerBoxBlockEntity
        || neighborSignalBox instanceof SignalCapacitorBoxBlockEntity) {
      return;
    }
    boolean signal = neighborSignalBox.getRedstoneSignal(neighborDirection) > 0;
    if (!this.neighborSignal && signal) {
      this.neighborSignal = true;
      this.incrementSequencer(true, new HashSet<>(), 0);
    } else {
      this.neighborSignal = signal;
    }
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

    Direction lastOutputDirection = this.outputDirection;
    do {
      this.outputDirection = this.outputDirection.getClockWise();
    } while (!this.canOutputToDirection(this.outputDirection)
        && lastOutputDirection != this.outputDirection);

    this.updateNeighbors();

    if (numberOfIterations >= MAX_ITERATIONS) {
      return;
    }

    TileEntity blockEntity =
        this.level.getBlockEntity(this.getBlockPos().relative(this.outputDirection));
    if (blockEntity instanceof SignalSequencerBoxBlockEntity) {
      SignalSequencerBoxBlockEntity signalBox = (SignalSequencerBoxBlockEntity) blockEntity;
      signalBox.incrementSequencer(false, visitedFirstPass, numberOfIterations + 1);
    }
  }

  private boolean canOutputToDirection(Direction direction) {
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
    this.syncToClient();
    super.updateNeighbors();
    this.updateNeighborSignalBoxes(false);
  }

  @Override
  public int getRedstoneSignal(Direction direction) {
    return this.level.getBlockEntity(
        this.getBlockPos().relative(direction)) instanceof AbstractSignalBoxBlockEntity
            ? PowerUtil.NO_POWER
            : this.outputDirection.getOpposite() == direction
                ? PowerUtil.FULL_POWER
                : PowerUtil.NO_POWER;
  }

  @Override
  public SignalAspect getSignalAspect(Direction direction) {
    return this.outputDirection == direction ? SignalAspect.GREEN : SignalAspect.RED;
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putString("outputDirection", this.outputDirection.getName());
    data.putBoolean("powered", this.powered);
    data.putBoolean("neighborSignal", this.neighborSignal);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.outputDirection = Direction.byName(data.getString("outputDirection"));
    this.powered = data.getBoolean("powered");
    this.neighborSignal = data.getBoolean("neighborSignal");
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
