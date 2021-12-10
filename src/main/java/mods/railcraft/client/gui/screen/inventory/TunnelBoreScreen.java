package mods.railcraft.client.gui.screen.inventory;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Railcraft;
import mods.railcraft.client.gui.screen.IngameWindowScreen;
import mods.railcraft.world.inventory.TunnelBoreMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class TunnelBoreScreen extends RailcraftMenuScreen<TunnelBoreMenu> {

  private static final ResourceLocation WIDGETS_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/tunnel_bore.png");

  private static final Component HEAD =
      new TranslatableComponent("screen.tunnel_bore.head");
  private static final Component FUEL =
      new TranslatableComponent("screen.tunnel_bore.fuel");
  private static final Component BALLAST =
      new TranslatableComponent("screen.tunnel_bore.ballast");
  private static final Component TRACK =
      new TranslatableComponent("screen.tunnel_bore.track");

  public TunnelBoreScreen(TunnelBoreMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.imageHeight = TunnelBoreMenu.IMAGE_HEIGHT;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
    super.renderLabels(matrixStack, mouseX, mouseY);
    this.font.draw(matrixStack, HEAD, 13, 24, IngameWindowScreen.TEXT_COLOR);
    this.font.draw(matrixStack, FUEL, 62, 24, IngameWindowScreen.TEXT_COLOR);
    this.font.draw(matrixStack, BALLAST, this.inventoryLabelX, 60, IngameWindowScreen.TEXT_COLOR);
    this.font.draw(matrixStack, TRACK, this.inventoryLabelX, 96, IngameWindowScreen.TEXT_COLOR);
  }

  @Override
  protected void renderBg(PoseStack matrixStack, float f, int i, int j) {
    super.renderBg(matrixStack, f, i, j);
    int centredX = (this.width - this.imageWidth) / 2;
    int centredY = (this.height - this.imageHeight) / 2;

    if (this.menu.getTunnelBore().getFuel() > 0) {
      int burnProgress = this.menu.getTunnelBore().getBurnProgressScaled(12);
      this.blit(matrixStack, centredX + 44, (centredY + 48) - burnProgress, 176, 12 - burnProgress,
          14, burnProgress + 2);
    }
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_LOCATION;
  }
}
