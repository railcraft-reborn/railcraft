package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeRenderer;
import mods.railcraft.world.inventory.SteamOvenMenu;
import mods.railcraft.world.module.SteamOvenModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class SteamOvenScreen extends RailcraftMenuScreen<SteamOvenMenu> {

  private static final Identifier WIDGETS_TEXTURE =
      RailcraftConstants.id("textures/gui/container/steam_oven.png");
  private final SteamOvenModule steamOvenModule;

  public SteamOvenScreen(SteamOvenMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.steamOvenModule = menu.getSteamOven().getSteamOvenModule();
    this.registerWidgetRenderer(new FluidGaugeRenderer(menu.getSteamFluidGauge()));
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY,
      float partialTicks) {
    super.extractBackground(graphics, mouseX, mouseY, partialTicks);
    int x = (this.width - this.imageWidth) / 2;
    int y = (this.height - this.imageHeight) / 2;
    if (this.steamOvenModule.getProgress() > 0) {
      int scale = (int) (this.steamOvenModule.getProgressPercent() * 49);
      graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, x + 65, y + 18 + 49 - scale, 176,
          47 + 49 - scale, 23, scale + 1, 256, 256);
    }
  }

  @Override
  public Identifier getWidgetsTexture() {
    return WIDGETS_TEXTURE;
  }
}
