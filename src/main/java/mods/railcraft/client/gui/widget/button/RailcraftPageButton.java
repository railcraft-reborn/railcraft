package mods.railcraft.client.gui.widget.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.client.gui.screen.RoutingTableBookScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.GameRenderer;

public class RailcraftPageButton extends PageButton {

  private final boolean isForward;

  public RailcraftPageButton(int x, int y, boolean isForward, OnPress onPress) {
    super(x, y, isForward, onPress, true);
    this.isForward = isForward;
  }

  @Override
  public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, RoutingTableBookScreen.BOOK_LOCATION);
    int i = 0;
    int j = 192;
    if (this.isHoveredOrFocused()) {
      i += 23;
    }

    if (!this.isForward) {
      j += 13;
    }

    this.blit(poseStack, this.getX(), this.getY(), i, j, 23, 13);
  }
}
