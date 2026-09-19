package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.widget.GaugeRenderer;
import mods.railcraft.world.inventory.ElectricLocomotiveMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class ElectricLocomotiveScreen extends LocomotiveScreen<ElectricLocomotiveMenu> {

  private static final Identifier TEXTURE_LOCATION =
      RailcraftConstants.id("textures/gui/container/electric_locomotive.png");

  public ElectricLocomotiveScreen(ElectricLocomotiveMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title, "electric");
    this.registerWidgetRenderer(new GaugeRenderer(menu.getEnergyGauge()));
  }

  @Override
  public Identifier getWidgetsTexture() {
    return TEXTURE_LOCATION;
  }
}
