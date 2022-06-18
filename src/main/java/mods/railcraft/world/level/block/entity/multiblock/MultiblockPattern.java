package mods.railcraft.world.level.block.entity.multiblock;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import org.jetbrains.annotations.Nullable;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.chars.CharList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

/**
 * Multiblock pattern.
 */
public class MultiblockPattern<T> {

  private final int xSize;
  private final int ySize;
  private final int zSize;
  private final Vec3i masterOffset;
  private final char[][][] pattern;
  private final Char2ObjectMap<BlockPredicate> predicates;
  @Nullable
  private final AABB entityCheckBounds;

  @Nullable
  private final T metadata;

  private MultiblockPattern(char[][][] pattern, Vec3i masterOffset,
      Map<Character, BlockPredicate> predicates, @Nullable AABB entityCheckBounds,
      @Nullable T metadata) {
    this.masterOffset = masterOffset;
    this.pattern = pattern;
    this.ySize = pattern.length;
    this.zSize = pattern[0].length;
    this.xSize = pattern[0][0].length;
    this.predicates = new Char2ObjectOpenHashMap<>(predicates);
    this.entityCheckBounds = entityCheckBounds;
    this.metadata = metadata;

    for (var y = 0; y < this.ySize; y++) {
      for (var z = 0; z < this.zSize; z++) {
        for (var x = 0; x < this.xSize; x++) {
          var marker = this.pattern[y][z][x];
          if (!this.predicates.containsKey(marker)) {
            throw new IllegalStateException("No predicate exists for marker: " + marker);
          }
        }
      }
    }
  }

  public MultiblockPattern<T> rotateClockwise() {
    var masterOffset =
        new Vec3i(-this.masterOffset.getZ(), this.masterOffset.getY(), this.masterOffset.getX());
    var pattern = rotatePatternClockwise(this.pattern);
    var entityCheckBounds =
        this.entityCheckBounds == null ? null : rotateBoundingBoxClockwise(this.entityCheckBounds);
    return new MultiblockPattern<>(pattern, masterOffset, this.predicates, entityCheckBounds,
        this.metadata);
  }

  public int getXSize() {
    return this.xSize;
  }

  public int getYSize() {
    return this.ySize;
  }

  public int getZSize() {
    return this.zSize;
  }

  public int getArea() {
    return this.xSize * this.ySize * this.zSize;
  }

  public T getMetadata() {
    return this.metadata;
  }

  public boolean isValidPosition(BlockPos relativePos) {
    return relativePos.getX() >= 0 && relativePos.getX() < this.xSize
        && relativePos.getY() >= 0 && relativePos.getY() < this.ySize
        && relativePos.getZ() >= 0 && relativePos.getZ() < this.zSize;
  }
  
  public char getMarkerOrDefault(BlockPos relativePos, char defaultMarker) {
    return this.isValidPosition(relativePos) ? this.getMarker(relativePos) : defaultMarker;
  }

  public char getMarker(BlockPos relativePos) {
    return this.pattern[relativePos.getY()][relativePos.getZ()][relativePos.getX()];
  }

  public boolean isWithinPattern(BlockPos blockPos, BlockPos masterPos) {
    var minPos = masterPos.subtract(this.masterOffset);
    var maxPos = minPos.offset(this.xSize, this.ySize, this.zSize);
    return blockPos.getX() <= maxPos.getX()
        && blockPos.getY() <= maxPos.getY()
        && blockPos.getZ() <= maxPos.getZ()
        && blockPos.getX() >= minPos.getX()
        && blockPos.getY() >= minPos.getY()
        && blockPos.getZ() >= minPos.getZ();
  }

  private boolean checkForEntities(BlockPos blockPos, ServerLevel level) {
    return this.entityCheckBounds == null
        || level.getEntitiesOfClass(LivingEntity.class, this.entityCheckBounds.move(blockPos))
            .isEmpty();
  }

  public Optional<Map<BlockPos, Element>> resolve(BlockPos blockPos, ServerLevel level) {
    if (!this.checkForEntities(blockPos, level)) {
      return Optional.empty();
    }

    var originPos = blockPos.subtract(this.masterOffset);
    Map<BlockPos, Element> map = new HashMap<>(this.getArea());

    for (var x = 0; x < this.xSize; x++) {
      for (var y = 0; y < this.ySize; y++) {
        for (var z = 0; z < this.zSize; z++) {
          if (!this.resolveElement(originPos, x, y, z, level, map::put)) {
            return Optional.empty();
          }
        }
      }
    }

    return Optional.of(map);
  }

