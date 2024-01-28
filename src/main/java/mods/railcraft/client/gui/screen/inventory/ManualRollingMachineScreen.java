package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.world.inventory.ManualRollingMachineMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ManualRollingMachineScreen extends RailcraftMenuScreen<ManualRollingMachineMenu> {

  private static final ResourceLocation BACKGROUND_TEXTURE =
      Railcraft.rl("textures/gui/container/manual_rolling_machine.png");

  public ManualRollingMachineScreen(ManualRollingMachineMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return BACKGROUND_TEXTURE;
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
    float progress = this.menu.rollingProgress();
    guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + 89, this.topPos + 47, 176, 0,
        Math.round(24.00F * progress), 12);
  }
}
