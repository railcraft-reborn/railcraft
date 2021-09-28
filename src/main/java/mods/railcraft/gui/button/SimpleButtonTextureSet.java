package mods.railcraft.gui.button;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class SimpleButtonTextureSet implements ButtonTextureSet {

  private final int x, y, height, width;

  public SimpleButtonTextureSet(int x, int y, int height, int width) {
    this.x = x;
    this.y = y;
    this.height = height;
    this.width = width;
  }

  @Override
  public int getX() {
    return x;
  }

  @Override
  public int getY() {
    return y;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public int getWidth() {
    return width;
  }
}
