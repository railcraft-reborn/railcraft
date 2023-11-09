package mods.railcraft.client.util;

import java.util.Arrays;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mods.railcraft.client.util.CuboidModel.Face;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

/**
 * Adapted from Mantle's FluidRenderer and Tinker's SmelteryTankRenderer
 */
public class CuboidModelRenderer {

  /**
   * Used to not need to create multiple arrays when we just want to fill it differently at times,
   * and given rendering TERs is not multithreaded it is perfectly safe to just use one backing
   * "temporary" array.
   */
  private static final int[] combinedARGB = new int[Direction.values().length];
  private static final Vector3f NORMAL = new Vector3f(1, 1, 1);
  private static final Vector3f YP = new Vector3f(0, 1, 0);

  static {
    NORMAL.normalize();
  }

  private CuboidModelRenderer() {}

  public static void render(CuboidModel model, PoseStack matrix, VertexConsumer buffer, int argb,
      FaceDisplay faceDisplay, boolean fakeDisableDiffuse) {
    Arrays.fill(combinedARGB, argb);
    render(model, matrix, buffer, combinedARGB, faceDisplay, fakeDisableDiffuse);
  }

  /**
   * @implNote Based off of Tinker's
   */
  public static void render(CuboidModel model, PoseStack matrix, VertexConsumer buffer,
      int[] colors, FaceDisplay faceDisplay, boolean fakeDisableDiffuse) {
    float xShift = Mth.floor(model.getMinX());
    float yShift = Mth.floor(model.getMinY());
    float zShift = Mth.floor(model.getMinZ());
    matrix.pushPose();
    matrix.translate(xShift, yShift, zShift);
    float minX = model.getMinX() - xShift;
    float minY = model.getMinY() - yShift;
    float minZ = model.getMinZ() - zShift;
    float maxX = model.getMaxX() - xShift;
    float maxY = model.getMaxY() - yShift;
    float maxZ = model.getMaxZ() - zShift;
    int xDelta = calculateDelta(minX, maxX);
    int yDelta = calculateDelta(minY, maxY);
    int zDelta = calculateDelta(minZ, maxZ);
    float[] xBounds = getBlockBounds(xDelta, minX, maxX);
    float[] yBounds = getBlockBounds(yDelta, minY, maxY);
    float[] zBounds = getBlockBounds(zDelta, minZ, maxZ);
    PoseStack.Pose lastMatrix = matrix.last();
    Matrix4f matrix4f = lastMatrix.pose();
    Matrix3f normalMatrix = lastMatrix.normal();
    Vector3f normal = fakeDisableDiffuse ? NORMAL : YP;
    Vector3f from = new Vector3f();
    Vector3f to = new Vector3f();
    // render each side
    for (int y = 0; y <= yDelta; y++) {
      Face upSprite = y == yDelta ? model.get(Direction.UP) : null;
      Face downSprite = y == 0 ? model.get(Direction.DOWN) : null;
      from.y = yBounds[y];
      to.y = yBounds[y + 1];
      for (int z = 0; z <= zDelta; z++) {
        Face northSprite = z == 0 ? model.get(Direction.NORTH) : null;
        Face southSprite = z == zDelta ? model.get(Direction.SOUTH) : null;
        from.z = zBounds[z];
        to.z = zBounds[z + 1];
        for (int x = 0; x <= xDelta; x++) {
          Face westSprite = x == 0 ? model.get(Direction.WEST) : null;
          Face eastSprite = x == xDelta ? model.get(Direction.EAST) : null;
          // Set bounds
          from.x = xBounds[x];
          to.x = xBounds[x + 1];
          putTexturedQuad(buffer, matrix4f, normalMatrix, westSprite, from, to, Direction.WEST,
              colors, faceDisplay, normal);
          putTexturedQuad(buffer, matrix4f, normalMatrix, eastSprite, from, to, Direction.EAST,
              colors, faceDisplay, normal);
          putTexturedQuad(buffer, matrix4f, normalMatrix, northSprite, from, to, Direction.NORTH,
              colors, faceDisplay, normal);
          putTexturedQuad(buffer, matrix4f, normalMatrix, southSprite, from, to, Direction.SOUTH,
              colors, faceDisplay, normal);
          putTexturedQuad(buffer, matrix4f, normalMatrix, upSprite, from, to, Direction.UP, colors,
              faceDisplay, normal);
          putTexturedQuad(buffer, matrix4f, normalMatrix, downSprite, from, to, Direction.DOWN,
              colors, faceDisplay, normal);
        }
      }
    }
    matrix.popPose();
  }

