package mods.railcraft.client.gui.widget.button;

import java.util.List;
import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.gui.button.ButtonState;
import mods.railcraft.gui.button.MultiButtonController;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.fml.client.gui.GuiUtils;

/**
 * @author CovertJaguar <https://railcraft.info/wiki/info:license>
 */
public final class MultiButton<T extends ButtonState> extends RailcraftButton {

  private final MultiButtonController<T> controller;
  public boolean canChange = true;

  public MultiButton(int x, int y, int width, int height, MultiButtonController<T> controller) {
    this(x, y, width, height, controller, __ -> {
    });
  }

  public MultiButton(int x, int y, int width, int height, MultiButtonController<T> controller,
      IPressable actionListener) {
    super(x, y, width, height, controller.getCurrentState().getLabel(), actionListener,
        controller.getCurrentState().getTexturePosition());
    this.controller = controller;
  }

  public MultiButton(int x, int y, int width, int height, MultiButtonController<T> controller,
      IPressable actionListener, ITooltip tooltip) {
    super(x, y, width, height, controller.getCurrentState().getLabel(), actionListener, tooltip,
        controller.getCurrentState().getTexturePosition());
    this.controller = controller;
  }

  @Override
  public void onPress() {
    if (this.canChange && this.active) {
      this.controller.incrementState();
      this.setTexturePosition(this.controller.getCurrentState().getTexturePosition());
      this.setMessage(this.controller.getCurrentState().getLabel());
      super.onPress();
    }
  }

  public MultiButtonController<T> getController() {
    return this.controller;
  }

  @Override
  public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
    if (this.isHovered()) {
      this.renderToolTip(matrixStack, mouseX, mouseY);
    }
  }

  @Override
  public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
    List<? extends ITextProperties> tooltip = this.controller.getCurrentState().getTooltip();
    if (tooltip == null) {
      super.renderToolTip(matrixStack, mouseX, mouseY);
      return;
    }
    Minecraft minecraft = Minecraft.getInstance();
    GuiUtils.drawHoveringText(matrixStack, tooltip, mouseX, mouseY, minecraft.screen.width,
        minecraft.screen.height, -1, minecraft.font);
  }
}
