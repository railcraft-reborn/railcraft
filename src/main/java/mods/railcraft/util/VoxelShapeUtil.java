package mods.railcraft.util;

import java.util.EnumMap;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 *
 * @author Sm0keySa1m0n
 *
 */
public class VoxelShapeUtil {

  public static VoxelShape[] makeShapes(VoxelShape mainShape,
      Map<Direction, VoxelShape> connectionShapes) {
    var shapes = new VoxelShape[64];
    for (int i = 0; i < 64; ++i) {
      var shape = mainShape;
      for (var entry : connectionShapes.entrySet()) {
        if ((i & indexFor(entry.getKey())) != 0) {
          shape = Shapes.or(shape, entry.getValue());
        }
      }
      shapes[i] = shape;
    }
    return shapes;
  }

  public static Map<Direction, VoxelShape> createHorizontalShapes(double minX, double minY,
      double minZ, double maxX, double maxY, double maxZ) {
    return createDirectionalShapes(minX, minY, minZ, maxX, maxY, maxZ, Direction.NORTH,
        Direction.SOUTH, Direction.EAST, Direction.WEST);
  }

  public static Map<Direction, VoxelShape> createDirectionalShapes(double minX, double minY,
      double minZ, double maxX, double maxY, double maxZ, Direction... directions) {
    Map<Direction, VoxelShape> connectionShapes = new EnumMap<>(Direction.class);
    for (int i = 0; i < directions.length; ++i) {
      var direction = directions[i];
      connectionShapes.put(direction, Block.box(
          Math.min(minX, 8.0D + direction.getStepX() * 8.0D),
          Math.min(minY, 8.0D + direction.getStepY() * 8.0D),
          Math.min(minZ, 8.0D + direction.getStepZ() * 8.0D),
          Math.max(maxX, 8.0D + direction.getStepX() * 8.0D),
          Math.max(maxY, 8.0D + direction.getStepY() * 8.0D),
          Math.max(maxZ, 8.0D + direction.getStepZ() * 8.0D)));
    }
    return connectionShapes;
  }

  public static int indexFor(Direction direction) {
    return 1 << direction.get3DDataValue();
  }
}
