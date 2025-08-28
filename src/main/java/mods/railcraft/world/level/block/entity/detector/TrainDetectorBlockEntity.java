package mods.railcraft.world.level.block.entity.detector;

import java.util.List;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;

public class TrainDetectorBlockEntity extends DetectorBlockEntity {

  private int trainSize = 5;

  public TrainDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.TRAIN_DETECTOR.get(), blockPos, blockState);
  }

  @Override
  protected int testCarts(List<AbstractMinecart> minecarts) {
    if (minecarts.stream()
        .mapToInt(cart -> RollingStock.getOrThrow(cart).train().size())
        .anyMatch(count -> count >= this.getTrainSize())) {
      return Redstone.SIGNAL_MAX;
    }
    return Redstone.SIGNAL_NONE;
  }

  @Override
  protected int updateInterval() {
    return 4;
  }

  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    this.trainSize = tag.getInt(CompoundTagKeys.TRAIN_SIZE).orElse(5);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.putInt(CompoundTagKeys.TRAIN_SIZE, this.trainSize);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf out) {
    super.writeToBuf(out);
    out.writeVarInt(this.trainSize);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf in) {
    super.readFromBuf(in);
    this.trainSize = in.readVarInt();
  }

  public int getTrainSize() {
    return this.trainSize;
  }

  public void setTrainSize(int trainSize) {
    this.trainSize = trainSize;
  }
}
