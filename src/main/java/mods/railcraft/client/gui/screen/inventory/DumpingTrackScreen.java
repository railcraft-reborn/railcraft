package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.Translations;
import mods.railcraft.client.util.GuiUtil;
import mods.railcraft.world.inventory.DumpingTrackMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DumpingTrackScreen extends RailcraftMenuScreen<DumpingTrackMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/dumping_track.png");

  private static final Component FILTERS =
      Component.translatable(Translations.Screen.ITEM_MANIPULATOR_FILTERS);
  private static final Component CARTS =
      Component.translatable(Translations.Screen.CART_FILTERS);

  public DumpingTrackScreen(DumpingTrackMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    super.renderLabels(guiGraphics, mouseX, mouseY);
    GuiUtil.drawCenteredString(guiGraphics, this.font, FILTERS, 250, 26);
    GuiUtil.drawCenteredString(guiGraphics, this.font, CARTS, 100, 35);
  }
}
