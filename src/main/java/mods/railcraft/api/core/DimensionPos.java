/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * This immutable class represents a point in the Minecraft world, while taking into account the
 * possibility of coordinates in different dimensions.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class DimensionPos {

  private static final Logger logger = LogManager.getLogger();

  /**
   * The dimension
   */
  private final RegistryKey<World> dimension;

  private final BlockPos blockPos;

  /**
   * Creates a new WorldCoordinate
   *
   * @param dimension Dimension ID
   * @param x World Coordinate
   * @param y World Coordinate
   * @param z World Coordinate
   */
  public DimensionPos(RegistryKey<World> dimension, int x, int y, int z) {
    this.dimension = dimension;
    this.blockPos = new BlockPos(x, y, z);
  }

  /**
   * Creates a new WorldCoordinate
   *
   * @param dimension Dimension ID
   * @param blockPos World Coordinates
   */
  public DimensionPos(RegistryKey<World> dimension, BlockPos blockPos) {
    this.dimension = dimension;
    this.blockPos = blockPos;
  }

  public static DimensionPos from(TileEntity blockEntity) {
    return new DimensionPos(blockEntity.getLevel().dimension(), blockEntity.getBlockPos());
  }

  public boolean isInSameChunk(DimensionPos otherCoord) {
    return dimension == otherCoord.dimension && getX() >> 4 == otherCoord.getX() >> 4
        && getZ() >> 4 == otherCoord.getZ() >> 4;
  }

  public boolean isEqual(RegistryKey<World> dim, int x, int y, int z) {
    return getX() == x && getY() == y && getZ() == z && dimension.equals(dim);
  }

  public boolean isEqual(RegistryKey<World> dim, BlockPos p) {
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

  public RegistryKey<World> getDim() {
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

  public Vector3d getVector3d() {
    return new Vector3d(this.getX(), this.getY(), this.getZ());
  }

  public CompoundNBT writeTag() {
    CompoundNBT tag = new CompoundNBT();
    tag.put("blockPos", NBTUtil.writeBlockPos(this.blockPos));
    World.RESOURCE_KEY_CODEC.encodeStart(NBTDynamicOps.INSTANCE, this.dimension)
        .resultOrPartial(logger::error)
        .ifPresent(dimensionTag -> tag.put("dimension", dimensionTag));
    return tag;
  }

  public static DimensionPos readTag(CompoundNBT tag) {
    RegistryKey<World> dimension = World.RESOURCE_KEY_CODEC
        .parse(NBTDynamicOps.INSTANCE, tag.get("dimension"))
        .resultOrPartial(logger::error)
        .orElse(World.OVERWORLD);
    return new DimensionPos(dimension, NBTUtil.readBlockPos(tag.getCompound("blockPos")));
  }
}
