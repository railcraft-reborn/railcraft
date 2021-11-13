package mods.railcraft.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.Railcraft;
import mods.railcraft.client.gui.screen.IngameWindowScreen;
import mods.railcraft.world.inventory.TunnelBoreMenu;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TunnelBoreScreen extends RailcraftMenuScreen<TunnelBoreMenu> {

  private static final ResourceLocation WIDGETS_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/tunnel_bore.png");

  private static final ITextComponent HEAD =
      new TranslationTextComponent("screen.tunnel_bore.head");
  private static final ITextComponent FUEL =
      new TranslationTextComponent("screen.tunnel_bore.fuel");
  private static final ITextComponent BALLAST =
      new TranslationTextComponent("screen.tunnel_bore.ballast");
  private static final ITextComponent TRACK =
      new TranslationTextComponent("screen.tunnel_bore.track");

  public TunnelBoreScreen(TunnelBoreMenu menu, PlayerInventory inventory, ITextComponent title) {
    super(menu, inventory, title);
    this.imageHeight = TunnelBoreMenu.IMAGE_HEIGHT;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
    super.renderLabels(matrixStack, mouseX, mouseY);
    this.font.draw(matrixStack, HEAD, 13, 24, IngameWindowScreen.TEXT_COLOR);
    this.font.draw(matrixStack, FUEL, 62, 24, IngameWindowScreen.TEXT_COLOR);
    this.font.draw(matrixStack, BALLAST, this.inventoryLabelX, 60, IngameWindowScreen.TEXT_COLOR);
    this.font.draw(matrixStack, TRACK, this.inventoryLabelX, 96, IngameWindowScreen.TEXT_COLOR);
  }

  @Override
  protected void renderBg(MatrixStack matrixStack, float f, int i, int j) {
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
