package mods.railcraft.api.tracks;

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
}
