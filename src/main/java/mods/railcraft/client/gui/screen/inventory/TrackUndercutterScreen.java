package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.Translations;
import mods.railcraft.client.gui.screen.IngameWindowScreen;
import mods.railcraft.client.util.GuiUtil;
import mods.railcraft.world.inventory.TrackUndercutterMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TrackUndercutterScreen extends RailcraftMenuScreen<TrackUndercutterMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/track_undercutter.png");

  private static final Component PATTERN = Component.translatable(Translations.Screen.PATTERN);
  private static final Component STOCK = Component.translatable(Translations.Screen.STOCK);
  private static final Component UNDER = Component.translatable(Translations.Screen.UNDER);
  private static final Component SIDES = Component.translatable(Translations.Screen.SIDES);

  public TrackUndercutterScreen(TrackUndercutterMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.imageHeight = 205;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    super.renderLabels(guiGraphics, mouseX, mouseY);
    guiGraphics.drawString(this.font, PATTERN, 8, 23, IngameWindowScreen.TEXT_COLOR, false);
    guiGraphics.drawString(this.font, STOCK, 125, 21, IngameWindowScreen.TEXT_COLOR, false);

    //guiGraphics.drawString(this.font, UNDER, 125, 21, IngameWindowScreen.TEXT_COLOR, false);
    //guiGraphics.drawString(this.font, SIDES, 125, 21, IngameWindowScreen.TEXT_COLOR, false);

    GuiUtil.drawCenteredString(guiGraphics, this.font, UNDER, imageWidth, 23, false);
    GuiUtil.drawCenteredString(guiGraphics, this.font, SIDES, imageWidth, 65, false);
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
