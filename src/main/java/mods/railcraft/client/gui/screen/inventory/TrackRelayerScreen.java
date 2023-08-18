package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.Translations;
import mods.railcraft.client.gui.screen.IngameWindowScreen;
import mods.railcraft.world.inventory.TrackRelayerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TrackRelayerScreen extends RailcraftMenuScreen<TrackRelayerMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/track_relayer.png");

  private static final Component PATTERN =
      Component.translatable(Translations.Screen.PATTERN);
  private static final Component STOCK =
      Component.translatable(Translations.Screen.STOCK);

  public TrackRelayerScreen(TrackRelayerMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    super.renderLabels(guiGraphics, mouseX, mouseY);
    guiGraphics.drawString(this.font, PATTERN, 38, 30, IngameWindowScreen.TEXT_COLOR, false);
    guiGraphics.drawString(this.font, STOCK, 125, 25, IngameWindowScreen.TEXT_COLOR, false);
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
