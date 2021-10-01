package mods.railcraft.battery;

import mods.railcraft.api.charge.Battery;

/**
 * Created by CovertJaguar on 1/15/2019 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class SimpleBattery implements Battery {

  protected final float capacity;
  protected float charge;
  protected float chargeDrawnThisTick;

  public SimpleBattery(float capacity) {
    this.capacity = capacity;
  }

  @Override
  public float getCharge() {
    return this.charge;
  }

  @Override
  public void setCharge(float charge) {
    this.charge = charge;
  }

  @Override
  public float getCapacity() {
    return this.capacity;
  }

  @Override
  public void addCharge(float charge) {
    this.charge += charge;
  }

  /**
   * Remove up to the requested amount of charge and returns the amount removed.
   * <p/>
   *
   * @return charge removed
   */
  @Override
  public float removeCharge(float request) {
    float amountToDraw = Math.min(request, this.getAvailableCharge());
    this.charge -= amountToDraw / this.getEfficiency();
    this.chargeDrawnThisTick += amountToDraw;
    return amountToDraw;
  }
}
