package mods.railcraft.client.gui.screen.inventory.detector;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.inventory.detector.AdvancedDetectorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedDetectorScreen extends AbstractContainerScreen<AdvancedDetectorMenu> {

  private static final ResourceLocation BACKGROUND_TEXTURE =
      RailcraftConstants.rl("textures/gui/container/advanced_detector.png");

  public AdvancedDetectorScreen(AdvancedDetectorMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.imageHeight = 140;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
    final int x = this.leftPos;
    final int y = this.topPos;
    guiGraphics.blit(BACKGROUND_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
  }
}
