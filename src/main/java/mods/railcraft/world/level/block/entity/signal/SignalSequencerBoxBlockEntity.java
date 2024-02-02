package mods.railcraft.world.level.block.entity.signal;

import java.util.HashSet;
import java.util.Set;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.RedstoneUtil;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;

public class SignalSequencerBoxBlockEntity extends AbstractSignalBoxBlockEntity {

  private static final int MAX_ITERATIONS = 64;
  private Direction outputDirection = Direction.NORTH;
  private boolean powered;
  private boolean neighborSignal;

  public SignalSequencerBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.SIGNAL_SEQUENCER_BOX.get(), blockPos, blockState);
  }

  @Override
  public void neighborChanged() {
    if (this.level.isClientSide()) {
      return;
    }
    var powered = RedstoneUtil.hasRepeaterSignal(this.level, this.getBlockPos());
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
    var signal = neighborSignalBox.getRedstoneSignal(neighborDirection) > 0;
    if (!this.neighborSignal && signal) {
      this.neighborSignal = true;
      this.incrementSequencer(true, new HashSet<>(), 0);
    } else {
      this.neighborSignal = signal;
    }
  }

  private void incrementSequencer(boolean firstPass, Set<BlockEntity> visitedFirstPass,
      int numberOfIterations) {
    if (firstPass) {
      visitedFirstPass.add(this);
      var blockEntity =
          this.level.getBlockEntity(this.getBlockPos().relative(this.outputDirection));
      if (blockEntity instanceof SignalSequencerBoxBlockEntity signalBox
          && !visitedFirstPass.contains(blockEntity)) {
        signalBox.incrementSequencer(true, visitedFirstPass, numberOfIterations);
        return;
      }
    }

    var lastOutputDirection = this.outputDirection;
    do {
      this.outputDirection = this.outputDirection.getClockWise();
    } while (!this.canOutputToDirection(this.outputDirection)
        && lastOutputDirection != this.outputDirection);

    this.setChanged();

    if (numberOfIterations >= MAX_ITERATIONS) {
      return;
    }

    var blockEntity =
        this.level.getBlockEntity(this.getBlockPos().relative(this.outputDirection));
    if (blockEntity instanceof SignalSequencerBoxBlockEntity signalBox) {
      signalBox.incrementSequencer(false, visitedFirstPass, numberOfIterations + 1);
    }
  }

  private boolean canOutputToDirection(Direction direction) {
    var blockState = this.level.getBlockState(this.getBlockPos().relative(direction));
    if (blockState.is(this.getBlockState().getBlock())
        || blockState.is(RailcraftTags.Blocks.ASPECT_RECEIVER))
      return true;
    var block = blockState.getBlock();
    if (block == Blocks.REDSTONE_WIRE) {
      return true;
    }
    if (block == Blocks.REPEATER) {
      Direction facing = blockState.getValue(HorizontalDirectionalBlock.FACING);
      return direction.getOpposite() == facing;
    }
    return false;
  }

  @Override
  public void setChanged() {
    super.setChanged();
    this.syncToClient();
  }

  @Override
  public int getRedstoneSignal(Direction direction) {
    return this.level.getBlockEntity(
        this.getBlockPos().relative(direction)) instanceof AbstractSignalBoxBlockEntity
            ? Redstone.SIGNAL_NONE
            : this.outputDirection.getOpposite() == direction
                ? Redstone.SIGNAL_MAX
                : Redstone.SIGNAL_NONE;
  }

  @Override
  public SignalAspect getSignalAspect(Direction direction) {
    return this.outputDirection == direction ? SignalAspect.GREEN : SignalAspect.RED;
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putString(CompoundTagKeys.OUTPUT_DIRECTION, this.outputDirection.getName());
    tag.putBoolean(CompoundTagKeys.POWERED, this.powered);
    tag.putBoolean(CompoundTagKeys.NEIGHBOR_SIGNAL, this.neighborSignal);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.outputDirection = Direction.byName(tag.getString(CompoundTagKeys.OUTPUT_DIRECTION));
    this.powered = tag.getBoolean(CompoundTagKeys.POWERED);
    this.neighborSignal = tag.getBoolean(CompoundTagKeys.NEIGHBOR_SIGNAL);
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeVarInt(this.outputDirection.get3DDataValue());
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.outputDirection = Direction.from3DDataValue(data.readVarInt());
  }
}
