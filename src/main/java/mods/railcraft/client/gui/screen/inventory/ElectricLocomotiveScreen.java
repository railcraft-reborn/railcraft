package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.client.gui.screen.inventory.widget.GaugeRenderer;
import mods.railcraft.world.inventory.ElectricLocomotiveMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ElectricLocomotiveScreen extends LocomotiveScreen<ElectricLocomotiveMenu> {

  private static final ResourceLocation TEXTURE_LOCATION =
      Railcraft.rl("textures/gui/container/electric_locomotive.png");

  public ElectricLocomotiveScreen(ElectricLocomotiveMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title, "electric");
    this.registerWidgetRenderer(new GaugeRenderer(menu.getEnergyGauge()));
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return TEXTURE_LOCATION;
  }
}
