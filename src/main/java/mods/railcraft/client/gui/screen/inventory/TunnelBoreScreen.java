package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.IngameWindowScreen;
import mods.railcraft.world.inventory.TunnelBoreMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TunnelBoreScreen extends RailcraftMenuScreen<TunnelBoreMenu> {

  private static final ResourceLocation WIDGETS_LOCATION =
      RailcraftConstants.rl("textures/gui/container/tunnel_bore.png");

  private static final Component HEAD =
      Component.translatable(Translations.Screen.TUNNEL_BORE_HEAD);
  private static final Component FUEL =
      Component.translatable(Translations.Screen.TUNNEL_BORE_FUEL);
  private static final Component BALLAST =
      Component.translatable(Translations.Screen.TUNNEL_BORE_BALLAST);
  private static final Component TRACK =
      Component.translatable(Translations.Screen.TUNNEL_BORE_TRACK);

  public TunnelBoreScreen(TunnelBoreMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.imageHeight = TunnelBoreMenu.IMAGE_HEIGHT;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    super.renderLabels(guiGraphics, mouseX, mouseY);
    guiGraphics.drawString(this.font, HEAD, 13, 24, IngameWindowScreen.TEXT_COLOR, false);
    guiGraphics.drawString(this.font, FUEL, 62, 24, IngameWindowScreen.TEXT_COLOR, false);
    guiGraphics.drawString(this.font, BALLAST, this.inventoryLabelX, 60,
        IngameWindowScreen.TEXT_COLOR, false);
    guiGraphics.drawString(this.font, TRACK, this.inventoryLabelX, 96,
        IngameWindowScreen.TEXT_COLOR, false);
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
    int centredX = (this.width - this.imageWidth) / 2;
    int centredY = (this.height - this.imageHeight) / 2;

    if (this.menu.getTunnelBore().getFuel() > 0) {
      int burnProgress = this.menu.getTunnelBore().getBurnProgressScaled(12);
      guiGraphics.blit(WIDGETS_LOCATION, centredX + 44, (centredY + 48) - burnProgress, 176,
        12 - burnProgress, 14, burnProgress + 2);
    }
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_LOCATION;
  }
}
