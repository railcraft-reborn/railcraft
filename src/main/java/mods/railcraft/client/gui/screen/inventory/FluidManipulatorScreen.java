package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeRenderer;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetFluidManipulatorAttributesMessage;
import mods.railcraft.world.inventory.FluidManipulatorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FluidManipulatorScreen extends ManipulatorScreen<FluidManipulatorMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE_LOCATION =
      Railcraft.rl("textures/gui/container/fluid_manipulator.png");

  public FluidManipulatorScreen(FluidManipulatorMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
    this.registerWidgetRenderer(new FluidGaugeRenderer(menu.getFluidGauge()));
  }

  @Override
  protected void sendAttributes() {
    NetworkChannel.GAME.sendToServer(new SetFluidManipulatorAttributesMessage(
        this.menu.getManipulator().getBlockPos(), this.menu.getManipulator().getRedstoneMode()));
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
