package mods.railcraft.util;

import mods.railcraft.api.core.Ownable;
import mods.railcraft.gui.buttons.IMultiButtonState;
import mods.railcraft.gui.buttons.MultiButtonController;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public interface ISecureObject<T extends IMultiButtonState> extends Ownable {

  MultiButtonController<T> getLockController();

  /*
   * Indicates if this object is locked
   */
  boolean isSecure();
}
