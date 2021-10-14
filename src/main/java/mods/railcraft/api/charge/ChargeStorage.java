/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.charge;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Base interface for charge batteries.
 *
 * <p><h2>IMPORTANT CHANGE: WE DO NOT USE FLOATS ANYMORE FOR ENERGY UNITS!</h2>
 *
 * <p>Created by CovertJaguar on 5/13/2017 for Railcraft.
 *
 * <p>Modifications took from {@link net.minecraftforge.energy.IEnergyStorage IEnergyStorage}
 *
 * @author CovertJaguar (https://www.railcraft.info)
 */
public interface ChargeStorage extends INBTSerializable<CompoundNBT> {

  /**
   * Sets the charge in the battery.
   * Use the {@link ChargeStorage#addCharge} or {@link ChargeStorage#addCharge} instead
   *
   * @param charge The target amount
   */
  void setCharge(int charge);

  /**
   * Adds charge to the battery. Returns quantity of charge that was accepted.
   *
   * <p>Batteries can have more charge than the max capacity for performance reasons.
   *
   * @param charge The charge intended to add
   * @param simulate If TRUE, the insertion will only be simulated.
   *
   * @return Amount of energy that was (or would have been, if simulated) accepted by the storage.
   * @see #needsCharging()
   */
  public int addCharge(int charge, boolean simulate);

  /**
   * Removes some charge from the battery.
   *
   * @param charge The maximum amount of charge requested
   * @param simulate If TRUE, the insertion will only be simulated.
   * @return
   *  Amount of energy that was (or would have been, if simulated) extracted from the storage.
   */
  public int removeCharge(int charge, boolean simulate);

  /**
   * Gets the amount of charge in the battery.
   *
   * <p>This value can potentially exceed the capacity on occasion.
   * Batteries can have more charge than the max capacity for performance reasons.
   */
  public int getCharge();

  /**
   * Gets the maximum amount of charge the battery can have.
   */
  public int getCapacity();

  /**
   * True if and only if {@code getCharge()< getCapacity()}.
   *
   * @return {@code getCharge()< getCapacity()}
   */
  default boolean needsCharging() {
    return this.getCharge() < this.getCapacity();
  }

  /**
   * Returns the maximum amount of charge you can add before the battery is full.
   */
  default int room() {
    return Math.max(0, this.getCapacity() - this.getCharge());
  }

  /**
   * The efficiency refers to how much of the power put into a battery can be drawn back out of it.
   */
  default float getEfficiency() {
    return 1.0F;
  }

  /**
   * Saves the charge information to the minecart.
   *
   * @return The tag provided
   */
  @Override
  default CompoundNBT serializeNBT() {
    CompoundNBT tag = new CompoundNBT();
    tag.putInt("charge", getCharge());
    return tag;
  }

  /**
   * Reads the charge information from the battery.
   *
   * @param tag - the tag that stores the information
   */
  @Override
  default void deserializeNBT(CompoundNBT tag) {
    this.setCharge(tag.getInt("charge"));
  }

}
