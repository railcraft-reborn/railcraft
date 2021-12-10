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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * @author CovertJaguar (https://www.railcraft.info/)
 */
public final class RailcraftNBTUtil {

  public static final int UUID_TAG_TYPE = Tag.TAG_INT_ARRAY;

  public static ListTag createUUIDArray(Iterable<UUID> uuids) {
    ListTag tag = new ListTag();
    for (UUID uuid : uuids) {
      tag.add(NbtUtils.createUUID(uuid));
    }
    return tag;
  }

  public static List<UUID> loadUUIDArray(ListTag listTag) {
    List<UUID> list = new ArrayList<>();
    for (Tag tag : listTag) {
      list.add(NbtUtils.loadUUID(tag));
    }
    return list;
  }

  /**
   * Loads energy data from a CompoundNBT.
   */
  public static void loadEnergyCell(CompoundTag loadData, IEnergyStorage storageMedium) {
    if (!(storageMedium instanceof EnergyStorage)) {
      throw new IllegalArgumentException(
          "Can not deserialize to an instance that isn't the default implementation");
    }
    ((EnergyStorage)storageMedium).receiveEnergy(loadData.getInt("energy"), false);
  }

  /**
   * Saves energy data from a CompoundNBT.
   */
  public static CompoundTag saveEnergyCell(IEnergyStorage storageMedium) {
    CompoundTag datOut = new CompoundTag();
    datOut.putInt("energy", storageMedium.getEnergyStored());
    return datOut;
  }
}