  private boolean resolveElement(BlockPos originPos, int x, int y, int z,
      ServerLevel level, BiConsumer<BlockPos, Element> consumer) {
    var marker = this.pattern[y][z][x];
    var predicate = this.predicates.get(marker);
    var pos = originPos.offset(x, y, z);
    if (!predicate.test(level, pos)) {
      return false;
    }
    consumer.accept(pos, new Element(new BlockPos(x, y, z), marker));
    return true;
  }

  public record Element(BlockPos relativePos, char marker) {}

  private static AABB rotateBoundingBoxClockwise(AABB boundingBox) {
    return new AABB(
        -boundingBox.minZ, boundingBox.minY, boundingBox.minX,
        -boundingBox.maxZ, boundingBox.maxY, boundingBox.maxX);
  }

  private static char[][][] rotatePatternClockwise(char[][][] pattern) {
    final int ySize = pattern.length;
    final int zSize = pattern[0].length;
    final int xSize = pattern[0][0].length;
    var ret = new char[ySize][xSize][zSize];
    for (int y = 0; y < ySize; y++) {
      for (int z = 0; z < zSize; z++) {
        for (int x = 0; x < xSize; x++) {
          ret[y][x][zSize - 1 - z] = pattern[y][z][x];
        }
      }
    }
    return ret;
  }

  public static <T> Builder<T> builder(int xOffset, int yOffset, int zOffset) {
    return builder(new Vec3i(xOffset, yOffset, zOffset));
  }

  public static <T> Builder<T> builder(Vec3i masterOffset) {
    return new Builder<>(masterOffset);
  }

  public static class Builder<T> {

    private final Vec3i masterOffset;
    private Deque<List<CharList>> pattern = new ArrayDeque<>();
    private final ImmutableMap.Builder<Character, BlockPredicate> predicates =
        ImmutableMap.builder();
    @Nullable
    private AABB entityCheckBounds;
    @Nullable
    private T metadata;

    private Builder(Vec3i masterOffset) {
      this.masterOffset = masterOffset;
    }

    /**
     * Appends a horizontal cross-sectional layer of the structure.
     * 
     * @param layer - a {@link List} of horizontal rows of blocks.
     * @return the builder
     */
    public Builder<T> layer(List<CharList> layer) {
      this.pattern.push(layer);
      return this;
    }

    /**
     * Resets the pattern to the specified one.
     * 
     * @param pattern - the pattern to set
     * @return the builder
     */
    public Builder<T> pattern(Collection<List<CharList>> pattern) {
      this.pattern = new ArrayDeque<>(pattern);
      return this;
    }

    /**
     * Defines a {@link BlockPredicate} for the specified pattern marker.
     * 
     * @param marker - the marker to define the predicate for
     * @param predicate - the predicate
     * @return the builder
     */
    public Builder<T> predicate(char marker, BlockPredicate predicate) {
      this.predicates.put(marker, predicate);
      return this;
    }

    /**
     * Defines a bounding box which will be checked for living entities upon pattern resolution, if
     * entities are detected within the specified bounds the pattern will fail to resolve.
     * 
     * @param entityCheckBounds - the bounds to check for entities in
     * @return the builder
     */
    public Builder<T> entityCheckBounds(AABB entityCheckBounds) {
      this.entityCheckBounds = entityCheckBounds;
      return this;
    }

    public Builder<T> metadata(T metadata) {
      this.metadata = metadata;
      return this;
    }

    /**
     * Creates a new {@link MultiblockPattern} using the attributes defined by this builder.
     * 
     * @return the created {@link MultiblockPattern}
     */
    public MultiblockPattern<T> build() {
      var patternArray = this.pattern.stream()
          .map(layer -> layer.stream()
              .map(CharList::toCharArray)
              .toArray(char[][]::new))
          .toArray(char[][][]::new);
      return new MultiblockPattern<>(patternArray, this.masterOffset, this.predicates.build(),
          this.entityCheckBounds, this.metadata);
    }
  }
}
