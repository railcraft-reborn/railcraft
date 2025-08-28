package mods.railcraft.world.level.block.charge;

import mods.railcraft.Translations;
import net.minecraft.network.chat.Component;

public class NickelZincBatteryBlock extends BatteryBlock {

  private static final Spec CHARGE_SPEC = BatterySpecs.NICKEL_ZINC.buildSpec();

  public NickelZincBatteryBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected Spec getChargeSpec() {
    return CHARGE_SPEC;
  }

  @Override
  public Component jeiDescription() {
    return Component.translatable(Translations.Jei.NICKEL_ZINC_BATTERY);
  }
}
