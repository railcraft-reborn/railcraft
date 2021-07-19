package mods.railcraft.gui.buttons;

import java.util.List;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public enum LockButtonState implements IMultiButtonState {

  UNLOCKED(new ButtonTextureSet(224, 0, 16, 16)),
  LOCKED(new ButtonTextureSet(240, 0, 16, 16));

  public static final LockButtonState[] VALUES = values();
  private final IButtonTextureSet texture;

  LockButtonState(IButtonTextureSet texture) {
    this.texture = texture;
  }

  @Override
  public ITextComponent getLabel() {
    return new StringTextComponent("");
  }

  @Override
  public IButtonTextureSet getTextureSet() {
    return texture;
  }

  @Override
  public List<? extends ITextProperties> getTooltip() {
    return null;
  }
}
