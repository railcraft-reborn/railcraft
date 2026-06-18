package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.widget.AnalogGaugeRenderer;
import mods.railcraft.world.inventory.PoweredRollingMachineMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class PoweredRollingMachineScreen extends RailcraftMenuScreen<PoweredRollingMachineMenu> {

  private static final Identifier BACKGROUND_TEXTURE =
      RailcraftConstants.id("textures/gui/container/powered_rolling_machine.png");

  public PoweredRollingMachineScreen(PoweredRollingMachineMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
    this.registerWidgetRenderer(new AnalogGaugeRenderer(menu.getEnergyWidget()));
  }

  @Override
  public Identifier getWidgetsTexture() {
    return BACKGROUND_TEXTURE;
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY,
      float partialTicks) {
    super.extractBackground(graphics, mouseX, mouseY, partialTicks);
    float progress = this.menu.rollingProgress();
    graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + 89,
        this.topPos + 36, 176, 0, Math.round(24.00F * progress), 12, 256, 256);
  }
}
