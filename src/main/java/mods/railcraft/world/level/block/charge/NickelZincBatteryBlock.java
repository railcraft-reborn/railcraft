package mods.railcraft.world.level.block.charge;

import mods.railcraft.api.charge.ChargeStorage;

public class NickelZincBatteryBlock extends BatteryBlock {

  private static final Spec CHARGE_SPEC = new Spec(ConnectType.BLOCK, 0.2f,
      new ChargeStorage.Spec(ChargeStorage.State.RECHARGEABLE, 150000, 16, 0.7f));

  public NickelZincBatteryBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected Spec getChargeSpec() {
    return CHARGE_SPEC;
  }
}
