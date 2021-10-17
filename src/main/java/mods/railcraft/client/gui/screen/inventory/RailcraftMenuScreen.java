package mods.railcraft.client.gui.screen.inventory;

import java.util.ArrayList;
import java.util.List;
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

  protected RailcraftMenuScreen(T menu, PlayerInventory inventory,
      ITextComponent title) {
    super(menu, inventory, title);
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
        if (!element.widget.hidden) {
          List<? extends ITextProperties> tooltip = element.getTooltip();
          if (tooltip != null && element.isMouseOver(mouseX - left, mouseY - top)) {
            this.renderWrappedToolTip(matrixStack, tooltip, mouseX, mouseY, this.font);
          }
        }
      }

      for (Slot slot : this.menu.slots) {
        if (slot instanceof SlotRailcraft && slot.getItem().isEmpty()) {
          List<? extends ITextProperties> tooltip = ((SlotRailcraft) slot).getTooltip();
          if (tooltip != null && this.isMouseOverSlot(slot, mouseX, mouseY)) {
            this.renderWrappedToolTip(matrixStack, tooltip, mouseX, mouseY, this.font);
          }
        }
      }
    }

    this.renderTooltip(matrixStack, mouseX, mouseY);
  }

  public abstract ResourceLocation getWidgetsTexture();

  @SuppressWarnings("deprecation")
  @Override
  protected void renderBg(MatrixStack matrixStack, float f, int mouseX, int mouseY) {
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.bindTexture(this.getWidgetsTexture());

    int x = (width - this.getXSize()) / 2;
    int y = (height - this.getYSize()) / 2;

    this.blit(matrixStack, x, y, 0, 0, this.getXSize(), this.getYSize());

    int relativeMouseX = mouseX - this.leftPos;
    int relativeMouseY = mouseY - this.topPos;

    for (WidgetRenderer<?> element : this.widgetRenderers) {
      if (!element.widget.hidden) {
        element.draw(this, matrixStack, x, y, relativeMouseX, relativeMouseY);
      }
    }
  }

  /**
   * Returns if the passed mouse position is over the specified slot.
   */
  private boolean isMouseOverSlot(Slot slot, int mouseX, int mouseY) {
    mouseX -= this.leftPos;
    mouseY -= this.topPos;
    return mouseX >= slot.x - 1 && mouseX < slot.x + 16 + 1 && mouseY >= slot.y - 1
        && mouseY < slot.y + 16 + 1;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    double relativeMouseX = mouseX - this.leftPos;
    double relativeMouseY = mouseY - this.topPos;

    if (this.widgetRenderers.stream()
        .filter(element -> !element.widget.hidden)
        .filter(element -> element.isMouseOver(relativeMouseX, relativeMouseY))
        .anyMatch(element -> element.mouseClicked(relativeMouseX, relativeMouseY, button))) {
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
}