  /**
   * @implNote From Tinker's
   */
  private static float[] getBlockBounds(int delta, float start, float end) {
    float[] bounds = new float[2 + delta];
    bounds[0] = start;
    int offset = (int) start;
    for (int i = 1; i <= delta; i++) {
      bounds[i] = i + offset;
    }
    bounds[delta + 1] = end;
    return bounds;
  }

  /**
   * @implNote From Tinker's
   */
  private static int calculateDelta(float min, float max) {
    // The texture can stretch over more blocks than the subtracted height is if min's decimal is
    // bigger than max's decimal (causing UV over 1)
    // ignoring the decimals prevents this, as yd then equals exactly how many ints are between the
    // two
    // for example, if max = 5.1 and min = 2.3, 2.8 (which rounds to 2), with the face array
    // becoming 2.3, 3, 4, 5.1
    int delta = (int) (max - (int) min);
    // except in the rare case of max perfectly aligned with the block, causing the top face to
    // render multiple times
    // for example, if max = 3 and min = 1, the values of the face array become 1, 2, 3, 3 as we
    // then have middle ints
    if (max % 1d == 0) {
      delta--;
    }
    return delta;
  }

  /**
   * @implNote From Mantle with some adjustments
   */
  private static void putTexturedQuad(VertexConsumer buffer, Matrix4f matrix, Matrix3f normalMatrix,
      @Nullable Face spriteInfo, Vector3f from, Vector3f to,
      Direction face, int[] colors, FaceDisplay faceDisplay,
      Vector3f normal) {
    if (spriteInfo == null) {
      return;
    }
    // start with texture coordinates
    float x1 = from.x(), y1 = from.y(), z1 = from.z();
    float x2 = to.x(), y2 = to.y(), z2 = to.z();
    // choose UV based on opposite two axis
    float u1, u2, v1, v2;
    switch (face.getAxis()) {
      default:
      case Y:
        u1 = x1;
        u2 = x2;
        v1 = z2;
        v2 = z1;
        break;
      case Z:
        u1 = x2;
        u2 = x1;
        v1 = y1;
        v2 = y2;
        break;
      case X:
        u1 = z2;
        u2 = z1;
        v1 = y1;
        v2 = y2;
        break;
    }

    // wrap UV to be between 0 and 1, assumes none of the positions lie outside the 0,0,0 to 1,1,1
    // range
    // however, one of them might be exactly on the 1.0 bound, that one should be set to 1 instead
    // of left at 0
    boolean bigger = u1 > u2;
    u1 = u1 % 1;
    u2 = u2 % 1;
    if (bigger) {
      if (u1 == 0) {
        u1 = 1;
      }
    } else if (u2 == 0) {
      u2 = 1;
    }
    bigger = v1 > v2;
    v1 = v1 % 1;
    v2 = v2 % 1;
    if (bigger) {
      if (v1 == 0) {
        v1 = 1;
      }
    } else if (v2 == 0) {
      v2 = 1;
    }

    // Flip V
    float temp = v1;
    v1 = 1f - v2;
    v2 = 1f - temp;

    float minU = spriteInfo.getSprite().getU(u1);
    float maxU = spriteInfo.getSprite().getU(u2);
    float minV = spriteInfo.getSprite().getV(v1);
    float maxV = spriteInfo.getSprite().getV(v2);
    int argb = colors[face.ordinal()];
    float red = RenderUtil.getRed(argb);
    float green = RenderUtil.getGreen(argb);
    float blue = RenderUtil.getBlue(argb);
    float alpha = RenderUtil.getAlpha(argb);
    // add quads
    switch (face) {
      case DOWN:
        drawFace(buffer, matrix, normalMatrix, red, green, blue, alpha, minU, maxU, minV, maxV,
            spriteInfo.getPackedLight(), spriteInfo.getPackedOverlay(), faceDisplay, normal,
            x1, y1, z2,
            x1, y1, z1,
            x2, y1, z1,
            x2, y1, z2);
        break;
      case UP:
        drawFace(buffer, matrix, normalMatrix, red, green, blue, alpha, minU, maxU, minV, maxV,
            spriteInfo.getPackedLight(), spriteInfo.getPackedOverlay(), faceDisplay, normal,
            x1, y2, z1,
            x1, y2, z2,
            x2, y2, z2,
            x2, y2, z1);
        break;
      case NORTH:
        drawFace(buffer, matrix, normalMatrix, red, green, blue, alpha, minU, maxU, minV, maxV,
            spriteInfo.getPackedLight(), spriteInfo.getPackedOverlay(), faceDisplay, normal,
            x1, y1, z1,
            x1, y2, z1,
            x2, y2, z1,
            x2, y1, z1);
        break;
      case SOUTH:
        drawFace(buffer, matrix, normalMatrix, red, green, blue, alpha, minU, maxU, minV, maxV,
            spriteInfo.getPackedLight(), spriteInfo.getPackedOverlay(), faceDisplay, normal,
            x2, y1, z2,
            x2, y2, z2,
            x1, y2, z2,
            x1, y1, z2);
        break;
      case WEST:
        drawFace(buffer, matrix, normalMatrix, red, green, blue, alpha, minU, maxU, minV, maxV,
            spriteInfo.getPackedLight(), spriteInfo.getPackedOverlay(), faceDisplay, normal,
            x1, y1, z2,
            x1, y2, z2,
            x1, y2, z1,
            x1, y1, z1);
        break;
      case EAST:
        drawFace(buffer, matrix, normalMatrix, red, green, blue, alpha, minU, maxU, minV, maxV,
            spriteInfo.getPackedLight(), spriteInfo.getPackedOverlay(), faceDisplay, normal,
            x2, y1, z1,
            x2, y2, z1,
            x2, y2, z2,
            x2, y1, z2);
        break;
    }
  }

