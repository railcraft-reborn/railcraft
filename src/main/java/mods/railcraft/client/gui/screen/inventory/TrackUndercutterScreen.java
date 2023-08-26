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

public class TrackUndercutterScreen extends MaintenanceMinecartScreen<TrackUndercutterMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/track_undercutter.png");

  private static final Component UNDER = Component.translatable(Translations.Screen.UNDER);
  private static final Component SIDES = Component.translatable(Translations.Screen.SIDES);

  public TrackUndercutterScreen(TrackUndercutterMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title, menu.getTrackUndercutter());
    this.imageHeight = 205;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY,
        4210752, false);
    guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY,
        4210752, false);
    guiGraphics.drawString(this.font, PATTERN, 8, 23, IngameWindowScreen.TEXT_COLOR, false);
    guiGraphics.drawString(this.font, STOCK, 125, 21, IngameWindowScreen.TEXT_COLOR, false);
    GuiUtil.drawCenteredString(guiGraphics, this.font, UNDER, imageWidth, 23);
    GuiUtil.drawCenteredString(guiGraphics, this.font, SIDES, imageWidth, 65);
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
