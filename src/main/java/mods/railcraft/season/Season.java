package mods.railcraft.season;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import mods.railcraft.Translations;
import mods.railcraft.api.util.EnumUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

public enum Season implements StringRepresentable {

  DEFAULT("default"),
  HALLOWEEN("halloween"),
  CHRISTMAS("christmas"),
  NONE("none");

  private static final Map<String, Season> byName = Arrays.stream(values())
      .collect(Collectors.toUnmodifiableMap(Season::getSerializedName, Function.identity()));
  private final String name;

  private Season(String name) {
    this.name = name;
  }

  public Component getDisplayName() {
    return Component.translatable(this.getTranslationKey());
  }

  public String getTranslationKey() {
    return Translations.makeKey("season", this.name);
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }

  public Season getNext() {
    return EnumUtil.next(this, values());
  }

  public static Optional<Season> getByName(String name) {
    return Optional.ofNullable(byName.get(name));
  }
}
