package mods.railcraft.api.track;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;

public enum ArrowDirection implements StringRepresentable {

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
    return switch (direction) {
      case NORTH -> NORTH;
      case SOUTH -> SOUTH;
      case EAST -> EAST;
      case WEST -> WEST;
      default -> throw new IllegalStateException("Not a horizontal direction.");
    };
  }
}
