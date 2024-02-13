package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.widget.AnalogGaugeRenderer;
import mods.railcraft.world.inventory.SteamTurbineMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SteamTurbineScreen extends RailcraftMenuScreen<SteamTurbineMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE_LOCATION =
      RailcraftConstants.rl("textures/gui/container/steam_turbine.png");

  public SteamTurbineScreen(SteamTurbineMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.registerWidgetRenderer(new AnalogGaugeRenderer(menu.getTurbineWidget()));
    this.registerWidgetRenderer(new AnalogGaugeRenderer(menu.getChargeWidget()));

    this.imageHeight = SteamTurbineMenu.GUI_HEIGHT;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    super.renderLabels(guiGraphics, mouseX, mouseY);
    guiGraphics.drawString(this.font,
        Component.translatable(Translations.Screen.STEAM_TURBINE_ROTOR), 20, 29, 0x404040, false);
    guiGraphics.drawString(this.font,
        Component.translatable(Translations.Screen.STEAM_TURBINE_OUTPUT), 93, 24, 0x404040, false);
    guiGraphics.drawString(this.font,
        Component.translatable(Translations.Screen.STEAM_TURBINE_USAGE), 95, 43, 0x404040, false);
  }
}
