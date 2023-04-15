package mods.railcraft.api.track;

import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.properties.RailShape;

public final class RailShapeUtil {

  private RailShapeUtil() {}

  public static boolean isLevelStraight(@Nullable RailShape dir) {
    return dir != null && dir.ordinal() < 2;
  }

  public static boolean isStraight(@Nullable RailShape dir) {
    return dir != null && dir.ordinal() < 6;
  }

  public static boolean isEastWest(@Nullable RailShape dir) {
    return (dir == RailShape.EAST_WEST || dir == RailShape.ASCENDING_EAST
        || dir == RailShape.ASCENDING_WEST);
  }

  public static boolean isNorthSouth(@Nullable RailShape dir) {
    return (dir == RailShape.NORTH_SOUTH || dir == RailShape.ASCENDING_NORTH
        || dir == RailShape.ASCENDING_SOUTH);
  }

  public static boolean isAscending(@Nullable RailShape dir) {
    return dir != null && dir.isAscending();
  }

  public static boolean isTurn(@Nullable RailShape dir) {
    return dir != null && dir.ordinal() >= 6;
  }
}
