package mods.railcraft.season;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public enum Season implements IStringSerializable {

  DEFAULT("default"),
  HALLOWEEN("halloween"),
  CHRISTMAS("christmas"),
  NONE("none");

  private static final Map<String, Season> byName = Arrays.stream(values())
      .collect(Collectors.toMap(Season::getSerializedName, Function.identity()));

  private final String name;
  private final ITextComponent displayName;

  private Season(String name) {
    this.name = name;
    this.displayName = new TranslationTextComponent("season." + name);
  }

  public ITextComponent getDisplayName() {
    return this.displayName;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }

  public boolean equals(Season season) {
    return (this.name == season.name);
  }

  public Season getNext() {
    return values()[(this.ordinal() + 1) % values().length];
  }

  public static Optional<Season> getByName(String name) {
    return Optional.ofNullable(byName.get(name));
  }
}
