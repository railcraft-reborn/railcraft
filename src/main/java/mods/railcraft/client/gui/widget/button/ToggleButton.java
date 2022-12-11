package mods.railcraft.client.gui.widget.button;

import net.minecraft.network.chat.Component;

public class ToggleButton extends RailcraftButton {

  private boolean toggled;

  private ToggleButton(Builder builder) {
    super(builder);
    this.toggled = builder.toggled;
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

  public static Builder toggleBuilder(Component message, OnPress onPress,
      TexturePosition texturePosition) {
    return new Builder(message, onPress, texturePosition);
  }

  public static class Builder extends AbstractBuilder<Builder, ToggleButton> {

    private boolean toggled;

    public Builder(Component message, OnPress onPress, TexturePosition texturePosition) {
      super(ToggleButton::new, message, onPress, texturePosition);
    }

    public Builder toggled(boolean toggled) {
      this.toggled = toggled;
      return this;
    }
  }
}
