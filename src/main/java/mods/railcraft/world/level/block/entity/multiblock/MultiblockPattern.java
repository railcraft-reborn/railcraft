package mods.railcraft.world.level.block.entity.multiblock;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.chars.CharList;
import it.unimi.dsi.fastutil.objects.Object2CharMap;
import it.unimi.dsi.fastutil.objects.Object2CharOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;

/**
 * Multiblock pattern. ONLY FOR CUBE-LIKE STRUCTURES!
 *
 * <p>
 * You are facing it in the Z axis (BACK/FOURTH), while LEFT-RIGHT is X, and UP/DOWN is Y.
 */
public class MultiblockPattern {

  private final int sizeX;
  private final int sizeY;
  private final int sizeZ;
  private final BlockPos masterOffset;
  private final char[][][] pattern;
  private final Char2ObjectMap<BlockPredicate> predicates;

  private MultiblockPattern(char[][][] pattern, BlockPos masterOffset,
      Map<Character, BlockPredicate> predicates) {
    this.masterOffset = masterOffset;
    this.pattern = pattern;
    this.sizeY = pattern.length;
    this.sizeZ = pattern[0].length;
    this.sizeX = pattern[0][0].length;
    this.predicates = new Char2ObjectOpenHashMap<>(predicates);
  }

  public int getRadius() {
    return Mth.ceil(Ints.max(this.sizeX, this.sizeY, this.sizeZ));
  }

  /**
   * Verifies the pattern.
   * 
   * @param blockPos The targeted block's {@link BlockPos}.
   * @param normal The normal/face that the user clicked on.
   * @param level The current game world. Must be serverside.
   * @return TRUE if the pattern is valid, FALSE if not.
   */
  public Optional<Object2CharMap<BlockPos>> verifyPattern(BlockPos blockPos, ServerLevel level) {
    var originPos = blockPos.subtract(this.masterOffset);
    Object2CharMap<BlockPos> map =
        new Object2CharOpenHashMap<>(this.sizeX * this.sizeY * this.sizeZ);

    for (var x = 0; x < this.sizeX; x++) {
      for (var y = 0; y < this.sizeY; y++) {
        for (var z = 0; z < this.sizeZ; z++) {
          var marker = this.pattern[y][z][x];
          var predicate = this.predicates.get(marker);
          var pos = new BlockPos(x, y, z).offset(originPos);
          if (!predicate.test(level, pos)) {
            return Optional.empty();
          }
          map.put(pos, marker);
        }
      }
    }

    return Optional.of(map);
  }

  public static Builder builder(BlockPos masterOffset) {
    return new Builder(masterOffset);
  }

  public static class Builder {

    private final Deque<List<CharList>> pattern = new ArrayDeque<>();
    private final BlockPos masterOffset;
    private final ImmutableMap.Builder<Character, BlockPredicate> predicates =
        ImmutableMap.builder();

    private Builder(BlockPos masterOffset) {
      this.masterOffset = masterOffset;
    }

    /**
     * Defines a row (both X and Y) of the recipie's pattern.
     * 
     * @param pattern A list (like [[b,b,b],[b,b,b],[b,b,b]]), this is a flat cut
     * @return this, for chaning functions.
     */
    public Builder layer(List<CharList> pattern) {
      this.pattern.push(pattern);
      return this;
    }

    public Builder predicate(char ch, BlockPredicate predicate) {
      this.predicates.put(ch, predicate);
      return this;
    }

    /**
     * "Build" the pattern, returning a new MultiblockPattern.
     */
    public MultiblockPattern build() {
      var patternArray = this.pattern.stream()
          .map(layer -> layer.stream()
              .map(CharList::toCharArray)
              .toArray(char[][]::new))
          .toArray(char[][][]::new);
      return new MultiblockPattern(patternArray, this.masterOffset, this.predicates.build());
    }
  }
}
