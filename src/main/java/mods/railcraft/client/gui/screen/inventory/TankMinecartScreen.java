package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeRenderer;
import mods.railcraft.world.inventory.TankMinecartMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TankMinecartScreen extends RailcraftMenuScreen<TankMinecartMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE_LOCATION =
      RailcraftConstants.rl("textures/gui/container/tank_minecart.png");

  public TankMinecartScreen(TankMinecartMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
    this.registerWidgetRenderer(new FluidGaugeRenderer(menu.getFluidGauge()));
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
