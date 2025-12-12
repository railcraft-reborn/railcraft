package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.widget.AnalogGaugeRenderer;
import mods.railcraft.world.inventory.CrusherMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class CrusherScreen extends RailcraftMenuScreen<CrusherMenu> {

  private static final Identifier WIDGETS_TEXTURE =
      RailcraftConstants.id("textures/gui/container/crusher.png");

  public CrusherScreen(CrusherMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.imageHeight = 171;
    this.inventoryLabelY = this.imageHeight - 94;

    this.registerWidgetRenderer(new AnalogGaugeRenderer(menu.getEnergyWidget()));
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
    int x = (this.width - this.imageWidth) / 2;
    int y = (this.height - this.imageHeight) / 2;

    var logic = this.menu.getCrusher().getCrusherModule();
    if (logic.getProgress() > 0) {
      var progressPercent = (int) (logic.getProgressPercent() * 29 + 1);
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
          WIDGETS_TEXTURE, x + 73, y + 20, 176, 0, progressPercent, 38, 256, 256);
    }
  }

  @Override
  public Identifier getWidgetsTexture() {
    return WIDGETS_TEXTURE;
  }
}
