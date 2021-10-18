package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeWidgetRenderer;
import mods.railcraft.world.inventory.TankMinecartMenu;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class TankMinecartScreen extends RailcraftMenuScreen<TankMinecartMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/tank_minecart.png");

  public TankMinecartScreen(TankMinecartMenu menu, PlayerInventory inventory,
      ITextComponent title) {
    super(menu, inventory, title);
    this.registerWidgetRenderer(new FluidGaugeWidgetRenderer(menu.getFluidGauge()));
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
