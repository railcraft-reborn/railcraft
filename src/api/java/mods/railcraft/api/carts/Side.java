package mods.railcraft.api.carts;

import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.util.StringRepresentable;

public enum Side implements StringRepresentable {

  FRONT("front"), BACK("back");

  private final String name;

  private Side(String name) {
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

  public String getName() {
    return this.name;
  }

  @Override
  public String getSerializedName() {
    return this.getName();
  }

  public static Optional<Side> getByName(String name) {
    return Stream.of(values())
        .filter(link -> link.name.equals(name))
        .findFirst();
  }
}
