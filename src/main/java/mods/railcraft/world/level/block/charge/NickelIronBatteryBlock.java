package mods.railcraft.world.level.block.charge;

import mods.railcraft.Translations;
import mods.railcraft.api.charge.ChargeStorage;
import net.minecraft.network.chat.Component;

public class NickelIronBatteryBlock extends BatteryBlock {

  private static final Spec CHARGE_SPEC = new Spec(ConnectType.BLOCK, 0.3f,
      new ChargeStorage.Spec(ChargeStorage.State.RECHARGEABLE, 100000, 32, 0.8f));

  public NickelIronBatteryBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected Spec getChargeSpec() {
    return CHARGE_SPEC;
  }

  @Override
  public Component addJeiInfo() {
    return Component.translatable(Translations.Jei.NICKEL_IRON_BATTERY);
  }
}
