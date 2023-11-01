package mods.railcraft.season;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import mods.railcraft.Translations;
import mods.railcraft.api.util.EnumUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

public enum Season implements StringRepresentable {

  DEFAULT("default"),
  HALLOWEEN("halloween"),
  CHRISTMAS("christmas"),
  NONE("none");

  private static final Map<String, Season> BY_NAME = Arrays.stream(values())
      .collect(Collectors.toUnmodifiableMap(Season::getSerializedName, Function.identity()));

  public static final Codec<Season> CODEC = Codec.STRING.comapFlatMap(Season::read,
      season -> season.name).stable();
  private final String name;

  Season(String name) {
    this.name = name;
  }

  private static DataResult<Season> read(String season) {
    try {
      return DataResult.success(BY_NAME.get(season));
    } catch (Exception e) {
      return DataResult.error(() -> "Not a valid season: " + season);
    }
  }

  public static Optional<Season> getByName(String name) {
    return Optional.ofNullable(BY_NAME.get(name));
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
}
