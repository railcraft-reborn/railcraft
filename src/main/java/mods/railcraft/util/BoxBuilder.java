package mods.railcraft.util;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Created by CovertJaguar on 3/9/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BoxBuilder {

  public static final AABB FULL_BOX = create().box().build();
  public static final double PIXEL = 1.0 / 16.0;

  private double minX;
  private double minY;
  private double minZ;
  private double maxX;
  private double maxY;
  private double maxZ;

  private BoxBuilder() {}

  public static BoxBuilder create() {
    return new BoxBuilder();
  }

  public AABB build() {
    return new AABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
  }

  public BoxBuilder fromAABB(AABB box) {
    return this.setBounds(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
  }

  public BoxBuilder box() {
    return this.setBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
  }

  public BoxBuilder setBounds(double x1, double y1, double z1, double x2, double y2, double z2) {
    this.minX = Math.min(x1, x2);
    this.minY = Math.min(y1, y2);
    this.minZ = Math.min(z1, z2);
    this.maxX = Math.max(x1, x2);
    this.maxY = Math.max(y1, y2);
    this.maxZ = Math.max(z1, z2);
    return this;
  }

  public BoxBuilder setBoundsToPoint(Vec3 vector) {
    this.minX = vector.x;
    this.minY = vector.y;
    this.minZ = vector.z;
    this.maxX = vector.x;
    this.maxY = vector.y;
    this.maxZ = vector.z;
    return this;
  }

  public BoxBuilder setBoundsFromBlock(BlockState state, BlockGetter world, BlockPos pos) {
    var shape = state.getShape(world, pos);
    this.setBounds(
        pos.getX() + shape.min(Direction.Axis.X),
        pos.getY() + shape.min(Direction.Axis.Y),
        pos.getZ() + shape.min(Direction.Axis.Z),
        pos.getX() + shape.max(Direction.Axis.X),
        pos.getY() + shape.max(Direction.Axis.Y),
        pos.getZ() + shape.max(Direction.Axis.Z));
    return this;
  }

  public BoxBuilder at(BlockPos pos) {
    this.setBounds(
        pos.getX(),
        pos.getY(),
        pos.getZ(),
        pos.getX() + 1,
        pos.getY() + 1,
        pos.getZ() + 1);
    return this;
  }

  public BoxBuilder at(BlockPos pos, double grow) {
    this.setBounds(
        pos.getX() - grow,
        pos.getY() - grow,
        pos.getZ() - grow,
        pos.getX() + 1.0 + grow,
        pos.getY() + 1.0 + grow,
        pos.getZ() + 1.0 + grow);
    return this;
  }

  public BoxBuilder inflate(double distance) {
    this.minX -= distance;
    this.minY -= distance;
    this.minZ -= distance;
    this.maxX += distance;
    this.maxY += distance;
    this.maxZ += distance;
    return this;
  }

  public BoxBuilder clampToWorld() {
    // if (minY < 0)
    // minY = 0; // cubic chunks
    return this;
  }

  public BoxBuilder inflateHorizontally(double distance) {
    this.minX -= distance;
    this.minZ -= distance;
    this.maxX += distance;
    this.maxZ += distance;
    return this;
  }

  public BoxBuilder expandXAxis(double distance) {
    this.minX -= distance;
    this.maxX += distance;
    return this;
  }

  public BoxBuilder expandYAxis(double distance) {
    this.minY -= distance;
    this.maxY += distance;
    return this;
  }

  public BoxBuilder expandZAxis(double distance) {
    this.minZ -= distance;
    this.maxZ += distance;
    return this;
  }

  public BoxBuilder expandAxis(Direction.Axis axis, double distance) {
    switch (axis) {
      case X -> this.expandXAxis(distance);
      case Y -> this.expandYAxis(distance);
      case Z -> this.expandZAxis(distance);
    }
    return this;
  }

  public BoxBuilder raiseFloor(double raise) {
    this.minY += raise;
    return this;
  }

  public BoxBuilder raiseCeiling(double raise) {
    this.maxY += raise;
    return this;
  }

  public BoxBuilder raiseCeilingPixel(int raise) {
    this.maxY += raise * PIXEL;
    return this;
  }

  public BoxBuilder shiftY(double shift) {
    this.minY += shift;
    this.maxY += shift;
    return this;
  }

  public BoxBuilder expandToCoordinate(Vec3 point) {
    return this.expandToCoordinate(point.x(), point.y(), point.z());
  }

  public BoxBuilder expandToCoordinate(BlockPos point) {
    return this.expandToCoordinate(point.getX(), point.getY(), point.getZ());
  }

  public BoxBuilder expandToCoordinate(double x, double y, double z) {
    if (x < this.minX) {
      this.minX = x;
    } else if (x > this.maxX) {
      this.maxX = x;
    }

    if (y < this.minY) {
      this.minY = y;
    } else if (y > this.maxY) {
      this.maxY = y;
    }

    if (z < this.minZ) {
      this.minZ = z;
    } else if (z > this.maxZ) {
      this.maxZ = z;
    }

    return this;
  }

  public BoxBuilder offset(Vec3i pos) {
    return this.offset(pos.getX(), pos.getY(), pos.getZ());
  }

  public BoxBuilder offset(double x, double y, double z) {
    this.minX += x;
    this.minY += y;
    this.minZ += z;
    this.maxX += x;
    this.maxY += y;
    this.maxZ += z;
    return this;
  }

  public BoxBuilder setMinY(double minY) {
    this.minY = minY;
    return this;
  }

  public BoxBuilder setMaxY(double maxY) {
    this.maxY = maxY;
    return this;
  }

  public BoxBuilder upTo(double distance) {
    this.minX -= distance;
    this.minZ -= distance;
    this.maxX += distance;
    this.maxY += distance;
    this.maxZ += distance;
    return this;
  }

  public boolean isUndefined() {
    return this.minX == this.maxX
        || this.minY == this.maxY
        || this.minZ == this.maxZ;
  }

  @Override
  public String toString() {
    return String.format("{%.1f,%.1f,%.1f} - {%.1f,%.1f,%.1f}",
        this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
  }
}
