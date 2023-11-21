package mods.railcraft.world.level.block.charge;

import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.world.level.block.RailcraftBlocks;

public class ZincCarbonBatteryBlock extends DisposableBatteryBlock {

  private static final Spec CHARGE_SPEC = new Spec(ConnectType.BLOCK, 0.01f,
      new ChargeStorage.Spec(ChargeStorage.State.DISPOSABLE, 75000, 8, 0.6f));

  public ZincCarbonBatteryBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected EmptyBatteryBlock emptyBlock() {
    return RailcraftBlocks.ZINC_CARBON_BATTERY_EMPTY.get();
  }

  @Override
  protected Spec getChargeSpec() {
    return CHARGE_SPEC;
  }
}
