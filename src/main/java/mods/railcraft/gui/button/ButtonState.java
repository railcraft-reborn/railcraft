package mods.railcraft.gui.button;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface ButtonState extends IStringSerializable {

  ITextComponent getLabel();

  ButtonTextureSet getTextureSet();

  @Nullable
  default List<? extends ITextProperties> getTooltip() {
    return null;
  }
}
