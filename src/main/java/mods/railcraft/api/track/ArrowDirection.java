package mods.railcraft.api.track;

import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;

public enum ArrowDirection implements IStringSerializable {

  NORTH("north"), SOUTH("south"), EAST("east"), WEST("west"), NORTH_SOUTH("north_south"),
  EAST_WEST("east_west");

  private final String name;

  private ArrowDirection(String name) {
    this.name = name;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }

  public static ArrowDirection fromHorizontalDirection(Direction direction) {
    switch (direction) {
      case NORTH:
        return NORTH;
      case SOUTH:
        return SOUTH;
      case EAST:
        return EAST;
      case WEST:
        return WEST;
      default:
        throw new IllegalStateException("Not a horizontal direction.");
    }
  }
}
