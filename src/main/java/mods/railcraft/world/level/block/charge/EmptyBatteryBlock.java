package mods.railcraft.world.level.block.charge;

import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.integrations.jei.JeiSearchable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class EmptyBatteryBlock extends ChargeBlock implements JeiSearchable {

  private static final Map<Charge, Spec> CHARGE_SPECS =
      Spec.make(Charge.distribution, ConnectType.BLOCK, 0.4f);

  public EmptyBatteryBlock(Properties properties) {
    super(properties);
  }

  @Override
  public Map<Charge, Spec> getChargeSpecs(BlockState state, ServerLevel level, BlockPos pos) {
    return CHARGE_SPECS;
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level,
      List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(Component.translatable(Translations.Tips.CHARGE_NETWORK_EMPTY_BATTERY)
        .withStyle(ChatFormatting.BLUE));
  }

  @Override
  public Component addJeiInfo() {
    return Component.translatable(Translations.Jei.DISPOSABLE_BATTERY_EMPTY);
  }
}
