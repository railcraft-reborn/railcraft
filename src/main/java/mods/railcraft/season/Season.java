package mods.railcraft.season;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Season implements StringRepresentable {

  DEFAULT("default"),
  HALLOWEEN("halloween"),
  CHRISTMAS("christmas"),
  NONE("none");

  private static final Map<String, Season> byName = Arrays.stream(values())
      .collect(Collectors.toUnmodifiableMap(Season::getSerializedName, Function.identity()));

  private final String name;
  private final Component displayName;

  private Season(String name) {
    this.name = name;
    this.displayName = Component.translatable("season." + name);
  }

  public Component getDisplayName() {
    return this.displayName;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }

  public Season getNext() {
    return values()[(this.ordinal() + 1) % values().length];
  }

  public static Optional<Season> getByName(String name) {
    return Optional.ofNullable(byName.get(name));
  }
}
