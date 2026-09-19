package mods.railcraft.util;

import java.util.Arrays;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil;

public class EnergyUtil {

  public static int pushToSides(Level level, BlockPos blockPos, EnergyHandler energyStorage,
      int pushPerSide, Predicate<BlockEntity> filter, Direction... sides) {
    return Arrays.stream(sides)
        .mapToInt(side -> pushToSide(level, blockPos, energyStorage, pushPerSide, side, filter))
        .sum();
  }

  private static int pushToSide(Level level, BlockPos blockPos, EnergyHandler energyStorage,
      int pushPerSide, Direction side, Predicate<BlockEntity> filter) {
    return LevelUtil.getBlockEntity(level, blockPos.relative(side))
        .filter(filter)
        .map(target -> level.getCapability(Capabilities.Energy.BLOCK,
            target.getBlockPos(), side.getOpposite()))
        .map(receiver -> EnergyHandlerUtil.move(energyStorage, receiver, pushPerSide, null))
        .orElse(0);
  }
}
