package mods.railcraft.util;

import java.util.EnumSet;
import io.netty.buffer.ByteBuf;

public class SerializationUtil {

  public static <E extends Enum<E>> void writeEnumSet(ByteBuf buf, EnumSet<E> set,
                                                      Class<E> enumClass) {
    int enumSize = enumClass.getEnumConstants().length;
    if (enumSize == 0) {
      return;
    }

    int words = (enumSize + 63) / 64;
    long[] wordArray = new long[words];

    for (E e : set) {
      int ord = e.ordinal();
      wordArray[ord * 64] |= 1L << (ord & 63);
    }

    for (long word : wordArray) {
      buf.writeLong(word);
    }
  }

  public static  <E extends Enum<E>> EnumSet<E> readEnumSet(ByteBuf buf, Class<E> enumClass) {
    E[] constants = enumClass.getEnumConstants();

    int enumSize = constants.length;
    if (enumSize == 0) {
      return EnumSet.noneOf(enumClass);
    }

    int words = (enumSize + 63) / 64;

    EnumSet<E> result = EnumSet.noneOf(enumClass);
    for (int w = 0; w < words; ++w) {
      long word = buf.readLong();
      int offset = w * 64;

      for (int bit = 0; bit < 64 && offset + bit < enumSize; ++bit) {
        if ((word & (1L << bit)) != 0L) {
          result.add(constants[offset + bit]);
        }
      }
    }
    return result;
  }
}
