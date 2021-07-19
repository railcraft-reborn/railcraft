/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signals;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class SignalTools {

  public static boolean printSignalDebug;
  public static int signalUpdateInterval = 4;

  @OnlyIn(Dist.CLIENT)
  public static ILinkEffectRenderer effectManager;
  public static ISignalPacketBuilder packetBuilder;

  public static void writeToNBT(CompoundNBT data, String tag, BlockPos pos) {
    data.putIntArray(tag, new int[] {pos.getX(), pos.getY(), pos.getZ()});
  }

  public static @Nullable BlockPos readFromNBT(CompoundNBT data, String key) {
    if (data.contains(key)) {
      int[] c = data.getIntArray(key);
      return new BlockPos(c[0], c[1], c[2]);
    }
    return null;
  }

  public static boolean isInSameChunk(BlockPos a, BlockPos b) {
    return a.getX() >> 4 == b.getX() >> 4 && a.getZ() >> 4 == b.getZ() >> 4;
  }
}
