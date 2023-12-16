package mods.railcraft.world.level.block.steamboiler;

import com.mojang.serialization.MapCodec;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.steamboiler.SolidFueledSteamBoilerBlockEntity;
import mods.railcraft.world.level.block.entity.steamboiler.SteamBoilerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SolidFueledFireboxBlock extends FireboxBlock {

  private static final MapCodec<SolidFueledFireboxBlock> CODEC =
      simpleCodec(SolidFueledFireboxBlock::new);

  public SolidFueledFireboxBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends FireboxBlock> codec() {
    return CODEC;
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
