/*------------------------------------------------------------------------------
 Copyright (c) Railcraft, 2011-2023

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.core;

public class ClientAccessException extends RuntimeException {

  private static final long serialVersionUID = 7824773890595326922L;

  public ClientAccessException() {
    super("This feature is not available on the client thread! It won't work!");
  }
}
