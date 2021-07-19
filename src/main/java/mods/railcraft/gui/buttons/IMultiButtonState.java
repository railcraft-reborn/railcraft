package mods.railcraft.gui.buttons;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IMultiButtonState {

  ITextComponent getLabel();

  String name();

  IButtonTextureSet getTextureSet();

  @Nullable
  List<? extends ITextProperties> getTooltip();
}
