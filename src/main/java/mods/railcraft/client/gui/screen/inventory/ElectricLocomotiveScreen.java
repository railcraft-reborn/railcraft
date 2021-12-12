/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2019
 https://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at https://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.client.gui.screen.inventory;

import java.util.List;

import mods.railcraft.Railcraft;
import mods.railcraft.client.gui.screen.inventory.widget.GaugeWidgetRenderer;
import mods.railcraft.gui.widget.Gauge;
import mods.railcraft.gui.widget.GaugeWidget;
import mods.railcraft.world.inventory.ElectricLocomotiveMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ElectricLocomotiveScreen extends LocomotiveScreen<ElectricLocomotiveMenu> {

  private static final ResourceLocation TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/electric_locomotive.png");

  public ElectricLocomotiveScreen(ElectricLocomotiveMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title, "electric");

    GaugeWidget wiget = (GaugeWidget)menu.getWidgets().get(0);
    this.registerWidgetRenderer(new GaugeWidgetRenderer(wiget) {
      @Override
      public List<Component> getTooltip() {
        Gauge theGauge = wiget.getGauge();
        theGauge.refresh();
        return theGauge.getTooltip();
      }
    });
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return TEXTURE_LOCATION;
  }
}
