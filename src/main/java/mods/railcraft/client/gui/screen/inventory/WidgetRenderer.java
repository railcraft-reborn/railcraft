package mods.railcraft.client.gui.screen.inventory;

import java.util.List;
import javax.annotation.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.gui.widget.Widget;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;

public class WidgetRenderer<T extends Widget> extends GuiComponent {

  protected final T widget;

  public WidgetRenderer(T widget) {
    this.widget = widget;
  }

  public final boolean isMouseOver(double mX, double mY) {
    return mX >= this.widget.x - 1 && mX < this.widget.x + this.widget.w + 1
        && mY >= this.widget.y - 1
        && mY < this.widget.y + this.widget.h + 1;
  }

  public boolean mouseClicked(double mX, double mY, int button) {
    return false;
  }

  public void draw(RailcraftMenuScreen<?> gui, PoseStack matrixStack, int guiX, int guiY,
      int mouseX, int mouseY) {
    this.blit(matrixStack, guiX + this.widget.x, guiY + this.widget.y, this.widget.u, this.widget.v,
        this.widget.w,
        this.widget.h);
  }

  @Nullable
  public List<Component> getTooltip() {
    return null;
  }
}
