/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signals;

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
  public static SignalPacketBuilder packetBuilder;

  public static boolean isInSameChunk(BlockPos a, BlockPos b) {
    return a.getX() >> 4 == b.getX() >> 4 && a.getZ() >> 4 == b.getZ() >> 4;
  }
}
