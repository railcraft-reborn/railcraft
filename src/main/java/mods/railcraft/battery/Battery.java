package mods.railcraft.battery;

import mods.railcraft.api.charge.IBattery;

/**
 * Created by CovertJaguar on 1/15/2019 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class Battery implements IBattery {

  protected final float capacity;
  protected float charge;
  protected float chargeDrawnThisTick;
  // protected float chargeAddedThisTick;

  public Battery(float capacity) {
    this.capacity = capacity;
  }

  @Override
  public float getCharge() {
    return charge;
  }

  @Override
  public void setCharge(float charge) {
    this.charge = charge;
  }

  @Override
  public float getCapacity() {
    return capacity;
  }

  @Override
  public void addCharge(float charge) {
    this.charge += charge;
    // chargeAddedThisTick += charge;
  }

  /**
   * Remove up to the requested amount of charge and returns the amount removed.
   * <p/>
   *
   * @return charge removed
   */
  @Override
  public float removeCharge(float request) {
    float amountToDraw = Math.min(request, getAvailableCharge());
    charge -= amountToDraw / getEfficiency();
    chargeDrawnThisTick += amountToDraw;
    return amountToDraw;
  }
}
