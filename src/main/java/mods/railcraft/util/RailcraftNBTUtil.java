/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020
 https://railcraft.info
 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at https://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/

package mods.railcraft.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * @author CovertJaguar (https://www.railcraft.info/)
 */
public final class RailcraftNBTUtil {

  public static final int UUID_TAG_TYPE = Constants.NBT.TAG_INT_ARRAY;

  public static ListNBT createUUIDArray(Iterable<UUID> uuids) {
    ListNBT tag = new ListNBT();
    for (UUID uuid : uuids) {
      tag.add(NBTUtil.createUUID(uuid));
    }
    return tag;
  }

  public static List<UUID> loadUUIDArray(ListNBT listTag) {
    List<UUID> list = new ArrayList<>();
    for (INBT tag : listTag) {
      list.add(NBTUtil.loadUUID(tag));
    }
    return list;
  }

  /**
   * Loads energy data from a CompoundNBT.
   */
  public static void loadEnergyCell(CompoundNBT loadData, IEnergyStorage storageMedium) {
    if (!(storageMedium instanceof EnergyStorage)) {
      throw new IllegalArgumentException(
          "Can not deserialize to an instance that isn't the default implementation");
    }
    ((EnergyStorage)storageMedium).receiveEnergy(loadData.getInt("energy"), false);
  }

  /**
   * Saves energy data from a CompoundNBT.
   */
  public static CompoundNBT saveEnergyCell(IEnergyStorage storageMedium) {
    CompoundNBT datOut = new CompoundNBT();
    datOut.putInt("energy", storageMedium.getEnergyStored());
    return datOut;
  }

}
