package mods.railcraft.client.gui.screen.inventory;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.util.GuiUtil;
import mods.railcraft.world.inventory.DumpingTrackMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DumpingTrackScreen extends RailcraftMenuScreen<DumpingTrackMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE_LOCATION =
      RailcraftConstants.rl("textures/gui/container/dumping_track.png");

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
  protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
    super.renderLabels(poseStack, mouseX, mouseY);
    GuiUtil.drawCenteredString(poseStack, this.font, FILTERS, 250, 26);
    GuiUtil.drawCenteredString(poseStack, this.font, CARTS, 100, 35);
  }
}