  private static void drawFace(VertexConsumer buffer, Matrix4f matrix, Matrix3f normalMatrix,
      float red, float green, float blue, float alpha, float minU, float maxU,
      float minV, float maxV, int light, int overlay, FaceDisplay faceDisplay, Vector3f normal,
      float x1, float y1, float z1,
      float x2, float y2, float z2,
      float x3, float y3, float z3,
      float x4, float y4, float z4) {
    if (faceDisplay.front) {
      buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).uv(minU, maxV)
          .overlayCoords(overlay).uv2(light)
          .normal(normalMatrix, normal.x(), normal.y(), normal.z()).endVertex();
      buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).uv(minU, minV)
          .overlayCoords(overlay).uv2(light)
          .normal(normalMatrix, normal.x(), normal.y(), normal.z()).endVertex();
      buffer.vertex(matrix, x3, y3, z3).color(red, green, blue, alpha).uv(maxU, minV)
          .overlayCoords(overlay).uv2(light)
          .normal(normalMatrix, normal.x(), normal.y(), normal.z()).endVertex();
      buffer.vertex(matrix, x4, y4, z4).color(red, green, blue, alpha).uv(maxU, maxV)
          .overlayCoords(overlay).uv2(light)
          .normal(normalMatrix, normal.x(), normal.y(), normal.z()).endVertex();
    }
    if (faceDisplay.back) {
      buffer.vertex(matrix, x4, y4, z4).color(red, green, blue, alpha).uv(maxU, maxV)
          .overlayCoords(overlay).uv2(light)
          .normal(normalMatrix, 0, -1, 0).endVertex();
      buffer.vertex(matrix, x3, y3, z3).color(red, green, blue, alpha).uv(maxU, minV)
          .overlayCoords(overlay).uv2(light)
          .normal(normalMatrix, 0, -1, 0).endVertex();
      buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).uv(minU, minV)
          .overlayCoords(overlay).uv2(light)
          .normal(normalMatrix, 0, -1, 0).endVertex();
      buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).uv(minU, maxV)
          .overlayCoords(overlay).uv2(light)
          .normal(normalMatrix, 0, -1, 0).endVertex();
    }
  }

  public enum FaceDisplay {

    FRONT(true, false),
    BACK(false, true),
    BOTH(true, true);

    private final boolean front;
    private final boolean back;

    FaceDisplay(boolean front, boolean back) {
      this.front = front;
      this.back = back;
    }
  }
}
