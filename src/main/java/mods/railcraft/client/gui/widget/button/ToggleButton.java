package mods.railcraft.client.gui.widget.button;

import net.minecraft.network.chat.Component;

public class ToggleButton extends RailcraftButton {

  private boolean toggled;

  public ToggleButton(int x, int y, int width, int height, Component text,
      OnPress actionListener, TexturePosition texturePosition, boolean toggled) {
    super(x, y, width, height, text, actionListener, texturePosition);
    this.toggled = toggled;
  }

  public ToggleButton(int x, int y, int width, int height, Component text,
      OnPress actionListener, OnTooltip tooltip, TexturePosition texturePosition,
      boolean toggled) {
    super(x, y, width, height, text, actionListener, tooltip, texturePosition);
    this.toggled = toggled;
  }

  public void toggle() {
    this.setToggled(!this.toggled);
  }

  public void setToggled(boolean toggled) {
    this.toggled = toggled;
  }

  @Override
  public void onPress() {
    this.toggle();
    super.onPress();
  }

  @Override
  protected int getYImage(boolean hovered) {
    if (this.toggled) {
      return 3;
    } else {
      return super.getYImage(hovered);
    }
  }
}
