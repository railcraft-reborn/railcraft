package mods.railcraft.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;

import mods.railcraft.Railcraft;
import mods.railcraft.crafting.RollingTableContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class RollingTableScreen extends ContainerScreen<RollingTableContainer>{
  private static final ResourceLocation CRAFTING_TABLE_LOCATION = new ResourceLocation(Railcraft.ID, "textures/gui/container/rolling_table.png");
  private final RollingTableContainer ourContainer;

  public RollingTableScreen(RollingTableContainer craftingContainer, PlayerInventory userInventory, ITextComponent textCMP) {
    super(craftingContainer, userInventory, textCMP);
    this.ourContainer = craftingContainer;
  }

  @Override
  public void render(MatrixStack matrixStack, int x, int y, float something) {
    this.renderBackground(matrixStack);
    super.render(matrixStack, x, y, something);
    this.renderTooltip(matrixStack, x, y);
  }

  @Override
  protected void renderBg(MatrixStack matrixStack, float what, int when, int who) {
    this.minecraft.getTextureManager().bind(CRAFTING_TABLE_LOCATION);
    int x = this.leftPos;
    int y = this.topPos;
    // initial draw
    this.blit(matrixStack, x, y, 0, 0, this.imageWidth, this.imageHeight);
    // prog bar
    float prog = this.ourContainer.rollingProgress();
    this.blit(matrixStack, x + 89, y + 35, 176, 0, Math.round(24.00F * prog), 17); // 24*0.1, basicaly 10% of 24. Rounded for safety!
  }

}
