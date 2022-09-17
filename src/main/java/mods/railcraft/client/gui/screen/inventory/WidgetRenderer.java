package mods.railcraft.client.gui.screen.inventory;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.gui.widget.Widget;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;

public class WidgetRenderer<T extends Widget> extends GuiComponent {

  protected final T widget;

  public WidgetRenderer(T widget) {
    this.widget = widget;
  }

  public final boolean isMouseOver(double mouseX, double mouseY) {
    return mouseX >= this.widget.x - 1 && mouseX < this.widget.x + this.widget.w + 1
        && mouseY >= this.widget.y - 1
        && mouseY < this.widget.y + this.widget.h + 1;
  }

  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    return false;
  }

  public void render(RailcraftMenuScreen<?> screen, PoseStack poseStack, int centreX, int centreY,
      int mouseX, int mouseY) {
    this.blit(poseStack, centreX + this.widget.x, centreY + this.widget.y, this.widget.u,
        this.widget.v, this.widget.w, this.widget.h);
  }

  @Nullable
  public List<Component> getTooltip() {
    return null;
  }
}
