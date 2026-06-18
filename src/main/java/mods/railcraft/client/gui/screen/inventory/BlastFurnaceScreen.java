package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class BlastFurnaceScreen extends RailcraftMenuScreen<BlastFurnaceMenu> {

  private static final Identifier WIDGETS_TEXTURE =
      RailcraftConstants.id("textures/gui/container/blast_furnace.png");

  public BlastFurnaceScreen(BlastFurnaceMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY,
      float partialTicks) {
    super.extractBackground(graphics, mouseX, mouseY, partialTicks);
    int x = (this.width - this.imageWidth) / 2;
    int y = (this.height - this.imageHeight) / 2;

    var logic = this.menu.getBlastFurnace().getBlastFurnaceModule();
    if (logic.isBurning()) {
      int burnProgressScale = logic.getBurnProgressScaled(12);
      graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, x + 56,
          y + 36 + 12 - burnProgressScale, 176, 12 - burnProgressScale,
          14, burnProgressScale + 2, 256, 256);
    }

    int progressScale = (int) (logic.getProgressPercent() * 24);
    graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, x + 79, y + 34, 176, 14,
        progressScale + 1, 16, 256, 256);
  }

  @Override
  public Identifier getWidgetsTexture() {
    return WIDGETS_TEXTURE;
  }
}
