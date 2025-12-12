package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeRenderer;
import mods.railcraft.world.inventory.TankMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class TankScreen extends RailcraftMenuScreen<TankMenu> {

  private static final Identifier WIDGETS_TEXTURE_LOCATION =
      RailcraftConstants.id("textures/gui/container/tank.png");

  public TankScreen(TankMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.registerWidgetRenderer(new FluidGaugeRenderer(menu.getFluidGauge()));
  }

  @Override
  public Identifier getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
