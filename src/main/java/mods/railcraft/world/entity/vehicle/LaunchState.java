package mods.railcraft.world.entity.vehicle;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.util.StringRepresentable;

public enum LaunchState implements StringRepresentable {

  LAUNCHING("launching"), LAUNCHED("launched"), LANDED("landed");

  private static final Map<String, LaunchState> byName =
      Arrays.stream(values()).collect(Collectors.toMap(LaunchState::getName, Function.identity()));

  private final String name;

  private LaunchState(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public String getSerializedName() {
    return this.getName();
  }

  public static Optional<LaunchState> getByName(String name) {
    return Optional.ofNullable(byName.get(name));
  }
}
