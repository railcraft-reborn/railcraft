/*------------------------------------------------------------------------------
Copyright (c) CovertJaguar, 2011-2020

This work (the API) is licensed under the "MIT" License,
see LICENSE.md for details.
-----------------------------------------------------------------------------*/

package mods.railcraft.api.carts;

/**
* This interface is implemented by the Energy Cart
* and is used by the Energy Loaders to charge/discharge carts.
* It is roughly equivalent to the IItemTransfer interface
* and based on ElectricItem and IElectricItem.
*
* <p>This interface has been superseded by the CapabilityCartCharge
* interface for general use. It remains in use solely for the
* IC2 based Energy Loaders.
*
* @author CovertJaguar (https://www.railcraft.info)
*/
public interface IEnergyTransfer {

  /**
  * Injects the specified amount of FE into the device.
  *
  * <p>The function returns the remainder of the FE after
  * any FE used is subtracted.
  *
  * @param source Object initiating the transfer, should be an Entity or Tile Entity
  * @param amount Amount of energy to transfer in FE
  * @param tier Tier of the source device, has to be at least as high as the target device
  * @param ignoreTransferLimit Ignore the transfer limit specified by getTransferLimit()
  * @param simulate Don't actually change the item, just determine the return value
  * @return The amount of FE not used
  */
  double injectEnergy(Object source, double amount, int tier,
      boolean ignoreTransferLimit, boolean simulate, boolean passAlong);

  /**
  * Requests a certain amount of FE from the device.
  *
  * <p>The is function will subtract FE from the device's store of power
  * and return a portion up to, but not exceeding, the amount of FE requested.
  *
  * @param source Object initiating the transfer, should be an Entity or Tile Entity
  * @param amount Amount of energy to transfer in FE
  * @param tier Tier of the source device, has to be at least as high as the target device
  * @param ignoreTransferLimit Ignore the transfer limit specified by getTransferLimit()
  * @param simulate Don't actually change the item, just determine the return value
  * @param passAlong Whether neighboring carts should be asked to provide any missing power.
  * @return The amount of FE transferred
  */
  double extractEnergy(Object source, double amount, int tier,
      boolean ignoreTransferLimit, boolean simulate, boolean passAlong);

  /**
  * Return true if energy can be injected into this device.
  *
  * @return true if can inject energy
  */
  boolean canInjectEnergy();

  /**
  * Return true if energy can be extracted from this device.
  *
  * @return true if can extract energy
  */
  boolean canExtractEnergy();

  /**
  * The max capacity of the device.
  *
  * @return max capacity
  */
  int getCapacity();

  /**
  * Returns the current energy contained in the device.
  *
  * @return current energy
  */
  double getEnergy();

  /**
  * The device's transfer rate in FE/t.
  *
  * @return the transfer rate
  */
  int getTransferLimit();

}
