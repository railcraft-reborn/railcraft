package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeRenderer;
import mods.railcraft.world.inventory.WaterTankSidingMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WaterTankSidingScreen extends RailcraftMenuScreen<WaterTankSidingMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE_LOCATION =
      Railcraft.rl("textures/gui/container/tank.png");

  public WaterTankSidingScreen(WaterTankSidingMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.registerWidgetRenderer(new FluidGaugeRenderer(menu.getFluidGauge()));
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
