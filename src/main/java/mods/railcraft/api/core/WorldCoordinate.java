/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.core;

import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * This immutable class represents a point in the Minecraft world, while taking into account the
 * possibility of coordinates in different dimensions.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class WorldCoordinate {

  private static final Logger logger = LogManager.getLogger();

  /**
   * The dimension
   */
  private final RegistryKey<World> dimension;

  private final BlockPos pos;

  /**
   * Creates a new WorldCoordinate
   *
   * @param dimension Dimension ID
   * @param x World Coordinate
   * @param y World Coordinate
   * @param z World Coordinate
   */
  public WorldCoordinate(RegistryKey<World> dimension, int x, int y, int z) {
    this.dimension = dimension;
    this.pos = new BlockPos(x, y, z);
  }

  /**
   * Creates a new WorldCoordinate
   *
   * @param dimension Dimension ID
   * @param pos World Coordinates
   */
  public WorldCoordinate(RegistryKey<World> dimension, BlockPos pos) {
    this.dimension = dimension;
    this.pos = pos;


  }

  public static WorldCoordinate from(TileEntity tile) {
    return new WorldCoordinate(tile.getLevel().dimension(), tile.getBlockPos());
  }

  public static @Nullable WorldCoordinate readFromNBT(CompoundNBT data, String key) {
    if (!data.contains(key, Constants.NBT.TAG_COMPOUND)) {
      return null;
    }
    CompoundNBT nbt = data.getCompound(key);
    RegistryKey<World> dim =
        World.RESOURCE_KEY_CODEC.parse(NBTDynamicOps.INSTANCE, nbt.get("dimension"))
            .resultOrPartial(logger::error).orElse(World.OVERWORLD);
    int x = nbt.getInt("x");
    int y = nbt.getInt("y");
    int z = nbt.getInt("z");
    return new WorldCoordinate(dim, x, y, z);
  }

  public void writeToNBT(CompoundNBT data, String tag) {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putInt("x", this.getX());
    nbt.putInt("y", this.getY());
    nbt.putInt("z", this.getZ());
    ResourceLocation.CODEC.encodeStart(NBTDynamicOps.INSTANCE, this.dimension.location())
        .resultOrPartial(logger::error)
        .ifPresent(dimensionNbt -> nbt.put("dimension", dimensionNbt));
    data.put(tag, nbt);
  }

  public boolean isInSameChunk(WorldCoordinate otherCoord) {
    return dimension == otherCoord.dimension && getX() >> 4 == otherCoord.getX() >> 4
        && getZ() >> 4 == otherCoord.getZ() >> 4;
  }

  public boolean isEqual(RegistryKey<World> dim, int x, int y, int z) {
    return getX() == x && getY() == y && getZ() == z && dimension.equals(dim);
  }

  public boolean isEqual(RegistryKey<World> dim, BlockPos p) {
    return getX() == p.getX() && getY() == p.getY() && getZ() == p.getZ() && dimension.equals(dim);
  }

  public boolean isEqual(WorldCoordinate p) {
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

    WorldCoordinate that = (WorldCoordinate) o;

    return dimension.equals(that.dimension) && pos.equals(that.pos);
  }

  @Override
  public int hashCode() {
    int result = dimension.hashCode();
    result = 31 * result + pos.hashCode();
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
    return pos;
  }

  public int getX() {
    return pos.getX();
  }

  public int getY() {
    return pos.getY();
  }

  public int getZ() {
    return pos.getZ();
  }

  public Vector3d getVector3d() {
    return new Vector3d(this.getX(), this.getY(), this.getZ());
  }
}
