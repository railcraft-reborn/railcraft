package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.widget.AnalogGaugeRenderer;
import mods.railcraft.world.inventory.SteamTurbineMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class SteamTurbineScreen extends RailcraftMenuScreen<SteamTurbineMenu> {

  private static final Identifier WIDGETS_TEXTURE_LOCATION =
      RailcraftConstants.id("textures/gui/container/steam_turbine.png");

  public SteamTurbineScreen(SteamTurbineMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title, SteamTurbineMenu.GUI_HEIGHT);
    this.registerWidgetRenderer(new AnalogGaugeRenderer(menu.getTurbineWidget()));
    this.registerWidgetRenderer(new AnalogGaugeRenderer(menu.getChargeWidget()));

    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  public Identifier getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
    super.extractLabels(graphics, xm, ym);
    graphics.text(this.font, Component.translatable(Translations.Screen.STEAM_TURBINE_ROTOR),
        20, 29, 0x404040, false);
    graphics.text(this.font, Component.translatable(Translations.Screen.STEAM_TURBINE_OUTPUT),
        93, 24, 0x404040, false);
    graphics.text(this.font, Component.translatable(Translations.Screen.STEAM_TURBINE_USAGE),
        95, 43, 0x404040, false);
  }
}
