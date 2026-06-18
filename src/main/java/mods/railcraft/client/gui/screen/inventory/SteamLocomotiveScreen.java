package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeRenderer;
import mods.railcraft.client.gui.screen.inventory.widget.GaugeRenderer;
import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.gui.widget.GaugeWidget;
import mods.railcraft.world.inventory.SteamLocomotiveMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class SteamLocomotiveScreen extends LocomotiveScreen<SteamLocomotiveMenu> {

  private static final Identifier TEXTURE_LOCATION =
      RailcraftConstants.id("textures/gui/container/steam_locomotive.png");

  public SteamLocomotiveScreen(SteamLocomotiveMenu menu, Inventory inv, Component title) {
    super(menu, inv, title, SteamLocomotiveMenu.HEIGHT, "steam");
    this.inventoryLabelY = 110;
    for (var w : this.menu.getWidgets()) {
      if (w instanceof FluidGaugeWidget fluidGaugeWidget) {
        this.registerWidgetRenderer(new FluidGaugeRenderer(fluidGaugeWidget));
      }
      if (w instanceof GaugeWidget gaugeWidget) {
        this.registerWidgetRenderer(new GaugeRenderer(gaugeWidget));
      }
    }
  }

  @Override
  public Identifier getWidgetsTexture() {
    return TEXTURE_LOCATION;
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY,
      float partialTicks) {
    super.extractBackground(graphics, mouseX, mouseY, partialTicks);
    int x = (width - this.getImageWidth()) / 2;
    int y = (height - this.getImageHeight()) / 2;
    if (this.menu.getLocomotive().boiler().hasFuel()) {
      int scale = this.menu.getLocomotive().boiler().getBurnProgressScaled(12);
      graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE_LOCATION, x + 99, y + 33 - scale, 176,
          59 - scale, 14, scale + 2, 256, 256);
    }
  }
}
