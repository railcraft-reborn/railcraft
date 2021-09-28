package mods.railcraft.client.gui.widget.button;

import java.util.List;
import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.gui.button.ButtonState;
import mods.railcraft.gui.button.MultiButtonController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

/**
 * @author CovertJaguar <http://railcraft.info/wiki/info:license>
 */
public final class GuiMultiButton<T extends ButtonState> extends RailcraftButton {

  private final MultiButtonController<T> control;
  public boolean canChange = true;

  public GuiMultiButton(int x, int y, int width, int height, MultiButtonController<T> control,
      Button.IPressable actionListener, Button.ITooltip tooltip, ITexturePosition texture) {
    super(x, y, width, height, StringTextComponent.EMPTY, actionListener, tooltip, texture);
    this.control = control;
  }

  @Override
  public void onPress() {
    if (canChange && this.active) {
      super.onPress();
      control.incrementState();
      this.setMessage(control.getCurrentState().getLabel());
    }
  }

  public MultiButtonController<T> getController() {
    return control;
  }

  @Override
  public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_,
      float p_230431_4_) {
    super.renderButton(p_230431_1_, p_230431_2_, p_230431_3_, p_230431_4_);
    if (this.isHovered()) {
      this.renderToolTip(p_230431_1_, p_230431_2_, p_230431_3_);
    }
  }

  @Override
  public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
    List<? extends ITextProperties> tip = control.getCurrentState().getTooltip();
    if (tip == null) {
      super.renderToolTip(matrixStack, mouseX, mouseY);
      return;
    }
    Minecraft mc = Minecraft.getInstance();
    GuiUtils.drawHoveringText(matrixStack, tip, mouseX, mouseY, width, height, -1,
        mc.font);
  }
}
