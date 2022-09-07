package mods.railcraft.client.gui.screen.inventory;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.world.inventory.RailcraftMenu;
import mods.railcraft.world.inventory.slot.RailcraftSlot;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
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
  public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(poseStack);
    super.render(poseStack, mouseX, mouseY, partialTicks);
    var left = this.leftPos;
    var top = this.topPos;

    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

    if (this.menu.getCarried().isEmpty()) {
      for (var renderer : this.widgetRenderers) {
        if (!renderer.widget.hidden) {
          var tooltip = renderer.getTooltip();
          if (tooltip != null && renderer.isMouseOver(mouseX - left, mouseY - top)) {
            this.renderComponentTooltip(poseStack, tooltip, mouseX, mouseY, this.font);
          }
        }
      }

      for (var slot : this.menu.slots) {
        if (slot instanceof RailcraftSlot railcraftSlot && slot.getItem().isEmpty()) {
          var tooltip = railcraftSlot.getTooltip();
          if (tooltip != null && this.isMouseOverSlot(slot, mouseX, mouseY)) {
            this.renderComponentTooltip(poseStack, tooltip, mouseX, mouseY, this.font);
          }
        }
      }
    }

    this.renderTooltip(poseStack, mouseX, mouseY);
  }

  public abstract ResourceLocation getWidgetsTexture();

  @Override
  protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, this.getWidgetsTexture());

    int x = (this.width - this.getXSize()) / 2;
    int y = (this.height - this.getYSize()) / 2;

    this.blit(poseStack, x, y, 0, 0, this.getXSize(), this.getYSize());

    int relativeMouseX = mouseX - this.leftPos;
    int relativeMouseY = mouseY - this.topPos;

    for (var renderer : this.widgetRenderers) {
      if (!renderer.widget.hidden) {
        renderer.render(this, poseStack, x, y, relativeMouseX, relativeMouseY);
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
    if (button == GLFW.GLFW_MOUSE_BUTTON_1 && slot instanceof RailcraftSlot
        && ((RailcraftSlot) slot).isPhantom())
      return true;
    return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
  }
}
