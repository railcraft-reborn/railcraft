package mods.railcraft.world.level.block.charge;

import mods.railcraft.Translations;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.integrations.jei.JeiSearchable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public abstract class DisposableBatteryBlock extends BatteryBlock implements JeiSearchable {

  public DisposableBatteryBlock(Properties properties) {
    super(properties);
  }

  abstract RegistryObject<EmptyBatteryBlock> getBatteryBlockEmpty();

  @Override
  public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    super.tick(state, level, pos, random);
    Charge.distribution.network(level).access(pos).storage().ifPresent(storage -> {
      if (storage.getEnergyStored() <= 0)
        level.setBlockAndUpdate(pos, getBatteryBlockEmpty().get().defaultBlockState());
    });
  }

  @Override
  public Component jeiDescription() {
    return Component.translatable(Translations.Jei.DISPOSABLE_BATTERY);
  }
}
