package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeRenderer;
import mods.railcraft.world.inventory.CokeOvenMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CokeOvenScreen extends RailcraftMenuScreen<CokeOvenMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE =
      Railcraft.rl("textures/gui/container/coke_oven.png");

  public CokeOvenScreen(CokeOvenMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);

    this.registerWidgetRenderer(new FluidGaugeRenderer(this.menu.getFluidGauge()));
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
    int x = (this.width - this.imageWidth) / 2;
    int y = (this.height - this.imageHeight) / 2;

    var logic = this.menu.getCokeOven().getCokeOvenModule();
    if (logic.getProgress() > 0) {
      var progressPercent = logic.getProgressPercent();
      int burnProgress = (int) ((1.0 - progressPercent) * 12);
      guiGraphics.blit(WIDGETS_TEXTURE, x + 16, (y + 38) - burnProgress, 176, 59 - burnProgress, 14,
          burnProgress + 2);
      int cookProgress = (int) (progressPercent * 20);
      guiGraphics.blit(WIDGETS_TEXTURE, x + 34, y + 43, 176, 61, cookProgress + 1, 16);
    }
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE;
  }
}
