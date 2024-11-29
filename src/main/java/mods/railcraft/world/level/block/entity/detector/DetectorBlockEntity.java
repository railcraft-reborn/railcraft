package mods.railcraft.world.level.block.entity.detector;

import java.util.List;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.world.entity.vehicle.CartConstants;
import mods.railcraft.world.level.block.detector.DetectorBlock;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;

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
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    this.powerState = tag.getInt(CompoundTagKeys.POWER_STATE);
    this.powerDelay = tag.getInt(CompoundTagKeys.POWER_DELAY);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.putInt(CompoundTagKeys.POWER_STATE, this.powerState);
    tag.putInt(CompoundTagKeys.POWER_DELAY, this.powerDelay);
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
