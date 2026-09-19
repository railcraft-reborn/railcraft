package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.IngameWindowScreen;
import mods.railcraft.world.inventory.TunnelBoreMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class TunnelBoreScreen extends RailcraftMenuScreen<TunnelBoreMenu> {

  private static final Identifier WIDGETS_LOCATION =
      RailcraftConstants.id("textures/gui/container/tunnel_bore.png");

  private static final Component HEAD =
      Component.translatable(Translations.Screen.TUNNEL_BORE_HEAD);
  private static final Component FUEL =
      Component.translatable(Translations.Screen.TUNNEL_BORE_FUEL);
  private static final Component BALLAST =
      Component.translatable(Translations.Screen.TUNNEL_BORE_BALLAST);
  private static final Component TRACK =
      Component.translatable(Translations.Screen.TUNNEL_BORE_TRACK);

  public TunnelBoreScreen(TunnelBoreMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title, TunnelBoreMenu.IMAGE_HEIGHT);
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
    super.extractLabels(graphics, xm, ym);
    graphics.text(this.font, HEAD, 13, 24, IngameWindowScreen.TEXT_COLOR, false);
    graphics.text(this.font, FUEL, 62, 24, IngameWindowScreen.TEXT_COLOR, false);
    graphics.text(this.font, BALLAST, this.inventoryLabelX, 60,
        IngameWindowScreen.TEXT_COLOR, false);
    graphics.text(this.font, TRACK, this.inventoryLabelX, 96,
        IngameWindowScreen.TEXT_COLOR, false);
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY,
      float partialTicks) {
    super.extractBackground(graphics, mouseX, mouseY, partialTicks);
    int centredX = (this.width - this.imageWidth) / 2;
    int centredY = (this.height - this.imageHeight) / 2;

    if (this.menu.getTunnelBore().getFuel() > 0) {
      int burnProgress = this.menu.getTunnelBore().getBurnProgressScaled(12);
      graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_LOCATION, centredX + 44,
          (centredY + 48) - burnProgress, 176, 12 - burnProgress, 14, burnProgress + 2, 256, 256);
    }
  }

  @Override
  public Identifier getWidgetsTexture() {
    return WIDGETS_LOCATION;
  }
}
