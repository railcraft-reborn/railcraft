package mods.railcraft.util;

import java.util.Arrays;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Created by CovertJaguar on 5/12/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class EnergyUtil {

  public static final IEnergyStorage DUMMY_STORAGE = new IEnergyStorage() {
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
      return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
      return 0;
    }

    @Override
    public int getEnergyStored() {
      return 0;
    }

    @Override
    public int getMaxEnergyStored() {
      return 0;
    }

    @Override
    public boolean canExtract() {
      return false;
    }

    @Override
    public boolean canReceive() {
      return false;
    }
  };

  public static int pushToSide(BlockEntity blockEntity, Direction side, int powerToTransfer) {
    if (powerToTransfer <= 0) {
      return 0;
    }
    return blockEntity.getCapability(ForgeCapabilities.ENERGY, side)
        .filter(IEnergyStorage::canReceive)
        .map(storage -> storage.receiveEnergy(powerToTransfer, false))
        .orElse(0);
  }

  public static int pushToSides(Level level, BlockPos blockPos, IEnergyStorage energyStorage,
      int pushPerSide, Predicate<BlockEntity> filter, Direction... sides) {
    return Arrays.stream(sides)
        .mapToInt(side -> pushToSide(level, blockPos, energyStorage, pushPerSide, side, filter))
        .sum();
  }

  private static int pushToSide(Level level, BlockPos blockPos, IEnergyStorage energyStorage,
      int pushPerSide, Direction side, Predicate<BlockEntity> filter) {
    return LevelUtil.getBlockEntity(level, blockPos.relative(side))
        .filter(filter)
        .flatMap(target -> target.getCapability(ForgeCapabilities.ENERGY, side.getOpposite()).resolve())
        .filter(IEnergyStorage::canReceive)
        .map(receiver -> {
          int amountToPush = energyStorage.extractEnergy(pushPerSide, true);
          if (amountToPush > 0) {
            int amountPushed = receiver.receiveEnergy(amountToPush, false);
            energyStorage.extractEnergy(amountPushed, false);
            return amountPushed;
          }
          return 0;
        })
        .orElse(0);
  }
}
