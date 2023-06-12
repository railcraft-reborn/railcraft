package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeRenderer;
import mods.railcraft.world.inventory.SteamOvenMenu;
import mods.railcraft.world.module.SteamOvenModule;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SteamOvenScreen extends RailcraftMenuScreen<SteamOvenMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/steam_oven.png");
  private final SteamOvenModule steamOvenModule;

  public SteamOvenScreen(SteamOvenMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.steamOvenModule = menu.getSteamOven().getSteamOvenModule();
    this.registerWidgetRenderer(new FluidGaugeRenderer(menu.getSteamFluidGauge()));
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
    int x = (this.width - this.imageWidth) / 2;
    int y = (this.height - this.imageHeight) / 2;
    if (this.steamOvenModule.getProgress() > 0) {
      int scale = (int) (this.steamOvenModule.getProgressPercent() * 49);
      guiGraphics.blit(WIDGETS_TEXTURE, x + 65, y + 18 + 49 - scale, 176, 47 + 49 - scale, 23, scale + 1);
    }
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE;
  }
}
