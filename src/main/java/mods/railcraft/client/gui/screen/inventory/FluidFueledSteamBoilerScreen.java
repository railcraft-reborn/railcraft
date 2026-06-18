package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeRenderer;
import mods.railcraft.client.gui.screen.inventory.widget.GaugeRenderer;
import mods.railcraft.world.inventory.FluidFueledSteamBoilerMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class FluidFueledSteamBoilerScreen extends RailcraftMenuScreen<FluidFueledSteamBoilerMenu> {

  private static final Identifier WIDGETS_LOCATION =
      RailcraftConstants.id("textures/gui/container/fluid_fueled_steam_boiler.png");

  public FluidFueledSteamBoilerScreen(FluidFueledSteamBoilerMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
    this.registerWidgetRenderer(new GaugeRenderer(menu.getTemperatureGauge()));
    this.registerWidgetRenderer(new FluidGaugeRenderer(menu.getWaterGauge()));
    this.registerWidgetRenderer(new FluidGaugeRenderer(menu.getSteamGauge()));
    this.registerWidgetRenderer(new FluidGaugeRenderer(menu.getFuelGauge()));
  }

  @Override
  public Identifier getWidgetsTexture() {
    return WIDGETS_LOCATION;
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY,
      float partialTicks) {
    super.extractBackground(graphics, mouseX, mouseY, partialTicks);
    int x = (this.width - this.getImageWidth()) / 2;
    int y = (this.height - this.getImageHeight()) / 2;
    if (this.menu.getModule().getBoiler().hasFuel()) {
      int scale = this.menu.getModule().getBoiler().getBurnProgressScaled(12);
      graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_LOCATION, x + 62, y + 50 - scale, 176,
          59 - scale, 14, scale + 2, 256, 256);
    }
  }
}
