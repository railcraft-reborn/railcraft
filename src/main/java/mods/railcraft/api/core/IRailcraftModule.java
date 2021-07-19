/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.core;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Defines a Railcraft module. Any class implementing this interface and annotated by
 * {@link RailcraftModule} will be loaded as a Module by Railcraft during its initialization phase.
 *
 * Created by CovertJaguar on 4/5/2016.
 */
public interface IRailcraftModule {
  String CAT_CONFIG = "module.config";

  default void checkPrerequisites() throws MissingPrerequisiteException {}

  default void loadConfig(ForgeConfigSpec.Builder config) {}

  ModuleEventHandler getModuleEventHandler(boolean enabled);

  interface ModuleEventHandler {

    default void construction() {}

    default void preInit() {}

    default void init() {}

    default void postInit() {}
  }

  class MissingPrerequisiteException extends Exception {

    private static final long serialVersionUID = -7008761529910288809L;

    public MissingPrerequisiteException(String msg) {
      super(msg);
    }
  }
}
