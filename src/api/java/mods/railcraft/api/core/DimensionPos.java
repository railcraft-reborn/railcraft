/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.core;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

/**
 * This immutable class represents a point in the Minecraft world, while taking into account the
 * possibility of coordinates in different dimensions.
 */
public final class DimensionPos {

  private static final Logger logger = LogUtils.getLogger();

  /**
   * The dimension
   */
  private final ResourceKey<Level> dimension;

  private final BlockPos blockPos;

  /**
   * Creates a new WorldCoordinate
   *
   * @param dimension Dimension ID
   * @param x World Coordinate
   * @param y World Coordinate
   * @param z World Coordinate
   */
  public DimensionPos(ResourceKey<Level> dimension, int x, int y, int z) {
    this.dimension = dimension;
    this.blockPos = new BlockPos(x, y, z);
  }

  /**
   * Creates a new WorldCoordinate
   *
   * @param dimension Dimension ID
   * @param blockPos World Coordinates
   */
  public DimensionPos(ResourceKey<Level> dimension, BlockPos blockPos) {
    this.dimension = dimension;
    this.blockPos = blockPos;
  }

  public static DimensionPos from(BlockEntity blockEntity) {
    return new DimensionPos(blockEntity.getLevel().dimension(), blockEntity.getBlockPos());
  }

  public boolean isInSameChunk(DimensionPos otherCoord) {
    return dimension == otherCoord.dimension && getX() >> 4 == otherCoord.getX() >> 4
        && getZ() >> 4 == otherCoord.getZ() >> 4;
  }

  public boolean isEqual(ResourceKey<Level> dim, int x, int y, int z) {
    return getX() == x && getY() == y && getZ() == z && dimension.equals(dim);
  }

  public boolean isEqual(ResourceKey<Level> dim, BlockPos p) {
    return getX() == p.getX() && getY() == p.getY() && getZ() == p.getZ() && dimension.equals(dim);
  }

  public boolean isEqual(DimensionPos p) {
    return getX() == p.getX() && getY() == p.getY() && getZ() == p.getZ()
        && getDim().equals(p.getDim());
  }

  // public int compareTo(@Nonnull WorldCoordinate o) {
  // if (dimension != o.dimension)
  // return dimension - o.dimension;
  // if (getX() != o.getX())
  // return getX() - o.getX();
  // if (getY() != o.getY())
  // return getY() - o.getY();
  // if (getZ() != o.getZ())
  // return getZ() - o.getZ();
  // return 0;
  // }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    DimensionPos that = (DimensionPos) o;

    return dimension.equals(that.dimension) && blockPos.equals(that.blockPos);
  }

  @Override
  public int hashCode() {
    int result = dimension.hashCode();
    result = 31 * result + blockPos.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "WorldCoordinate{" + "dimension=" + dimension.toString() + ", x=" + getX() + ", y="
        + getY() + ", z=" + getZ() + '}';
  }

  public ResourceKey<Level> getDim() {
    return dimension;
  }

  public BlockPos getPos() {
    return blockPos;
  }

  public int getX() {
    return blockPos.getX();
  }

  public int getY() {
    return blockPos.getY();
  }

  public int getZ() {
    return blockPos.getZ();
  }

  public Vec3 getVector3d() {
    return new Vec3(this.getX(), this.getY(), this.getZ());
  }

  public CompoundTag writeTag() {
    CompoundTag tag = new CompoundTag();
    tag.put("blockPos", NbtUtils.writeBlockPos(this.blockPos));
    Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, this.dimension)
        .resultOrPartial(logger::error)
        .ifPresent(dimensionTag -> tag.put("dimension", dimensionTag));
    return tag;
  }

  public static DimensionPos readTag(CompoundTag tag) {
    ResourceKey<Level> dimension = Level.RESOURCE_KEY_CODEC
        .parse(NbtOps.INSTANCE, tag.get("dimension"))
        .resultOrPartial(logger::error)
        .orElse(Level.OVERWORLD);
    return new DimensionPos(dimension, NbtUtils.readBlockPos(tag.getCompound("blockPos")));
  }
}
