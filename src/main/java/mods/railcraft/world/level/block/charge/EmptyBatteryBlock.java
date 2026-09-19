package mods.railcraft.world.level.block.charge;

import java.util.Map;
import mods.railcraft.Translations;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.integrations.jei.JeiSearchable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class EmptyBatteryBlock extends ChargeBlock implements JeiSearchable {

  public static final Map<Charge, Spec> CHARGE_SPECS =
      Spec.make(Charge.distribution, ConnectType.BLOCK, 0.4f);

  public EmptyBatteryBlock(Properties properties) {
    super(properties);
  }

  @Override
  public Map<Charge, Spec> getChargeSpecs(BlockState state, ServerLevel level, BlockPos pos) {
    return CHARGE_SPECS;
  }

  @Override
  public Component jeiDescription() {
    return Component.translatable(Translations.Jei.DISPOSABLE_BATTERY_EMPTY);
  }
}
