package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeRenderer;
import mods.railcraft.client.gui.screen.inventory.widget.GaugeRenderer;
import mods.railcraft.world.inventory.SteamEngineMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SteamEngineScreen extends RailcraftMenuScreen<SteamEngineMenu> {

  private static final ResourceLocation WIDGETS_LOCATION =
      RailcraftConstants.rl("textures/gui/container/steam_engine.png");

  public SteamEngineScreen(SteamEngineMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.registerWidgetRenderer(new FluidGaugeRenderer(menu.getSteamGauge()));
    this.registerWidgetRenderer(new GaugeRenderer(menu.getEnergyGauge()));
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_LOCATION;
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    super.renderLabels(guiGraphics, mouseX, mouseY);
    guiGraphics.drawString(this.font, Component.translatable(Translations.Screen.STEAM_ENGINE_OUTPUT,
        Math.round(this.menu.getSteamEngine().getCurrentOutput())), 110, 40, 0x404040, false);
  }
}
