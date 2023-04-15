package mods.railcraft.world.level.block.charge;

import mods.railcraft.api.charge.ChargeStorage;

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
}
