package mods.railcraft.client.gui.widget.button;

import java.util.List;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.gui.button.ButtonState;
import net.minecraft.network.chat.Component;

import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.components.Button.OnTooltip;

/**
 * @author CovertJaguar <https://railcraft.info/wiki/info:license>
 */
public final class MultiButton<T extends ButtonState<T>> extends RailcraftButton {

  private static final OnPress NO_PRESS = __ -> {
  };

  private final TooltipRenderer tooltipRenderer;
  private T state;
  private boolean locked;

  public MultiButton(int x, int y, int width, int height, T state,
      TooltipRenderer tooltipRenderer) {
    this(x, y, width, height, state, tooltipRenderer, NO_PRESS);
  }

  public MultiButton(int x, int y, int width, int height, T state, TooltipRenderer tooltipRenderer,
      OnPress actionListener) {
    super(x, y, width, height, state.getLabel(), actionListener, state.getTexturePosition());
    this.state = state;
    this.tooltipRenderer = tooltipRenderer;
  }

  public MultiButton(int x, int y, int width, int height, T state, TooltipRenderer tooltipRenderer,
      OnPress actionListener, OnTooltip tooltip) {
    super(x, y, width, height, state.getLabel(), actionListener, tooltip,
        state.getTexturePosition());
    this.state = state;
    this.tooltipRenderer = tooltipRenderer;
  }

  @Override
  public void onPress() {
    if (!this.locked && this.active) {
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
  public void renderButton(PoseStack poseStack, int mouseX, int mouseY,
      float partialTicks) {
    super.renderButton(poseStack, mouseX, mouseY, partialTicks);
    if (this.isHoveredOrFocused()) {
      this.renderToolTip(poseStack, mouseX, mouseY);
    }
  }

  @Override
  public void renderToolTip(PoseStack poseStack, int mouseX, int mouseY) {
    List<Component> tooltip = this.state.getTooltip();
    if (tooltip == null) {
      super.renderToolTip(poseStack, mouseX, mouseY);
      return;
    }
    this.tooltipRenderer.renderTooltip(poseStack, tooltip, mouseX, mouseY);
  }

  public interface TooltipRenderer {
    void renderTooltip(PoseStack poseStack, List<Component> tooltip, int x, int y);
  }
}
