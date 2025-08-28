package mods.railcraft.world.level.block.charge;

import mods.railcraft.api.charge.ChargeStorage;

public enum BatterySpecs {

  NICKEL_ZINC(0.2f, new ChargeStorage.Spec(ChargeStorage.State.RECHARGEABLE, 150_000, 16, 0.7f)),
  NICKEL_IRON(0.3f, new ChargeStorage.Spec(ChargeStorage.State.RECHARGEABLE, 100_000, 32, 0.8f)),
  ZINC_CARBON(0.01f, new ChargeStorage.Spec(ChargeStorage.State.DISPOSABLE, 75_000, 8, 0.6f)),
  ZINC_SILVER(0, new ChargeStorage.Spec(ChargeStorage.State.DISPOSABLE, 200_000, 40, 1)),
  ;

  private final float losses;
  private final ChargeStorage.Spec spec;

  BatterySpecs(float losses, ChargeStorage.Spec spec) {
    this.losses = losses;
    this.spec = spec;
  }

  public float getLosses() {
    return losses;
  }

  public ChargeStorage.Spec getSpec() {
    return spec;
  }

  public ChargeBlock.Spec buildSpec() {
    return new ChargeBlock.Spec(ChargeBlock.ConnectType.BLOCK, losses, spec);
  }
}
