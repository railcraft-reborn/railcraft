package mods.railcraft.client.gui.widget.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Railcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.components.Button.OnTooltip;

public class RailcraftButton extends Button {

  private static final ResourceLocation WIDGETS_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/widgets.png");

  private TexturePosition texturePosition;

  public RailcraftButton(int x, int y, int width, int height,
      Component text, OnPress actionListener, TexturePosition texturePosition) {
    super(x, y, width, height, text, actionListener);
    this.texturePosition = texturePosition;
  }

  public RailcraftButton(int x, int y, int width, int height, Component text,
      OnPress actionListener, OnTooltip tooltip, TexturePosition texturePosition) {
    super(x, y, width, height, text, actionListener, tooltip);
    this.texturePosition = texturePosition;
  }

  public void setTexturePosition(TexturePosition texturePosition) {
    this.texturePosition = texturePosition;
  }

  @Override
  public void renderButton(PoseStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    Minecraft minecraft = Minecraft.getInstance();
    Font fontrenderer = minecraft.font;
    RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
    int i = this.getYImage(this.isHoveredOrFocused());
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
    RenderSystem.enableDepthTest();

    int xOffset = this.texturePosition.getX();
    int yOffset = this.texturePosition.getY();
    int h = this.texturePosition.getHeight();
    int w = this.texturePosition.getWidth();

    this.blit(matrixStack, this.x, this.y, xOffset, yOffset + i * h, this.width / 2, h);
    this.blit(matrixStack, this.x + this.width / 2, this.y, xOffset + w - this.width / 2,
        yOffset + i * h, this.width / 2, h);
    this.renderBg(matrixStack, minecraft, mouseX, mouseY);
    int j = getFGColor();
    drawCenteredString(matrixStack, fontrenderer, this.getMessage(), this.x + this.width / 2,
        this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
  }
}
