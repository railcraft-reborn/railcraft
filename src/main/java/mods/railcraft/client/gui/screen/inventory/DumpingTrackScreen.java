package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.util.GuiUtil;
import mods.railcraft.world.inventory.DumpingTrackMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class DumpingTrackScreen extends RailcraftMenuScreen<DumpingTrackMenu> {

  private static final Identifier WIDGETS_TEXTURE_LOCATION =
      RailcraftConstants.id("textures/gui/container/dumping_track.png");

  private static final Component FILTERS =
      Component.translatable(Translations.Screen.ITEM_MANIPULATOR_FILTERS);
  private static final Component CARTS =
      Component.translatable(Translations.Screen.CART_FILTERS);

  public DumpingTrackScreen(DumpingTrackMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
  }

  @Override
  public Identifier getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
    super.extractLabels(graphics, xm, ym);
    GuiUtil.drawCenteredString(graphics, this.font, FILTERS, 250, 26);
    GuiUtil.drawCenteredString(graphics, this.font, CARTS, 100, 35);
  }
}
