package mods.railcraft.util;

import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

/**
 * A mutable 2D Vector class that supports a broader range of math operations than Minecraft's
 * immutable Vec3D class.
 * <p>
 * For historical purposes, I borrowed this class from NovaStar, a simple 2D space shooter project I
 * wrote in college.
 */
public class Vec2d {

  private double x;

  private double y;

  public double getX() {
    return this.x;
  }

  public double getY() {
    return this.y;
  }

  public Vec2d() {}

  public Vec2d(Vec3i p) {
    this(p.getX(), p.getZ());
  }

  public Vec2d(Vec3 p) {
    this(p.x, p.z);
  }

  public Vec2d(Entity p) {
    this(p.getX(), p.getZ());
  }

  public Vec2d(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public static Vec2d fromPolar(double angle, float magnitude) {
    Vec2d v = new Vec2d();
    v.setFromPolar(angle, magnitude);
    return v;
  }

  public static Vec2d add(final Vec2d a, final Vec2d b) {
    return new Vec2d(a.getX() + b.getX(), a.getY() + b.getY());
  }

  public static Vec2d subtract(final Vec2d a, final Vec2d b) {
    return new Vec2d(a.getX() - b.getX(), a.getY() - b.getY());
  }

  public static Vec2d unit(final Vec2d a, final Vec2d b) {
    Vec2d unit = subtract(a, b);
    unit.normalize();
    return unit;
  }

  @Override
  public Vec2d clone() {
    return new Vec2d(x, y);
  }

  public void setLocation(double x, double y) {
    this.x = x;
    this.y = y;
  }

  // ==============================================================================
  public void setFromPolar(double angle, float magnitude) {
    float x = (float) Math.cos(angle) * magnitude;
    float y = (float) Math.sin(angle) * magnitude;
    setLocation(x, y);
  }

  public void zero() {
    x = y = 0;
  }

  public void normalize() {
    double mag = magnitude();
    if (mag != 0) {
      setLocation(x / mag, y / mag);
    }
  }

  public Vec2d unitVector() {
    Vec2d v = clone();
    v.normalize();
    return v;
  }

  public double magnitude() {
    return Math.sqrt(x * x + y * y);
  }

  public void setMagnitude(float mag) {
    setFromPolar(angle(), mag);
  }

  public double magnitudeSq() {
    return x * x + y * y;
  }

  public void negate() {
    x = -x;
    y = -y;
  }

  public float angle() {
    return (float) Math.atan2(y, x);
  }

  public void rotate(double theta) {
    double nx = x * (float) Math.cos(theta) - y * (float) Math.sin(theta);
    double ny = x * (float) Math.sin(theta) + y * (float) Math.cos(theta);
    setLocation(nx, ny);
  }

  public void rotate90() {
    double ox = x;
    x = -y;
    y = ox;
  }

  public void rotate270() {
    double ox = x;
    // noinspection SuspiciousNameCombination
    x = y;
    y = -ox;
  }

  public void subtract(final Vec2d p) {
    x -= p.getX();
    y -= p.getY();
  }

  public void subtract(int x, int y) {
    this.x -= x;
    this.y -= y;
  }

  public void subtract(final double x, final double y) {
    this.x -= (int) x;
    this.y -= (int) y;
  }

  public void add(final Vec2d p) {
    x += p.getX();
    y += p.getY();
  }

  public void add(int x, int y) {
    this.x += x;
    this.y += y;
  }

  public void add(final double x, final double y) {
    this.x += (int) x;
    this.y += (int) y;
  }

  // public void transform(AffineTransform a) {
  // setLocation(a.transform(this, null));
  // }
  //
  // public Vec2D makeTransform(AffineTransform a) {
  // return new Vec2D(a.transform(this, null));
  // }

  /**
   * Finds the dot product of two vectors.
   */
  public double dotProduct(final Vec2d v) {
    return x * v.getX() + y * v.getY();
  }

  /**
   * Finds the absolute angle between two vectors using the Law of Cosines:<br>
   * <b>V . W = |V||W|cos(a)</b>
   */
  public double angleBetween(final Vec2d v) {
    double a = dotProduct(v);
    double magnitude = magnitude() * v.magnitude();
    if (magnitude == 0) {
      return 0;
    }
    a /= magnitude;

    // Check bounds, if you don't you WILL get NaNs from acos.
    // This is because of slight errors in the floating point arithmetic.
    if (a > 1) {
      a = 1;
    } else if (a < -1) {
      a = -1;
    }
    return Math.acos(a);
  }

  public double angleTo(Vec2d a) {
    return Math.atan2(a.getY() - y, a.getX() - x);
  }

  public double angleFrom(Vec2d a) {
    return Math.atan2(y - a.getY(), x - a.getX());
  }

  public void scale(float scale) {
    x *= scale;
    y *= scale;
  }

  public void addScale(float scale, Vec2d v) {
    setLocation(x + v.getX() * scale, y + v.getY() * scale);
  }
}
