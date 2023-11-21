package mods.railcraft.world.level.block.charge;

import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.world.level.block.RailcraftBlocks;

public class ZincSilverBatteryBlock extends DisposableBatteryBlock {

  private static final Spec CHARGE_SPEC = new Spec(ConnectType.BLOCK, 0,
      new ChargeStorage.Spec(ChargeStorage.State.DISPOSABLE, 200000, 40, 1));

  public ZincSilverBatteryBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected EmptyBatteryBlock emptyBlock() {
    return RailcraftBlocks.ZINC_SILVER_BATTERY_EMPTY.get();
  }

  @Override
  protected Spec getChargeSpec() {
    return CHARGE_SPEC;
  }
}
