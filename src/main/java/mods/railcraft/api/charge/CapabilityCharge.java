/*------------------------------------------------------------------------------
Copyright (c) CovertJaguar, 2011-2020

This work (the API) is licensed under the "MIT" License,
see LICENSE.md for details.
-----------------------------------------------------------------------------*/

package mods.railcraft.api.charge;

import mods.railcraft.battery.SimpleBattery;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
  * A Capability that can be attached to TE, Entity, and Itemstacks.
  * This has been generalized so that you can attatch it to basicaly anything,
  * instead of trains gobbling it up.
  *
  * <p>Created by CovertJaguar on 10/4/2016 for Railcraft.
  *
  * @author CovertJaguar (https://www.railcraft.info)
  */
public final class CapabilityCharge {

  @CapabilityInject(ChargeStorage.class)
  public static Capability<ChargeStorage> CART_BATTERY = null;

  public static void register() {
    CapabilityManager.INSTANCE.register(ChargeStorage.class,
        new IStorage<ChargeStorage>() {
          @Override
          public INBT writeNBT(Capability<ChargeStorage> capability, ChargeStorage instance,
              Direction side) {
            // todo: check if capacity is needed
            return instance.serializeNBT();
            // return IntNBT.valueOf(instance.getCharge());
          }

          @Override
          public void readNBT(Capability<ChargeStorage> capability, ChargeStorage instance,
              Direction side, INBT nbt) {
            if (!(instance instanceof SimpleBattery)) {
              throw new IllegalArgumentException(
                  "Can not deserialize to an instance that isn't the default implementation");
            }
            instance.deserializeNBT((CompoundNBT)nbt);
            // ((SimpleBattery)instance).setCharge(((IntNBT)nbt).getAsInt());
          }
        },
        () -> new SimpleBattery(1000));
  }
}
