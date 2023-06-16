package mods.railcraft.client.gui.screen.inventory;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Railcraft;
import mods.railcraft.Translations;
import mods.railcraft.client.gui.screen.IngameWindowScreen;
import mods.railcraft.world.inventory.TrackLayerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TrackLayerScreen extends RailcraftMenuScreen<TrackLayerMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/track_layer.png");

  private static final Component PATTERN =
      Component.translatable(Translations.Screen.PATTERN);
  private static final Component STOCK =
      Component.translatable(Translations.Screen.STOCK);

  public TrackLayerScreen(TrackLayerMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
  }

  @Override
  protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
    super.renderLabels(poseStack, mouseX, mouseY);
    this.font.draw(poseStack, PATTERN, 38, 30, IngameWindowScreen.TEXT_COLOR);
    this.font.draw(poseStack, STOCK, 125, 25, IngameWindowScreen.TEXT_COLOR);
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
