package mods.railcraft.world.level.block.entity.tank;

import java.util.Collection;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidType;

public class SteelTankBlockEntity extends TankBlockEntity {

  private static final Collection<MultiblockPattern<Void>> PATTERNS =
      buildPatterns(RailcraftTags.Blocks.STEEL_TANK_WALL,
          RailcraftTags.Blocks.STEEL_TANK_GAUGE,
          RailcraftTags.Blocks.STEEL_TANK_VALVE);

  public SteelTankBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.STEEL_TANK.get(), blockPos, blockState, PATTERNS);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      SteelTankBlockEntity blockEntity) {
    blockEntity.serverTick();
    blockEntity.moduleDispatcher.serverTick();
  }

  @Override
  protected int getCapacityPerBlock() {
    return 2 * RailcraftConfig.server.tankCapacityPerBlock.get() * FluidType.BUCKET_VOLUME;
  }
}
