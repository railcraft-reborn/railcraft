/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.crafting;

import mods.railcraft.api.core.RailcraftCore;

/**
 * These variables are defined during the pre-init phase. Do not attempt to access them during
 * pre-init.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class Crafters {

  static ICokeOvenCrafter cokeOven = new ICokeOvenCrafter() {};
  private static IBlastFurnaceCrafter blastFurnace = new IBlastFurnaceCrafter() {};
  static IRockCrusherCrafter rockCrusher = new IRockCrusherCrafter() {};

  private static void validateStage() {
    RailcraftCore.validateStage(RailcraftCore.InitStage.PRE_INIT, RailcraftCore.InitStage.INIT,
        RailcraftCore.InitStage.POST_INIT, RailcraftCore.InitStage.FINISHED);
  }

  /**
   * Returns the coke oven crafting manager.
   *
   * <p>
   * If railcraft is not available, a dummy one is returned.
   * </p>
   *
   * @return The coke oven crafting manager
   */
  public static ICokeOvenCrafter cokeOven() {
    validateStage();
    return cokeOven;
  }

  /**
   * Returns the blast furnace crafting manager.
   *
   * <p>
   * If railcraft is not available, a dummy one is returned.
   * </p>
   *
   * @return The blast furnace crafting manager
   */
  public static IBlastFurnaceCrafter blastFurnace() {
    validateStage();
    return blastFurnace;
  }

  /**
   * Returns the rock crusher crafting manager.
   *
   * <p>
   * If railcraft is not available, a dummy one is returned.
   * </p>
   *
   * @return The rock crusher crafting manager
   */
  public static IRockCrusherCrafter rockCrusher() {
    validateStage();
    return rockCrusher;
  }

  private Crafters() {}
}
