package mods.railcraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.Railcraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BasicIngameScreen extends Screen {

  public static final ResourceLocation WIDGETS =
      new ResourceLocation(Railcraft.ID, "textures/gui/widgets.png");
  public static final ResourceLocation LARGE_BASIC_BACKGROUND =
      new ResourceLocation(Railcraft.ID, "textures/gui/large_basic_background.png");

  public static final int TEXT_COLOR = 0xFF404040;

  protected final int x;
  protected final int y;
  protected final ResourceLocation backgroundTexture;

  protected BasicIngameScreen(ITextComponent title) {
    this(title, WIDGETS, 176, 88);
  }

  protected BasicIngameScreen(ITextComponent title, ResourceLocation backgroundTexture, int x,
      int y) {
    super(title);
    this.x = x;
    this.y = y;
    this.backgroundTexture = backgroundTexture;
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(matrixStack);
    int centredX = (this.width - this.x) / 2;
    int centredY = (this.height - this.y) / 2;
    this.minecraft.getTextureManager().bind(this.backgroundTexture);
    this.blit(matrixStack, centredX, centredY, 0, 0, this.x, this.y);
    matrixStack.pushPose();
    {
      matrixStack.translate(centredX, centredY, 0.0F);
      this.drawCenteredString(matrixStack, this.title, this.x / 2, this.font.lineHeight);
      this.renderContent(matrixStack, mouseX, mouseY, partialTicks);
    }
    matrixStack.popPose();
    super.render(matrixStack, mouseX, mouseY, partialTicks);
  }

  protected void renderContent(MatrixStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {}

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (super.keyPressed(keyCode, scanCode, modifiers)) {
      return true;
    }
    if (this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
      this.onClose();
      return true;
    }
    return false;
  }

  @Override
  public void tick() {
    super.tick();
    if (!this.minecraft.player.isAlive() || this.minecraft.player.isDeadOrDying()) {
      this.onClose();
    }
  }

  public void drawCenteredString(MatrixStack matrixStack, ITextComponent text, float x, float y) {
    IReorderingProcessor orderedText = text.getVisualOrderText();
    this.font.draw(matrixStack, orderedText, x - this.font.width(orderedText) / 2, y, TEXT_COLOR);
  }
}
