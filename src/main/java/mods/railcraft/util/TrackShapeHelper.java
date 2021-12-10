package mods.railcraft.util;

import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.properties.RailShape;

/**
 * Created by CovertJaguar on 3/29/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public final class TrackShapeHelper {

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
