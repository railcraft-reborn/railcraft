package mods.railcraft.client.gui.screen.inventory;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mods.railcraft.world.inventory.RailcraftMenu;
import mods.railcraft.world.inventory.SlotRailcraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;

public abstract class RailcraftMenuScreen<T extends RailcraftMenu>
    extends ContainerScreen<T> {

  private final List<WidgetRenderer<?>> widgetRenderers = new ArrayList<>();

  protected void registerWidgetRenderer(WidgetRenderer<?> renderer) {
    this.widgetRenderers.add(renderer);
  }

  protected RailcraftMenuScreen(T container, PlayerInventory playerInventory,
      ITextComponent title) {
    super(container, playerInventory, title);
  }

  /**
   * Draws the screen and all the components in it.
   */
  @SuppressWarnings("deprecation")
  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float par3) {
    this.renderBackground(matrixStack);
    super.render(matrixStack, mouseX, mouseY, par3);
    int left = this.leftPos;
    int top = this.topPos;

    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

    if (this.inventory.getCarried().isEmpty()) {
      for (WidgetRenderer<?> element : this.widgetRenderers) {
        if (element.widget.hidden)
          continue;
        List<? extends ITextProperties> tips = element.getTooltip();
        if (tips == null)
          continue;
        if (element.isMouseOver(mouseX - left, mouseY - top)) {
          this.renderWrappedToolTip(matrixStack, tips, mouseX, mouseY, this.font);
        }
      }

      for (Object obj : this.menu.slots) {
        if (!(obj instanceof SlotRailcraft))
          continue;
        SlotRailcraft slot = (SlotRailcraft) obj;
        if (!slot.getItem().isEmpty())
          continue;
        List<? extends ITextProperties> tips = slot.getTooltip();
        if (tips == null)
          continue;
        if (this.isMouseOverSlot(slot, mouseX, mouseY)) {
          this.renderWrappedToolTip(matrixStack, tips, mouseX, mouseY, this.font);
        }
      }
    }

    this.renderTooltip(matrixStack, mouseX, mouseY);
  }

  public abstract ResourceLocation getTextureLocation();

  @SuppressWarnings("deprecation")
  @Override
  protected void renderBg(MatrixStack matrixStack, float f, int mouseX, int mouseY) {
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    bindTexture(this.getTextureLocation());

    int x = (width - this.getXSize()) / 2;
    int y = (height - this.getYSize()) / 2;

    drawGuiBackground(matrixStack, x, y);

    int mX = mouseX - this.leftPos;
    int mY = mouseY - this.topPos;

    for (WidgetRenderer<?> element : this.widgetRenderers) {
      if (element.widget.hidden)
        continue;
      element.draw(this, matrixStack, x, y, mX, mY);
    }
  }

  protected void drawGuiBackground(MatrixStack matrixStack, int x, int y) {
    this.blit(matrixStack, x, y, 0, 0, this.getXSize(), this.getYSize());
  }

  private @Nullable Slot getSlotAtPosition(int par1, int par2) {
    return this.menu.slots.stream()
        .filter(var4 -> isMouseOverSlot(var4, par1, par2))
        .findFirst().orElse(null);
  }

  /**
   * Returns if the passed mouse position is over the specified slot.
   */
  private boolean isMouseOverSlot(Slot par1Slot, int par2, int par3) {
    int var4 = this.leftPos;
    int var5 = this.topPos;
    par2 -= var4;
    par3 -= var5;
    return par2 >= par1Slot.x - 1 && par2 < par1Slot.x + 16 + 1 && par3 >= par1Slot.y - 1
        && par3 < par1Slot.y + 16 + 1;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    double mX = mouseX - this.leftPos;
    double mY = mouseY - this.topPos;

    if (this.widgetRenderers.stream()
        .filter(element -> !element.widget.hidden)
        .filter(element -> element.isMouseOver(mX, mY))
        .anyMatch(element -> element.mouseClicked(mX, mY, button))) {
      return true;
    }

    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX,
      double deltaY) {
    Slot slot = this.getSlotUnderMouse();
    if (button == GLFW.GLFW_MOUSE_BUTTON_1 && slot instanceof SlotRailcraft
        && ((SlotRailcraft) slot).isPhantom())
      return true;
    return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
  }

  public void bindTexture(ResourceLocation texture) {
    this.minecraft.textureManager.bind(texture);
  }

  // public void drawTexture(int x, int y, int w, int h, float uMin, float vMin, float uMax, float
  // vMax) {
  // Tessellator tessellator = Tessellator.instance();
  // WorldRenderer wr = tessellator.getWorldRenderer();
  // wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
  // wr.pos(x + 0, y + h, zLevel).tex(uMin, vMax).endVertex();
  // wr.pos(x + w, y + h, zLevel).tex(uMax, vMax).endVertex();
  // wr.pos(x + w, y + 0, zLevel).tex(uMax, vMin).endVertex();
  // wr.pos(x + 0, y + 0, zLevel).tex(uMin, vMin).endVertex();
  // }

}
