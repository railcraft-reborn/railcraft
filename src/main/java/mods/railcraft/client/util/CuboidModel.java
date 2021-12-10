package mods.railcraft.client.util;

import java.util.Arrays;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public class CuboidModel {

  private float minX, minY, minZ;
  private float maxX, maxY, maxZ;

  private int packedLight;
  private int packedOverlay;

  private Face[] faces = new Face[6];

  public CuboidModel() {}

  public CuboidModel(float size) {
    this.setMaxX(size);
    this.setMaxY(size);
    this.setMaxZ(size);
  }

  public CuboidModel(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
    this.setMinX(minX);
    this.setMinY(minY);
    this.setMinZ(minZ);
    this.setMaxX(maxX);
    this.setMaxY(maxY);
    this.setMaxZ(maxZ);
  }

  public CuboidModel copy() {
    CuboidModel copy =
        new CuboidModel(this.getMinX(), this.getMinY(), this.getMinZ(), this.getMaxX(),
            this.getMaxY(), this.getMaxZ());
    System.arraycopy(this.faces, 0, copy.faces, 0, this.faces.length);
    copy.setMinX(this.getMinX());
    copy.setMinY(this.getMinY());
    copy.setMinZ(this.getMinZ());
    copy.setMaxX(this.getMaxX());
    copy.setMaxY(this.getMaxY());
    copy.setMaxZ(this.getMaxZ());
    return copy;
  }

  public void disable(Direction direction) {
    Face side = this.get(direction);
    if (side != null) {
      side.enabled = false;
    }
  }

  @Nullable
  public Face get(Direction direction) {
    return this.faces[direction.ordinal()];
  }

  public void set(Direction direction, Face side) {
    this.faces[direction.ordinal()] = side;
  }

  public void setAll(Face face) {
    Arrays.fill(this.faces, face);
  }

  public void setSides(Face down, Face up, Face north, Face south,
      Face west, Face east) {
    this.faces[0] = down;
    this.faces[1] = up;
    this.faces[2] = north;
    this.faces[3] = south;
    this.faces[4] = west;
    this.faces[5] = east;
  }

  public void clear() {
    this.faces = new Face[6];
  }

  public float getMinX() {
    return this.minX;
  }

  public void setMinX(float minX) {
    this.minX = minX;
  }

  public float getMinY() {
    return this.minY;
  }

  public void setMinY(float minY) {
    this.minY = minY;
  }

  public float getMinZ() {
    return this.minZ;
  }

  public void setMinZ(float minZ) {
    this.minZ = minZ;
  }

  public float getMaxX() {
    return this.maxX;
  }

  public void setMaxX(float maxX) {
    this.maxX = maxX;
  }

  public float getMaxY() {
    return this.maxY;
  }

  public void setMaxY(float maxY) {
    this.maxY = maxY;
  }

  public float getMaxZ() {
    return this.maxZ;
  }

  public void setMaxZ(float maxZ) {
    this.maxZ = maxZ;
  }

  public int getPackedLight() {
    return this.packedLight;
  }

  public void setPackedLight(int packedLight) {
    this.packedLight = packedLight;
  }

  public int getPackedOverlay() {
    return this.packedOverlay;
  }

  public void setPackedOverlay(int packedOverlay) {
    this.packedOverlay = packedOverlay;
  }

  public final class Face {

    private TextureAtlasSprite sprite;
    private int size = 16;
    private boolean enabled = true;
    private int packedLight;
    private int packedOverlay;
    private boolean overrideLight;
    private boolean overrideOverlay;

    public TextureAtlasSprite getSprite() {
      return this.sprite;
    }

    public Face setSprite(TextureAtlasSprite sprite) {
      this.sprite = sprite;
      return this;
    }

    public int getSize() {
      return this.size;
    }

    public Face setSize(int size) {
      this.size = size;
      return this;
    }

    public boolean isEnabled() {
      return this.enabled;
    }

    public Face setEnabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    public int getPackedLight() {
      return this.overrideLight ? this.packedLight : CuboidModel.this.packedLight;
    }

    public Face setPackedLight(int packedLight) {
      this.packedLight = packedLight;
      this.overrideLight = true;
      return this;
    }

    public int getPackedOverlay() {
      return this.overrideOverlay ? this.packedOverlay : CuboidModel.this.packedOverlay;
    }

    public Face setPackedOverlay(int packedOverlay) {
      this.packedOverlay = packedOverlay;
      this.overrideOverlay = true;
      return this;
    }
  }
}
