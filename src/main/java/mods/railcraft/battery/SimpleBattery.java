package mods.railcraft.battery;

import mods.railcraft.api.charge.ChargeStorage;

/**
 * Implementation of {@link mods.railcraft.api.charge.ChargeStorage}.
 *
 * <p>Created by CovertJaguar on 1/15/2019 for Railcraft.
 *
 * @author CovertJaguar (https://www.railcraft.info)
 */
public class SimpleBattery implements ChargeStorage {

  protected float charge;
  protected final float capacity;
  protected float chargeDrawnThisTick;

  public SimpleBattery(float capacity) {
    this(capacity, 0);
  }

  public SimpleBattery(float capacity, float charge) {
    this.capacity = capacity;
    this.charge = Math.max(0, Math.min(capacity, charge));
  }

  public float addCharge(float charge) {
    return this.addCharge(charge, false);
  }

  @Override
  public float addCharge(float charge, boolean simulate) {
    float chargedRecive = charge;
    if (!simulate) {
      this.charge += charge;
    }
    return chargedRecive;
  }

  @Override
  public void setCharge(float charge) {
    this.charge = charge;
  }

  public float removeCharge(float charge) {
    return this.removeCharge(charge, false);
  }

  @Override
  public float removeCharge(float charge, boolean simulate) {
    float amountToDraw = Math.min(charge, this.getCharge());
    if (!simulate) {
      this.charge -= amountToDraw / this.getEfficiency();
      this.chargeDrawnThisTick += amountToDraw;
    }

    return amountToDraw;
  }

  @Override
  public float getCharge() {
    return this.charge;
  }

  @Override
  public float getCapacity() {
    return this.capacity;
  }
}
