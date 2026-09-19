package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeRenderer;
import mods.railcraft.network.to_server.SetFluidManipulatorMessage;
import mods.railcraft.world.inventory.FluidManipulatorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class FluidManipulatorScreen extends ManipulatorScreen<FluidManipulatorMenu> {

  private static final Identifier WIDGETS_TEXTURE_LOCATION =
      RailcraftConstants.id("textures/gui/container/fluid_manipulator.png");

  public FluidManipulatorScreen(FluidManipulatorMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
    this.registerWidgetRenderer(new FluidGaugeRenderer(menu.getFluidGauge()));
  }

  @Override
  protected void sendAttributes() {
    ClientPacketDistributor.sendToServer(new SetFluidManipulatorMessage(
        this.menu.getManipulator().getBlockPos(), this.menu.getManipulator().getRedstoneMode()));
  }

  @Override
  public Identifier getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
