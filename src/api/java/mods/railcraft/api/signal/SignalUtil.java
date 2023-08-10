/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signal;

import java.util.Objects;
import org.jetbrains.annotations.ApiStatus;
import com.google.common.base.Preconditions;

public final class SignalUtil {

  public static int aspectUpdateInterval = 4;

  private static TuningAuraHandler tuningAuraHandler;

  private SignalUtil() {}

  public static TuningAuraHandler tuningAuraHandler() {
    Objects.requireNonNull(tuningAuraHandler);
    return tuningAuraHandler;
  }

  @ApiStatus.Internal
  public static void _setTuningAuraHandler(TuningAuraHandler tuningAuraHandler) {
    Preconditions.checkState(SignalUtil.tuningAuraHandler == null,
        "tuningAuraHandler is already set.");
    SignalUtil.tuningAuraHandler = tuningAuraHandler;
  }
}
