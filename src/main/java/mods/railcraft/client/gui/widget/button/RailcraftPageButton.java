package mods.railcraft.client.gui.widget.button;

import mods.railcraft.client.gui.screen.RoutingTableBookScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.PageButton;

public class RailcraftPageButton extends PageButton {

  private final boolean isForward;

  public RailcraftPageButton(int x, int y, boolean isForward, OnPress onPress) {
    super(x, y, isForward, onPress, true);
    this.isForward = isForward;
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

    guiGraphics.blit(RoutingTableBookScreen.BOOK_LOCATION, this.getX(), this.getY(), i, j, 23, 13);
  }
}
