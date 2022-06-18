package mods.railcraft.client.gui.screen.inventory;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Railcraft;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeRenderer;
import mods.railcraft.client.gui.screen.inventory.widget.GaugeRenderer;
import mods.railcraft.world.inventory.SolidFueledSteamBoilerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SolidFueledSteamBoilerScreen extends RailcraftMenuScreen<SolidFueledSteamBoilerMenu> {

  private static final ResourceLocation WIDGETS_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/solid_fueled_steam_boiler.png");

  public SolidFueledSteamBoilerScreen(SolidFueledSteamBoilerMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
    this.registerWidgetRenderer(new GaugeRenderer(menu.getTemperatureGauge()));
    this.registerWidgetRenderer(new FluidGaugeRenderer(menu.getWaterGauge()));
    this.registerWidgetRenderer(new FluidGaugeRenderer(menu.getSteamGauge()));
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_LOCATION;
  }

  @Override
  protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
    super.renderBg(poseStack, partialTick, mouseX, mouseY);
    int x = (this.width - this.getXSize()) / 2;
    int y = (this.height - this.getYSize()) / 2;
    if (this.menu.getModule().getBoiler().hasFuel()) {
      int scale = this.menu.getModule().getBoiler().getBurnProgressScaled(12);
      this.blit(poseStack, x + 62, y + 34 - scale, 176, 59 - scale, 14, scale + 2);
    }
  }
}
