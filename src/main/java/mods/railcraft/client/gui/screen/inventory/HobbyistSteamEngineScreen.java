package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeRenderer;
import mods.railcraft.client.gui.screen.inventory.widget.GaugeRenderer;
import mods.railcraft.world.inventory.HobbyistSteamEngineMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class HobbyistSteamEngineScreen extends RailcraftMenuScreen<HobbyistSteamEngineMenu> {

  private static final ResourceLocation WIDGETS_LOCATION =
      RailcraftConstants.rl("textures/gui/container/hobbyist_steam_engine.png");

  public HobbyistSteamEngineScreen(HobbyistSteamEngineMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
    this.registerWidgetRenderer(new FluidGaugeRenderer(menu.getSteamGauge()));
    this.registerWidgetRenderer(new FluidGaugeRenderer(menu.getWaterGauge()));
    this.registerWidgetRenderer(new GaugeRenderer(menu.getTemperatureGauge()));
    this.registerWidgetRenderer(new GaugeRenderer(menu.getEnergyGauge()));
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_LOCATION;
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
    var boiler = this.menu.getSteamEngine().getBoiler();
    if (boiler.hasFuel()) {
      int x = (this.width - this.getXSize()) / 2;
      int y = (this.height - this.getYSize()) / 2;
      int scale = boiler.getBurnProgressScaled(12);
      guiGraphics.blit(WIDGETS_LOCATION, x + 62, y + 34 - scale, 176, 59 - scale, 14, scale + 2);
    }
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    super.renderLabels(guiGraphics, mouseX, mouseY);
    guiGraphics.drawString(this.font, Component.translatable(Translations.Screen.STEAM_ENGINE_OUTPUT,
        Math.round(this.menu.getSteamEngine().getCurrentOutput())), 55, 60, 0x404040, false);
  }
}
