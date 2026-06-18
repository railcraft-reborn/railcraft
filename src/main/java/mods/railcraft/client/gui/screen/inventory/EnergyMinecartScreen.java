package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.IngameWindowScreen;
import mods.railcraft.client.gui.screen.inventory.widget.GaugeRenderer;
import mods.railcraft.world.inventory.EnergyMinecartMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class EnergyMinecartScreen extends RailcraftMenuScreen<EnergyMinecartMenu> {

  private static final Identifier WIDGETS_TEXTURE_LOCATION =
      RailcraftConstants.id("textures/gui/container/energy_minecart.png");

  public EnergyMinecartScreen(EnergyMinecartMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
    this.registerWidgetRenderer(new GaugeRenderer(menu.getEnergyGauge()));
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
    super.extractLabels(graphics, xm, ym);
    graphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY,
        IngameWindowScreen.TEXT_COLOR, false);
  }

  @Override
  public Identifier getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
