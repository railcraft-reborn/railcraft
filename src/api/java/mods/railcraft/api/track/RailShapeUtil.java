package mods.railcraft.api.track;

import net.minecraft.world.level.block.state.properties.RailShape;

public final class RailShapeUtil {

  private RailShapeUtil() {}

  public static boolean isLevelStraight(RailShape railShape) {
    return railShape == RailShape.NORTH_SOUTH || railShape == RailShape.EAST_WEST;
  }

  public static boolean isEastWest(RailShape railShape) {
    return (railShape == RailShape.EAST_WEST
        || railShape == RailShape.ASCENDING_EAST
        || railShape == RailShape.ASCENDING_WEST);
  }

  public static boolean isNorthSouth(RailShape railShape) {
    return (railShape == RailShape.NORTH_SOUTH
        || railShape == RailShape.ASCENDING_NORTH
        || railShape == RailShape.ASCENDING_SOUTH);
  }

  public static boolean isTurn(RailShape railShape) {
    return railShape == RailShape.SOUTH_EAST
        || railShape == RailShape.SOUTH_WEST
        || railShape == RailShape.NORTH_WEST
        || railShape == RailShape.NORTH_EAST;
  }
}
