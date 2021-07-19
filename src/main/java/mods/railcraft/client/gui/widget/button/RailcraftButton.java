package mods.railcraft.client.gui.widget.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mods.railcraft.Railcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class RailcraftButton extends Button {

  private static final ResourceLocation WIDGETS_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/widgets.png");

  private final ITexturePosition texture;

  public RailcraftButton(int x, int y, int width, int height,
      ITextComponent text, IPressable actionListener, ITexturePosition texture) {
    super(x, y, width, height, text, actionListener);
    this.texture = texture;
  }

  public RailcraftButton(int x, int y, int width, int height, ITextComponent text,
      IPressable actionListener, ITooltip tooltip, ITexturePosition texture) {
    super(x, y, width, height, text, actionListener, tooltip);
    this.texture = texture;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    Minecraft minecraft = Minecraft.getInstance();
    FontRenderer fontrenderer = minecraft.font;
    minecraft.getTextureManager().bind(WIDGETS_LOCATION);
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
    int i = this.getYImage(this.isHovered());
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
    RenderSystem.enableDepthTest();

    int xOffset = texture.getX();
    int yOffset = texture.getY();
    int h = texture.getHeight();
    int w = texture.getWidth();

    this.blit(matrixStack, x, y, xOffset, yOffset + i * h, width / 2, h);
    this.blit(
        matrixStack, x + width / 2, y, xOffset + w - width / 2, yOffset + i * h, width / 2, h);
    this.renderBg(matrixStack, minecraft, mouseX, mouseY);
    int j = getFGColor();
    drawCenteredString(matrixStack, fontrenderer, this.getMessage(), this.x + this.width / 2,
        this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
  }
}
