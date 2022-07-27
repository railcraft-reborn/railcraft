package mods.railcraft.api.carts;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.util.StringRepresentable;

public enum Link implements StringRepresentable {

  FRONT("front"), BACK("back");

  private static final Map<String, Link> byName = Arrays.stream(values())
      .collect(Collectors.toUnmodifiableMap(Link::getName, Function.identity()));

  private final String name;

  private Link(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public String getSerializedName() {
    return this.getName();
  }

  public static Optional<Link> getByName(String name) {
    return Optional.ofNullable(byName.get(name));
  }
}
