/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020
 http://railcraft.info
 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mods.railcraft.util.EnumTools;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.common.util.Constants;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public final class NBTPlugin {

  public static final int UUID_TAG_TYPE = Constants.NBT.TAG_INT_ARRAY;

  public static <T extends Enum<T>> void writeEnumOrdinal(CompoundNBT data, String tag,
      Enum<T> e) {
    assert e.ordinal() < Byte.MAX_VALUE;
    data.putByte(tag, (byte) e.ordinal());
  }

  public static <T extends Enum<T>> T readEnumOrdinal(CompoundNBT data, String tag,
      T[] enumConstants, T defaultValue) {
    if (data.contains(tag)) {
      byte ordinal = data.getByte(tag);
      return enumConstants[ordinal];
    }
    return defaultValue;
  }

  public static <T extends Enum<T>> void incrementEnumOrdinal(CompoundNBT data, String tag,
      T[] enumConstants, T defaultValue) {
    assert enumConstants.length < Byte.MAX_VALUE;

    writeEnumOrdinal(data, tag,
        EnumTools.next(readEnumOrdinal(data, tag, enumConstants, defaultValue), enumConstants));
  }

  public static <T extends Enum<T>> void writeEnumName(CompoundNBT data, String tag, Enum<T> e) {
    data.putString(tag, e.name());
  }

  public static <T extends Enum<T>> T readEnumName(CompoundNBT data, String tag,
      T defaultValue) {
    if (data.contains(tag)) {
      String name = data.getString(tag);
      return Enum.valueOf(defaultValue.getClass().asSubclass(Enum.class), name);
    }
    return defaultValue;
  }

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
}
