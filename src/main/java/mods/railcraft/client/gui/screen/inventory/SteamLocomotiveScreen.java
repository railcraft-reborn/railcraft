/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2019
 https://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at https://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.client.gui.screen.inventory;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Railcraft;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeWidgetRenderer;
import mods.railcraft.client.gui.screen.inventory.widget.GaugeWidgetRenderer;
import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.gui.widget.GaugeWidget;
import mods.railcraft.gui.widget.Widget;
import mods.railcraft.world.inventory.SteamLocomotiveMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class SteamLocomotiveScreen extends LocomotiveScreen<SteamLocomotiveMenu> {

  private static final ResourceLocation TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/steam_locomotive.png");

  public SteamLocomotiveScreen(SteamLocomotiveMenu menu, Inventory inv,
      Component title) {
    super(menu, inv, title, "steam");
    this.imageHeight = SteamLocomotiveMenu.HEIGHT;
    this.inventoryLabelY = 110;
    for (Widget w : this.menu.getWidgets()) {
      if (w instanceof FluidGaugeWidget) {
        this.registerWidgetRenderer(new FluidGaugeWidgetRenderer((FluidGaugeWidget) w));
      }
      if (w instanceof GaugeWidget) {
        this.registerWidgetRenderer(new GaugeWidgetRenderer((GaugeWidget) w));
      }
    }
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return TEXTURE_LOCATION;
  }

  @Override
  protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
    super.renderBg(poseStack, partialTicks, mouseY, mouseY);
    int x = (width - this.getXSize()) / 2;
    int y = (height - this.getYSize()) / 2;
    if (this.menu.getLocomotive().getBoiler().hasFuel()) {
      int scale = this.menu.getLocomotive().getBoiler().getBurnProgressScaled(12);
      this.blit(poseStack, x + 99, y + 33 - scale, 176, 59 - scale, 14, scale + 2);
    }
  }
}
