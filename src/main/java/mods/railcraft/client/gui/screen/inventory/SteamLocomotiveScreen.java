package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeRenderer;
import mods.railcraft.client.gui.screen.inventory.widget.GaugeRenderer;
import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.gui.widget.GaugeWidget;
import mods.railcraft.world.inventory.SteamLocomotiveMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SteamLocomotiveScreen extends LocomotiveScreen<SteamLocomotiveMenu> {

  private static final ResourceLocation TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/steam_locomotive.png");

  public SteamLocomotiveScreen(SteamLocomotiveMenu menu, Inventory inv, Component title) {
    super(menu, inv, title, "steam");
    this.imageHeight = SteamLocomotiveMenu.HEIGHT;
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
  public ResourceLocation getWidgetsTexture() {
    return TEXTURE_LOCATION;
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
    int x = (width - this.getXSize()) / 2;
    int y = (height - this.getYSize()) / 2;
    if (this.menu.getLocomotive().boiler().hasFuel()) {
      int scale = this.menu.getLocomotive().boiler().getBurnProgressScaled(12);
      guiGraphics.blit(TEXTURE_LOCATION, x + 99, y + 33 - scale, 176, 59 - scale, 14, scale + 2);
    }
  }
}
