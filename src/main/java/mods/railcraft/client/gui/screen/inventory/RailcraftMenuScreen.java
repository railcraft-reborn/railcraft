package mods.railcraft.client.gui.screen.inventory;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.GLFW;
import mods.railcraft.world.inventory.RailcraftMenu;
import mods.railcraft.world.inventory.slot.RailcraftSlot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public abstract class RailcraftMenuScreen<T extends RailcraftMenu>
    extends AbstractContainerScreen<T> {

  private final List<WidgetRenderer<?>> widgetRenderers = new ArrayList<>();

  protected final Inventory inventory;

  protected void registerWidgetRenderer(WidgetRenderer<?> renderer) {
    this.widgetRenderers.add(renderer);
  }

  protected RailcraftMenuScreen(T menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.inventory = inventory;
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    super.render(guiGraphics, mouseX, mouseY, partialTicks);
    var left = this.leftPos;
    var top = this.topPos;

    if (this.menu.getCarried().isEmpty()) {
      for (var renderer : this.widgetRenderers) {
        if (!renderer.widget.hidden) {
          var tooltip = renderer.getTooltip();
          if (!tooltip.isEmpty() && renderer.isMouseOver(mouseX - left, mouseY - top)) {
            guiGraphics.renderTooltip(
                this.font,
                tooltip,
                mouseX,
                mouseY,
                DefaultTooltipPositioner.INSTANCE,
                null);
          }
        }
      }

      for (var slot : this.menu.slots) {
        if (slot instanceof RailcraftSlot railcraftSlot && slot.getItem().isEmpty()) {
          var tooltip = railcraftSlot.getTooltip();
          if (tooltip != null && this.isMouseOverSlot(slot, mouseX, mouseY)) {
            guiGraphics.renderTooltip(
                this.font,
                tooltip,
                mouseX,
                mouseY,
                DefaultTooltipPositioner.INSTANCE,
                null);
          }
        }
      }
    }

    this.renderTooltip(guiGraphics, mouseX, mouseY);
  }

  public abstract ResourceLocation getWidgetsTexture();

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    int x = (this.width - this.getXSize()) / 2;
    int y = (this.height - this.getYSize()) / 2;

    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, getWidgetsTexture(), x, y, 0, 0, this.getXSize(),
        this.getYSize(), 256, 256);

    int relativeMouseX = mouseX - this.leftPos;
    int relativeMouseY = mouseY - this.topPos;

    for (var renderer : this.widgetRenderers) {
      if (!renderer.widget.hidden) {
        renderer.render(getWidgetsTexture(), guiGraphics, x, y, relativeMouseX, relativeMouseY);
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
  public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
    double relativeMouseX = event.x()- this.leftPos;
    double relativeMouseY = event.y() - this.topPos;

    if (this.widgetRenderers.stream()
        .filter(element -> !element.widget.hidden)
        .filter(element -> element.isMouseOver(relativeMouseX, relativeMouseY))
        .anyMatch(element -> element.mouseClicked(relativeMouseX, relativeMouseY, event.button()))) {
      return true;
    }

    return super.mouseClicked(event, doubleClick);
  }

  @Override
  public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
    Slot slot = this.getSlotUnderMouse();
    if (event.button() == GLFW.GLFW_MOUSE_BUTTON_1 && slot instanceof RailcraftSlot railcraftSlot
        && railcraftSlot.isPhantom())
      return true;
    return super.mouseDragged(event, deltaX, deltaY);
  }
}
