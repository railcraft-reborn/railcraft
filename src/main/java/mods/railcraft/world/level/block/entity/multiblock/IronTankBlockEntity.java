package mods.railcraft.world.level.block.entity.multiblock;

import java.util.Collection;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.material.fluid.FluidTools;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class IronTankBlockEntity extends TankBlockEntity {

  private static final Collection<MultiblockPattern> PATTERNS =
      buildPatterns(RailcraftTags.Blocks.IRON_TANK_WALL,
          RailcraftTags.Blocks.IRON_TANK_GAUGE,
          RailcraftTags.Blocks.IRON_TANK_VALVE);

  public IronTankBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.IRON_TANK.get(), blockPos, blockState, PATTERNS);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      IronTankBlockEntity blockEntity) {
    blockEntity.serverTick();
    blockEntity.moduleDispatcher.serverTick();
  }

  @Override
  protected int getCapacityPerBlock() {
    return RailcraftConfig.server.tankCapacityPerBlock.get() * FluidTools.BUCKET_VOLUME;
  }
}
