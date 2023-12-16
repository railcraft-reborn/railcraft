package mods.railcraft.world.level.block.steamboiler;

import com.mojang.serialization.MapCodec;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.steamboiler.FluidFueledSteamBoilerBlockEntity;
import mods.railcraft.world.level.block.entity.steamboiler.SteamBoilerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FluidFueledFireboxBlock extends FireboxBlock {

  private static final MapCodec<FluidFueledFireboxBlock> CODEC =
      simpleCodec(FluidFueledFireboxBlock::new);

  public FluidFueledFireboxBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends FireboxBlock> codec() {
    return CODEC;
  }

  @Override
  public SteamBoilerBlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new FluidFueledSteamBoilerBlockEntity(blockPos, blockState);
  }

  @Override
  protected BlockEntityType<? extends SteamBoilerBlockEntity> getBlockEntityType() {
    return RailcraftBlockEntityTypes.FLUID_FUELED_STEAM_BOILER.get();
  }
}
