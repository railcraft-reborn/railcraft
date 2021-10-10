package mods.railcraft.client.gui.widget.button;

import java.util.List;
import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.gui.button.ButtonState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.fml.client.gui.GuiUtils;

/**
 * @author CovertJaguar <https://railcraft.info/wiki/info:license>
 */
public final class MultiButton<T extends ButtonState<T>> extends RailcraftButton {

  private T state;
  public boolean canChange = true;

  public MultiButton(int x, int y, int width, int height, T state) {
    this(x, y, width, height, state, __ -> {
    });
  }

  public MultiButton(int x, int y, int width, int height, T state,
      IPressable actionListener) {
    super(x, y, width, height, state.getLabel(), actionListener, state.getTexturePosition());
    this.state = state;
  }

  public MultiButton(int x, int y, int width, int height, T state,
      IPressable actionListener, ITooltip tooltip) {
    super(x, y, width, height, state.getLabel(), actionListener, tooltip,
        state.getTexturePosition());
    this.state = state;
  }

  @Override
  public void onPress() {
    if (this.canChange && this.active) {
      this.setState(this.state.getNext());
      super.onPress();
    }
  }

  public T getState() {
    return this.state;
  }

  public void setState(T state) {
    if (this.state != state) {
      this.state = this.state.getNext();
      this.setTexturePosition(this.state.getTexturePosition());
      this.setMessage(this.state.getLabel());
    }
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
    List<? extends ITextProperties> tooltip = this.state.getTooltip();
    if (tooltip == null) {
      super.renderToolTip(matrixStack, mouseX, mouseY);
      return;
    }
    Minecraft minecraft = Minecraft.getInstance();
    GuiUtils.drawHoveringText(matrixStack, tooltip, mouseX, mouseY, minecraft.screen.width,
        minecraft.screen.height, -1, minecraft.font);
  }
}
