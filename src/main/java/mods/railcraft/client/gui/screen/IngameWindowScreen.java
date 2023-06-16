package mods.railcraft.client.gui.screen;

import mods.railcraft.Railcraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

public class IngameWindowScreen extends Screen {

  public static final ResourceLocation WIDGETS_TEXTURE =
      new ResourceLocation(Railcraft.ID, "textures/gui/widgets.png");
  public static final ResourceLocation LARGE_WINDOW_TEXTURE =
      new ResourceLocation(Railcraft.ID, "textures/gui/large_window.png");

  public static final int TEXT_COLOR = 0xFF404040;
  public static final int DEFAULT_WINDOW_WIDTH = 176;
  public static final int DEFAULT_WINDOW_HEIGHT = 88;
  public static final int LARGE_WINDOW_HEIGHT = 113;

  protected final int windowWidth;
  protected final int windowHeight;
  protected final ResourceLocation backgroundTexture;

  protected IngameWindowScreen(Component title) {
    this(title, WIDGETS_TEXTURE, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
  }

  protected IngameWindowScreen(Component title, ResourceLocation backgroundTexture,
      int windowWidth, int windowHeight) {
    super(title);
    this.windowWidth = windowWidth;
    this.windowHeight = windowHeight;
    this.backgroundTexture = backgroundTexture;
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(guiGraphics);
    int centredX = (this.width - this.windowWidth) / 2;
    int centredY = (this.height - this.windowHeight) / 2;
    guiGraphics.blit(this.backgroundTexture, centredX, centredY, 0, 0,
        this.windowWidth, this.windowHeight);
    var poseStack = guiGraphics.pose();
    poseStack.pushPose();
    poseStack.translate(centredX, centredY, 0.0F);
    this.drawCenteredString(guiGraphics, this.title, this.windowWidth / 2, this.font.lineHeight);
    this.renderContent(guiGraphics, mouseX, mouseY, partialTicks);
    poseStack.popPose();
    super.render(guiGraphics, mouseX, mouseY, partialTicks);
  }

  protected void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY,
      float partialTicks) {}


  @Override
  public void tick() {
    super.tick();
    if (!this.minecraft.player.isAlive() || this.minecraft.player.isDeadOrDying()) {
      this.onClose();
    }
  }

  public void drawCenteredString(GuiGraphics guiGraphics, Component text, float x, float y) {
    FormattedCharSequence orderedText = text.getVisualOrderText();
    guiGraphics.drawString(this.font, orderedText, x - (float) this.font.width(orderedText) / 2, y,
        TEXT_COLOR, false);
  }

  public void drawCenteredString(GuiGraphics guiGraphics, Component component, int y, boolean shadow) {
    drawCenteredString(guiGraphics, component, windowWidth, y, shadow);
  }

  public void drawCenteredString(GuiGraphics guiGraphics, Component component, int width, int y,
      boolean shadow) {
    int length = font.width(component);
    int x = width / 2 - length / 2;
    guiGraphics.drawString(font, component, x, y, TEXT_COLOR, shadow);
  }
}
