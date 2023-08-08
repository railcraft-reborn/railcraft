package mods.railcraft.client.gui.widget.button;

public class SimpleTexturePosition implements TexturePosition {

  private final int x, y, height, width;

  public SimpleTexturePosition(int x, int y, int height, int width) {
    this.x = x;
    this.y = y;
    this.height = height;
    this.width = width;
  }

  @Override
  public int getX() {
    return this.x;
  }

  @Override
  public int getY() {
    return this.y;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public int getWidth() {
    return this.width;
  }
}
