package mods.railcraft.world.level.block.entity.multiblock;

import com.google.common.collect.Lists;

import java.util.ArrayList;

import net.minecraft.advancements.criterion.BlockPredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Multiblock pattern. ONLY FOR CUBE-LIKE STRUCTURES!
 *
 * <p>You are facing it in the Z axis (BACK/FOURTH), while LEFT-RIGHT is X, and UP/DOWN is Y.
 */
public class MultiblockPattern {
  private static final Logger logger = LogManager.getLogger();
  private final int sizeX;
  private final int sizeY;
  private final int sizeZ;
  private final int offsetX;
  private final int offsetZ;
  private final ArrayList<ArrayList<ArrayList<BlockPredicate>>> threeDarray;

  MultiblockPattern(ArrayList<ArrayList<ArrayList<BlockPredicate>>> pattern,
      int offsetX, int offsetZ) {
    this.offsetX = offsetX;
    this.offsetZ = offsetZ;
    // Z Y X
    this.sizeZ = pattern.size();
    this.threeDarray = pattern;

    int highestY = 0;
    int highestX = 0;
    for (ArrayList<ArrayList<BlockPredicate>> blocksY : pattern) {
      int yyLen = blocksY.size();
      if (yyLen > highestY) {
        highestY = yyLen;
      }
      for (ArrayList<BlockPredicate> blocksX : blocksY) {
        int xxLen = blocksX.size();
        if (xxLen > highestX) {
          highestX = xxLen;
        }
      }
    }
    this.sizeY = highestY;
    this.sizeX = highestX;
  }

  /**
   * Gets BlockPos from the pattern.
   * @param worldPos The target location
   * @param normal Used for offsetting
   * @return Iteratable list of the pattern's position
   */
  public Iterable<BlockPos> getPatternPos(BlockPos worldPos, BlockPos normal) {
    int boxX = this.sizeX - 1;
    int boxY = this.sizeY - 1;
    int boxZ = this.sizeZ - 1;
    BlockPos originPos = worldPos.offset(
          (normal.getX() == 0) ? this.offsetX : 0,
          -1, // this.offsetY
          (normal.getZ() == 0) ? this.offsetZ : 0);

    return BlockPos.betweenClosed(
        originPos, originPos.offset(
          (normal.getX() == 0) ? boxX : normal.getX() * boxX, // positive or negative, but never 0
          boxY, // you CANNOT assemble multiblocks on the top.
          (normal.getZ() == 0) ? boxZ : normal.getZ() * boxZ));
  }

  /**
   * Verifies the pattern.
   * @param worldPos The targeted block's {@link BlockPos}.
   * @param normal The normal/face that the user clicked on.
   * @param currentLevel The current game world. Must be serverside.
   * @return TRUE if the pattern is valid, FALSE if not.
   */
  public boolean verifyPattern(BlockPos worldPos, BlockPos normal, World currentLevel) {
    if (currentLevel.isClientSide()) {
      return false;
    }
    logger.info("verifyPattern: Verifying pattern. Using [pattern name to be implemented soon].");
    for (BlockPos pos : this.getPatternPos(worldPos, normal)) {
      BlockPos denormalizedPos = pos.subtract(worldPos).offset(
          // you may ask: what the fuck? and my answer: me too, what the fuck?
          (normal.getX() == 0) ? -this.offsetX : (normal.getX() == -1) ? this.sizeX - 1 : 0,
          1,
          (normal.getZ() == 0) ? -this.offsetZ : (normal.getZ() == -1) ? this.sizeZ - 1 : 0);
      BlockPredicate predicate =
          this.threeDarray.get(denormalizedPos.getZ())
            .get(denormalizedPos.getY())
            .get(denormalizedPos.getX());

      if (!predicate.matches((ServerWorld)currentLevel, pos)) {
        logger.info("verifyPattern: Multiblock failed at POS: " + pos.toShortString());
        return false;
      }
    }
    logger.info("verifyPattern: Pattern valid.");
    return true;
  }

  public static class Builder {
    private final ArrayList<ArrayList<ArrayList<BlockPredicate>>> pattern =
        // Y Z X
        Lists.newArrayList(Lists.newArrayList(Lists.newArrayList()));
    private final int offsetX;
    private final int offsetZ;

    Builder(int offsetX, int offsetZ) {
      this.offsetX = offsetX;
      this.offsetZ = offsetZ;
    }

    /**
     * Defines a row (both X and Y) of the recipie's pattern.
     * @param pattern A list (like [[b,b,b],[b,b,b],[b,b,b]]), this is a flat cut
     * @return this, for chaning functions.
     */
    public Builder pattern(ArrayList<ArrayList<BlockPredicate>> pattern) {
      this.pattern.add(pattern);
      return this;
    }

    public MultiblockPattern build() {
      return new MultiblockPattern(this.pattern, this.offsetX, this.offsetZ);
    }
  }
}
