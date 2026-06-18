package mods.railcraft.client.gui.widget.button;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class RailcraftPageButton extends PageButton {

  private final boolean isForward;
  private final Identifier atlasLocation;

  public RailcraftPageButton(int x, int y, boolean isForward, Identifier atlasLocation,
      OnPress onPress) {
    super(x, y, isForward, onPress, true);
    this.isForward = isForward;
    this.atlasLocation = atlasLocation;
  }

  @Override
  public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
    int i = 0;
    int j = 192;
    if (this.isHoveredOrFocused()) {
      i += 23;
    }

    if (!this.isForward) {
      j += 13;
    }

    graphics.blit(RenderPipelines.GUI_TEXTURED, this.atlasLocation, this.getX(), this.getY(), i, j,
        23, 13, 256, 256);
  }
}
