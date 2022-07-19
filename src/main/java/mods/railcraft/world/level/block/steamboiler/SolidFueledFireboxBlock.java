package mods.railcraft.world.level.block.steamboiler;

import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.steamboiler.SolidFueledSteamBoilerBlockEntity;
import mods.railcraft.world.level.block.entity.steamboiler.SteamBoilerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SolidFueledFireboxBlock extends FireboxBlock {

  public SolidFueledFireboxBlock(Properties properties) {
    super(properties);
  }

  @Override
  public SteamBoilerBlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new SolidFueledSteamBoilerBlockEntity(blockPos, blockState);
  }

  @Override
  protected BlockEntityType<? extends SteamBoilerBlockEntity> getBlockEntityType() {
    return RailcraftBlockEntityTypes.SOLID_FUELED_STEAM_BOILER.get();
  }
}
