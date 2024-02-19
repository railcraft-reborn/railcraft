package mods.railcraft.client.gui.screen.inventory.detector;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.inventory.detector.SheepDetectorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SheepDetectorScreen extends AbstractContainerScreen<SheepDetectorMenu> {

  private static final ResourceLocation BACKGROUND_TEXTURE =
      RailcraftConstants.rl("textures/gui/container/single_slot.png");

  public SheepDetectorScreen(SheepDetectorMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.imageHeight = 140;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    this.renderBackground(guiGraphics);
    final int x = this.leftPos;
    final int y = this.topPos;
    guiGraphics.blit(BACKGROUND_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
  }
}
