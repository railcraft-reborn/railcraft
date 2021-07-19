/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020
 http://railcraft.info
 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.util.EnumTools;
import mods.railcraft.util.inventory.InvTools;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.EndNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.ShortNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public final class NBTPlugin {

  public static @Nullable CompoundNBT makeGameProfileTag(@Nullable GameProfile profile) {
    if (profile == null || (profile.getName() == null && profile.getId() == null))
      return null;
    CompoundNBT nbt = new CompoundNBT();
    NBTUtil.writeGameProfile(nbt, new GameProfile(profile.getId(), profile.getName()));
    return nbt;
  }

  public static GameProfile readGameProfileTag(CompoundNBT data) {
    if (data.contains("Name")) {
      GameProfile ret = NBTUtil.readGameProfile(data);
      return ret == null ? new GameProfile(null, RailcraftConstantsAPI.UNKNOWN_PLAYER) : ret;
    }
    String ownerName = RailcraftConstantsAPI.UNKNOWN_PLAYER;
    if (data.contains("name"))
      ownerName = data.getString("name");
    UUID ownerUUID = null;
    if (data.contains("id"))
      ownerUUID = UUID.fromString(data.getString("id"));
    return new GameProfile(ownerUUID, ownerName);
  }

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

  public static void writeBlockPos(CompoundNBT data, String tag, BlockPos pos) {
    data.putIntArray(tag, new int[] {pos.getX(), pos.getY(), pos.getZ()});
  }

  public static @Nullable BlockPos readBlockPos(CompoundNBT data, String tag) {
    if (data.contains(tag)) {
      if (data.contains(tag, NBT.TAG_INT_ARRAY)) {
        int[] c = data.getIntArray(tag);
        return new BlockPos(c[0], c[1], c[2]);
      } else {
        return NBTUtil.readBlockPos(data.getCompound(tag));
      }
    }
    return null;
  }

  public static void writeItemStack(CompoundNBT data, String tag, @Nullable ItemStack stack) {
    CompoundNBT nbt = new CompoundNBT();
    if (!InvTools.isEmpty(stack))
      stack.save(nbt);
    data.put(tag, nbt);
  }

  public static ItemStack readItemStack(CompoundNBT data, String tag) {
    if (data.contains(tag)) {
      CompoundNBT nbt = data.getCompound(tag);
      return ItemStack.of(nbt);
    }
    return InvTools.emptyStack();
  }

  public enum EnumNBTType {

    END(EndNBT.class),
    BYTE(ByteNBT.class),
    SHORT(ShortNBT.class),
    INT(IntNBT.class),
    LONG(LongNBT.class),
    FLOAT(FloatNBT.class),
    DOUBLE(DoubleNBT.class),
    BYTE_ARRAY(ByteArrayNBT.class),
    STRING(StringNBT.class),
    LIST(ListNBT.class),
    COMPOUND(CompoundNBT.class),
    INT_ARRAY(IntArrayNBT.class);

    public static final EnumNBTType[] VALUES = values();
    private static final Map<Class<? extends INBT>, EnumNBTType> classToType = new HashMap<>();
    public final Class<? extends INBT> classObject;

    EnumNBTType(Class<? extends INBT> c) {
      this.classObject = c;
    }

    static {
      for (EnumNBTType each : VALUES) {
        classToType.put(each.classObject, each);
      }
    }

    public static EnumNBTType fromClass(Class<? extends INBT> c) {
      return classToType.get(c);
    }
  }
}
