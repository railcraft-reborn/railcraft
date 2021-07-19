package mods.railcraft.client.gui.widget.button;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class GuiToggleButton extends Button {

  public GuiToggleButton(int x, int y, ITextComponent label,
      Button.IPressable actionListener, Button.ITooltip tooltip) {
    this(x, y, 200, label, actionListener, tooltip);
  }

  public GuiToggleButton(int x, int y, int width, ITextComponent label,
      Button.IPressable actionListener, Button.ITooltip tooltip) {
    super(x, y, width, 20, label, actionListener, tooltip);
  }

  // public GuiToggleButton(int id, int x, int y, int width, IButtonTextureSet texture, String s,
  // boolean active) {
  // super(id, x, y, width, texture, s);
  // this.active = active;
  // }

  public void toggle() {
    this.active = !this.active;
  }
}
