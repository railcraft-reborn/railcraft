package mods.railcraft.world.level.block.entity.multiblock;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class MultiblockEntity<T extends MultiblockEntity<T>> extends TileEntity {

  public static final int PULL = 1;
  public static final int PUSH = 2;
  public static final int PULL_PUSH = (PULL | PUSH);

  private boolean formed = false;
  private BlockPos parentPos = BlockPos.ZERO;

  public MultiblockEntity(TileEntityType<?> tileEntityType) {
    super(tileEntityType);
  }

  /**
   * Try to make this tile (entity) a parent.
   * @return true if ok, false if not.
   */
  public boolean tryToMakeParent() {
    if (this.isMultiblockPatternValid() || this.level.isClientSide()) {
      return false;
    }

    T[] tileEntities = this.getPatternEntities();
    if (tileEntities == null) {
      return false;
    }

    for (T tileEntity : tileEntities) {
      tileEntity.setParent(this.worldPosition.subtract(tileEntity.worldPosition));
    }

    return true;
  }

  /**
   * Handles the pattern detection. Required.
   * @return true if pattern detection is ok, false if not.
   */
  public boolean isMultiblockPatternValid() {
    return false;
  }

  /**
   * Gathers all of the block's tileentities. Required.
   * @return List of TileEntity we gathered
   */
  public @Nullable T[] getPatternEntities() {
    return null;
  }

  /**
   * Sets this tilentity's parent.
   * @param parentPos - The position of the parent
   */
  public void setParent(BlockPos parentPos) {
    this.delink(); // iirc we should not have linked stuffed before
    this.parentPos = parentPos;
    this.formed = true;
    this.setChanged();
  }

  /**
   * .
   * @return
   */
  public @Nullable T getParent() {
    if (!this.formed) {
      return null;
    }
    if (this.parentPos.equals(BlockPos.ZERO)) {
      return (T) this;
    }
    // it's totaly safe :) - speaking of!
    // TODO: implement chunk safety
    TileEntity doesItExist = this.level.getBlockEntity(parentPos);

    if (doesItExist == null || !(doesItExist instanceof MultiblockEntity<?>)) {
      this.delink();
      return null;
    }

    return (T)doesItExist;
  }

  /**
   * Delinks the parent softref, also makes us "unformed".
   */
  public void delink() {
    this.formed = false;
    this.parentPos = BlockPos.ZERO;
    this.setChanged();
  }

  /**
   * Check the link validity.
   */
  public void verifyLink() {
    if (this.getParent() == null) {
      this.delink();
      return;
    }
    if (!this.isMultiblockPatternValid()) {
      T[] tileEntities = this.getPatternEntities();
      if (tileEntities == null) {
        return;
      }

      for (T tileEntity : tileEntities) {
        tileEntity.delink();
      }
    }
  }

  public boolean isFormed() {
    return this.formed;
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
  public void load(BlockState blockState, CompoundNBT data) {
    super.load(blockState, data);
    this.formed = data.getBoolean("IsFormed");
    this.parentPos = NBTUtil.readBlockPos(data.getCompound("ParentPos"));
    this.verifyLink();
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putBoolean("IsFormed", this.formed);
    data.put("ParentPos", NBTUtil.writeBlockPos(this.parentPos));
    return data;
  }

}
