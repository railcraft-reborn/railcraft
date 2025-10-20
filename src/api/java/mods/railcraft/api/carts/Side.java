package mods.railcraft.api.carts;

import net.minecraft.util.StringRepresentable;

public enum Side implements StringRepresentable {

  FRONT("front"),
  BACK("back");

  private final String name;

  Side(String name) {
    this.name = name;
  }

  public Side opposite() {
    return switch (this) {
      case FRONT -> BACK;
      case BACK -> FRONT;
    };
  }

  public boolean isFront() {
    return this == FRONT;
  }

  public boolean isBack() {
    return this == BACK;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }
}
