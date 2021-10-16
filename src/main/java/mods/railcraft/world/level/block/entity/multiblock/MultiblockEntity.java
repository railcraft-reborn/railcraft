package mods.railcraft.world.level.block.entity.multiblock;

import java.util.Collection;

import javax.annotation.Nullable;

import mods.railcraft.world.level.block.entity.RailcraftTickableBlockEntity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MultiblockEntity<T extends MultiblockEntity<T>> extends RailcraftTickableBlockEntity {

  private static final Logger logger =
      LogManager.getLogger("Railcraft/MultiblockEntity");
  public static final int PULL = 1;
  public static final int PUSH = 2;
  public static final int PULL_PUSH = (PULL | PUSH);

  private boolean formed = false;
  private BlockPos parentPos = BlockPos.ZERO;
  private BlockPos normal;
  private T entityCache;

  public MultiblockEntity(TileEntityType<?> tileEntityType) {
    super(tileEntityType);
  }

  /**
   * Try to make this tile (entity) a parent.
   * @return true if ok, false if not.
   */
  public boolean tryToMakeParent(Direction facingDir) {
    logger.info("TryToMakeParent - Trying to create THIS as a parent.");
    if (facingDir.getStepY() != 0) {
      logger.info("TryToMakeParent - Denied, face is either UP/DOWN.");
      return false;
    }

    // rotate 90 degrees, facing AWAY from the user
    this.normal = new BlockPos(facingDir.getClockWise().getClockWise().getNormal());
    if (this.level.isClientSide()) {
      // silently fail.
      return false;
    }
    if (!this.isMultiblockPatternValid(this.normal)) {
      logger.info("TryToMakeParent - Fail, pattern invalid OR clientside.");
      return false;
    }

    Collection<T> tileEntities = this.getPatternEntities(this.normal);
    if (tileEntities == null) {
      logger.info("TryToMakeParent - Fail, getPatternEntities returned null.");
      return false;
    }

    for (T tileEntity : tileEntities) {
      tileEntity.setParent(this.worldPosition);
    }
    logger.info("TryToMakeParent - Success.");

    return true;
  }

  /**
   * Handles the pattern detection. Required.
   * @return true if pattern detection is ok, false if not.
   */
  public boolean isMultiblockPatternValid(BlockPos normal) {
    // SCAN BY NORMAL.
    return false;
  }

  /**
   * Gathers all of the block's tileentities. Required.
   * @return List of TileEntity we gathered
   */
  @Nullable
  public Collection<T> getPatternEntities(BlockPos normal) {
    return null;
  }

  /**
   * Sets this tilentity's parent, delinking first.
   * @param parentPos - The position of the parent
   */
  public void setParent(BlockPos parentPos) {
    logger.info(
        "setParent - Setting (" + parentPos.toShortString() + ") as our parentPos.");
    this.delink(); // iirc we should not have linked stuffed before
    this.parentPos = parentPos;
    // we do not precache the ref here, though i can.
    this.formed = true;
    this.setChanged();
  }

  /**
   * Is this a parent.
   * @return TRUE if yes.
   */
  public boolean isParent() {
    if (this.normal == null || !this.isFormed()) {
      return false;
    }
    return this.parentPos.equals(BlockPos.ZERO);
  }

  /**
   * Gets the parent of this multiblock, or null if not. Does delinking and cacheing.
   */
  @SuppressWarnings("unchecked")
  @Nullable
  public T getParent() {
    if (!this.formed) {
      if (this.entityCache != null) {
        // this should NOT happen!
        logger.warn("Multiblock has entityCache while it isn't formed.");
        this.entityCache = null;
      }
      return null;
    }
    // us
    if (this.isParent()) {
      if (entityCache == null || entityCache.isRemoved()) {
        entityCache = (T) this;
      }
      return (T) entityCache;
    }
    // not us
    if (entityCache == null || entityCache.isRemoved()) {
      // it's totaly safe :) - speaking of!
      // TODO: implement chunk safety
      @Nullable TileEntity doesItExist = this.getLevel().getBlockEntity(parentPos);

      if (doesItExist == null || !(doesItExist instanceof MultiblockEntity<?>)) {
        logger.info(
            "getParent - Parent does not exist OR not the same type. Type or Null:"
            + ((doesItExist == null) ? "null" : doesItExist.toString()));
        this.delink();
        return null;
      }
      entityCache = (T) doesItExist;
    }
    return entityCache;
  }

  /**
   * Delinks the parent softref, also makes us "unformed".
   */
  public void delink() {
    logger.info(
        "delink - Delink, last parent pos: (" + this.parentPos.toShortString() + ")");
    this.formed = false;
    this.parentPos = BlockPos.ZERO;
    this.entityCache = null;
    this.setChanged();
  }

  /**
   * Check the link validity.
   * 1. Checks if the parent exists
   * 2. checks if the pattern is right (if parent)
   * If any fails, delinking will happen
   */
  public boolean verifyLink() {
    if (!this.formed) {
      return false;
    }
    logger.info(
        "verifyLink -Veryfing link. "
        + "Parent: (" + this.parentPos.toShortString() + ")"
        + "Us: (" + this.worldPosition.toShortString() + ")");
    if (this.getParent() == null) {
      this.delink();
      logger.info("getParent - Parent didnt exist anymore.");
      return false;
    }
    // we do not save normals if we arent the parent.
    if (this.normal == null) {
      return true;
    }

    if (this.getParent() == this && !this.isMultiblockPatternValid(this.normal)) {
      logger.info("getParent - Parent is valid (it is us), however pattern broke.");

      Collection<T> tileEntities = this.getPatternEntities(this.normal);
      if (tileEntities == null) {
        return false;
      }

      for (T tileEntity : tileEntities) {
        tileEntity.delink();
      }
      return false;
    }
    return true;
  }

  public boolean isFormed() {
    return this.formed;
  }

  /**
   * Do something once the game fully loads (after load()).
   * <b>Always</b> call the parent!
   */
  protected void initializeAfterLoad() {
    this.verifyLink();
  }

  /**
   * Sets the facing direction's Fluid IO.
   * This is on a per-face basis as we do not need complex in outs just yet. might move this
   * @param face - The face.
   * @param pushpullFlag - Methods:
   *  {@link mods.railcraft.world.level.block.entity.multiblock.MultiblockEntity#PULL PULL}
   *  {@link mods.railcraft.world.level.block.entity.multiblock.MultiblockEntity#PUSH PUSH}
   *  {@link
   *    mods.railcraft.world.level.block.entity.multiblock.MultiblockEntity#PULL_PUSH PULL_PUSH}
   * @return
   */
  public boolean setFacingDirectionFluidIO(Direction face, int pushpullFlag) {
    return false;
  }

  /**
   * Sets the facing direction's Item IO.
   * @param face - The face.
   * @param pushpullFlag - Methods:
   *  {@link mods.railcraft.world.level.block.entity.multiblock.MultiblockEntity#PULL PULL}
   *  {@link mods.railcraft.world.level.block.entity.multiblock.MultiblockEntity#PUSH PUSH}
   *  {@link
   *    mods.railcraft.world.level.block.entity.multiblock.MultiblockEntity#PULL_PUSH PULL_PUSH}
   * @return
   */
  public boolean setFacingDirectionItemIO(Direction face, int pushpullFlag) {
    return false;
  }

  @Override
  protected void load() {
    this.verifyLink();
  }

  @Override
  public void load(BlockState blockState, CompoundNBT data) {
    super.load(blockState, data);

    this.formed = data.getBoolean("formed");
    this.parentPos = NBTUtil.readBlockPos(data.getCompound("parentPos"));

    CompoundNBT norm;
    try {
      norm = data.getCompound("normal");
    } catch (Exception e) {
      return;
    }
    if (norm == null) {
      return;
    }
    this.normal = NBTUtil.readBlockPos(norm);
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putBoolean("formed", this.formed);
    data.put("parentPos", NBTUtil.writeBlockPos(this.parentPos));
    // do not save the normal when it's null.
    if (this.normal != null) {
      data.put("normal", NBTUtil.writeBlockPos(this.normal));
    }
    return data;
  }
}
