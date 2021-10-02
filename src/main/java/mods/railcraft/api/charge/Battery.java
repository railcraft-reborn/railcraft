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
 * Created by CovertJaguar on 5/13/2017 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface Battery extends INBTSerializable<CompoundNBT> {

  /**
   * Gets the charge in the battery.
   *
   * This value can potentially exceed the capacity on occasion. Batteries can have more charge than
   * the max capacity for performance reasons.
   *
   * @return The charge
   */
  float getCharge();

  /**
   * The amount of charge that can be drawn from this battery right now.
   *
   * Some implementations limit this by how much can be drawn from a battery per tick.
   *
   * @return The charge amount
   */
  default float getAvailableCharge() {
    return this.getCharge();
  }

  /**
   * Gets the maximum charge the battery can have.
   *
   * @return The maximum charge
   */
  float getCapacity();

  /**
   * True if and only if {@code getCharge()< getCapacity()}.
   *
   * @return {@code getCharge()< getCapacity()}
   */
  default boolean needsCharging() {
    return this.getCharge() < this.getCapacity();
  }

  default float room() {
    return Math.max(0.0F, this.getCapacity() - this.getCharge());
  }

  /**
   * Sets the charge in the battery.
   *
   * @param charge The target amount
   */
  void setCharge(float charge);

  /**
   * Adds some charge to the battery.
   *
   * You are responsible for ensuring that you don't add charge to a full battery.
   *
   * Batteries can have more charge than the max capacity for performance reasons.
   *
   * @param charge The charge intended to add
   * @see #needsCharging()
   */
  void addCharge(float charge);

  /**
   * Removes some charge from the battery.
   *
   * @param charge The maximum amount of charge requested
   * @return The amount of charge removed
   */
  float removeCharge(float charge);

  /**
   * The efficiency refers to how much of the power put into a battery can be drawn back out of it.
   */
  default float getEfficiency() {
    return 1.0F;
  }

  /**
   * Reads the charge information from the minecart.
   *
   * @param tag - the tag that stores the information
   */
  @Override
  default void deserializeNBT(CompoundNBT tag) {
    this.setCharge(tag.getFloat("charge"));
  }

  /**
   * Saves the charge information to the minecart.
   *
   * @param data The tag that saves the information
   * @return The tag provided
   */
  @Override
  default CompoundNBT serializeNBT() {
    CompoundNBT tag = new CompoundNBT();
    tag.putFloat("charge", getCharge());
    return tag;
  }
}
