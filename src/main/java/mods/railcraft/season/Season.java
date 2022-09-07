package mods.railcraft.season;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import mods.railcraft.Translations.Tips;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

public enum Season implements StringRepresentable {

  DEFAULT("default", Tips.CRAWBAR_SEASON_DEFAULT),
  HALLOWEEN("halloween", Tips.CRAWBAR_SEASON_HALLOWEEN),
  CHRISTMAS("christmas", Tips.CRAWBAR_SEASON_CHRISTMAS),
  NONE("none", Tips.CRAWBAR_SEASON_NONE);

  private static final Map<String, Season> byName = Arrays.stream(values())
      .collect(Collectors.toUnmodifiableMap(Season::getSerializedName, Function.identity()));
  private final String name;
  private final Component displayName;

  private Season(String name, String translationKey) {
    this.name = name;
    this.displayName = Component.translatable(translationKey);
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
