/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signal;

import java.util.Objects;
import org.jetbrains.annotations.ApiStatus;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class SignalTools {

  public static boolean printSignalDebug;
  public static int aspectUpdateInterval = 4;

  private static TuningAuraProvider tuningAuraProvider;

  public static TuningAuraProvider tuningAuraProvider() {
    Objects.requireNonNull(tuningAuraProvider);
    return tuningAuraProvider;
  }

  @ApiStatus.Internal
  public static void _setTuningAuraProvider(TuningAuraProvider tuningAuraProvider) {
    if (SignalTools.tuningAuraProvider != null) {
      throw new IllegalStateException("tuningAuraProvider is already set.");
    }
    SignalTools.tuningAuraProvider = tuningAuraProvider;
  }
}
