/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.core;

import java.util.Arrays;

import static mods.railcraft.api.core.RailcraftCore.InitStage.LOADING;

/**
 * Created by CovertJaguar on 8/23/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class RailcraftCore {

  public enum InitStage {
    LOADING,
    DEPENDENCY_CHECKING,
    CONSTRUCTION,
    PRE_INIT,
    INIT,
    POST_INIT,
    FINISHED
  }

  public static InitStage getInitStage() {
    return initStage;
  }

  public static void setInitStage(String stage) {
    initStage = InitStage.valueOf(stage);
  }

  public static void validateStage(InitStage... validStages) {
    if (!Arrays.asList(validStages).contains(initStage))
      throw new InvalidStageException(initStage);
  }

  public static class InvalidStageException extends RuntimeException {

    private static final long serialVersionUID = -7730051761649044067L;

    public InvalidStageException(InitStage stage) {
      super("This operation cannot be performed during the " + stage + " stage.");
    }
  }

  private static InitStage initStage = LOADING;

  private RailcraftCore() {}
}
