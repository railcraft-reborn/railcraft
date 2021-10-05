package mods.railcraft.api.core;

import mods.railcraft.gui.button.ButtonState;
import mods.railcraft.gui.button.MultiButtonController;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public interface Secure<T extends ButtonState> extends Ownable {

  MultiButtonController<T> getLockController();

  /*
   * Indicates if this object is locked
   */
  boolean isSecure();
}
