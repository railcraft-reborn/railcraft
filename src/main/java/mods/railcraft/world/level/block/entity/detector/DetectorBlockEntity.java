package mods.railcraft.world.level.block.entity.detector;

import java.util.List;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.world.entity.vehicle.CartConstants;
import mods.railcraft.world.level.block.detector.DetectorBlock;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class DetectorBlockEntity extends RailcraftBlockEntity {

  private static final float SENSITIVITY = 0.2f;
  private int powerState, powerDelay, tick;

  protected DetectorBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
    super(type, blockPos, blockState);
  }

  protected int updateInterval() {
    return 0;
  }

  protected boolean shouldTest() {
    return true; // RoutingDetector = isLogicValid();
  }

  protected int testCarts(List<AbstractMinecart> minecarts) {
    return minecarts.isEmpty() ? Redstone.SIGNAL_NONE : Redstone.SIGNAL_MAX;
  }

  private static List<AbstractMinecart> getCarts(Level level, BlockPos blockPos) {
    return EntitySearcher.findMinecarts().at(blockPos).upTo(SENSITIVITY).list(level);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      DetectorBlockEntity blockEntity) {
    blockEntity.tick++;
    if (blockEntity.powerDelay > 0) {
      blockEntity.powerDelay--;
      return;
    }
    if (blockEntity.updateInterval() == 0 ||
        blockEntity.tick % blockEntity.updateInterval() == 0) {
      int newPowerState = blockEntity.shouldTest()
          ? blockEntity.testCarts(getCarts(level, blockPos))
          : Redstone.SIGNAL_NONE;
      if (newPowerState != blockEntity.powerState) {
        blockEntity.powerState = newPowerState;
        var powered = blockEntity.powerState > Redstone.SIGNAL_NONE;
        if (powered) {
          blockEntity.powerDelay = CartConstants.DETECTED_POWER_OUTPUT_FADE;
        }
        level.setBlockAndUpdate(blockPos, blockState.setValue(DetectorBlock.POWERED, powered));
        var offsetPos = blockPos.offset(blockState.getValue(DetectorBlock.FACING).getUnitVec3i());
        level.updateNeighborsAt(offsetPos, blockState.getBlock());
      }
      blockEntity.tick = 0;
    }
  }

  public int getPowerState() {
    return this.powerState;
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    this.powerState = input.getIntOr(CompoundTagKeys.POWER_STATE, 0);
    this.powerDelay = input.getIntOr(CompoundTagKeys.POWER_DELAY, 0);
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.putInt(CompoundTagKeys.POWER_STATE, this.powerState);
    output.putInt(CompoundTagKeys.POWER_DELAY, this.powerDelay);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf out) {
    super.writeToBuf(out);
    out.writeVarInt(this.powerState);
    out.writeVarInt(this.powerDelay);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf in) {
    super.readFromBuf(in);
    this.powerState = in.readVarInt();
    this.powerDelay = in.readVarInt();
  }
}
