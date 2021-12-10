package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeWidgetRenderer;
import mods.railcraft.world.inventory.TankMinecartMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class TankMinecartScreen extends RailcraftMenuScreen<TankMinecartMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/tank_minecart.png");

  public TankMinecartScreen(TankMinecartMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
    this.registerWidgetRenderer(new FluidGaugeWidgetRenderer(menu.getFluidGauge()));
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
