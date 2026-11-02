package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeRenderer;
import mods.railcraft.client.gui.screen.inventory.widget.GaugeRenderer;
import mods.railcraft.world.inventory.HobbyistSteamEngineMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class HobbyistSteamEngineScreen extends RailcraftMenuScreen<HobbyistSteamEngineMenu> {

  private static final Identifier WIDGETS_LOCATION =
      RailcraftConstants.id("textures/gui/container/hobbyist_steam_engine.png");

  public HobbyistSteamEngineScreen(HobbyistSteamEngineMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
    this.registerWidgetRenderer(new FluidGaugeRenderer(menu.getSteamGauge()));
    this.registerWidgetRenderer(new FluidGaugeRenderer(menu.getWaterGauge()));
    this.registerWidgetRenderer(new GaugeRenderer(menu.getTemperatureGauge()));
    this.registerWidgetRenderer(new GaugeRenderer(menu.getEnergyGauge()));
  }

  @Override
  public Identifier getWidgetsTexture() {
    return WIDGETS_LOCATION;
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY,
      float partialTicks) {
    super.extractBackground(graphics, mouseX, mouseY, partialTicks);
    var boiler = this.menu.getSteamEngine().getBoiler();
    if (boiler.hasFuel()) {
      int x = (this.width - this.getImageWidth()) / 2;
      int y = (this.height - this.getImageHeight()) / 2;
      int scale = boiler.getBurnProgressScaled(12);
      graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_LOCATION, x + 62, y + 34 - scale, 176,
          59 - scale, 14, scale + 2, 256, 256);
    }
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
    super.extractLabels(graphics, mouseX, mouseY);
    graphics.text(this.font, Component.translatable(Translations.Screen.STEAM_ENGINE_OUTPUT,
        Math.round(this.menu.getSteamEngine().getCurrentOutput())), 55, 60, 0x404040, false);
  }
}
