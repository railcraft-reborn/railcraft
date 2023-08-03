package mods.railcraft.client.gui.widget.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.resources.ResourceLocation;

public class RailcraftPageButton extends PageButton {

  private final boolean isForward;
  private final ResourceLocation atlasLocation;

  public RailcraftPageButton(int x, int y, boolean isForward, ResourceLocation atlasLocation,
      OnPress onPress) {
    super(x, y, isForward, onPress, true);
    this.isForward = isForward;
    this.atlasLocation = atlasLocation;
  }

  @Override
  public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    int i = 0;
    int j = 192;
    if (this.isHoveredOrFocused()) {
      i += 23;
    }

    if (!this.isForward) {
      j += 13;
    }

    guiGraphics.blit(this.atlasLocation, this.getX(), this.getY(), i, j, 23, 13);
  }
}
