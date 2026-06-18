package mods.railcraft.client.gui.screen.inventory;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.GLFW;
import mods.railcraft.world.inventory.RailcraftMenu;
import mods.railcraft.world.inventory.slot.RailcraftSlot;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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

  protected RailcraftMenuScreen(T menu, Inventory inventory, Component title, int imageWidth,
      int imageHeight) {
    super(menu, inventory, title, imageWidth, imageHeight);
    this.inventory = inventory;
  }

  protected RailcraftMenuScreen(T menu, Inventory inventory, Component title, int imageHeight) {
    this(menu, inventory, title, 176, imageHeight);
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
    super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
    var left = this.leftPos;
    var top = this.topPos;

    if (this.menu.getCarried().isEmpty()) {
      for (var renderer : this.widgetRenderers) {
        if (!renderer.widget.hidden) {
          var tooltip = renderer.getTooltip();
          if (!tooltip.isEmpty() && renderer.isMouseOver(mouseX - left, mouseY - top)) {
            graphics.tooltip(
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
            graphics.tooltip(
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

    this.extractTooltip(graphics, mouseX, mouseY);
  }

  public abstract Identifier getWidgetsTexture();

  @Override
  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
    super.extractBackground(graphics, mouseX, mouseY, partialTicks);
    int x = (this.width - this.getImageWidth()) / 2;
    int y = (this.height - this.getImageHeight()) / 2;

    graphics.blit(RenderPipelines.GUI_TEXTURED, getWidgetsTexture(), x, y, 0, 0, this.getImageWidth(),
        this.getImageHeight(), 256, 256);

    int relativeMouseX = mouseX - this.leftPos;
    int relativeMouseY = mouseY - this.topPos;

    for (var renderer : this.widgetRenderers) {
      if (!renderer.widget.hidden) {
        renderer.render(getWidgetsTexture(), graphics, x, y, relativeMouseX, relativeMouseY);
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
    Slot slot = this.getHoveredSlot();
    if (event.button() == GLFW.GLFW_MOUSE_BUTTON_1 && slot instanceof RailcraftSlot railcraftSlot
        && railcraftSlot.isPhantom())
      return true;
    return super.mouseDragged(event, deltaX, deltaY);
  }
}
