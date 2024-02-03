package mods.railcraft.season;

import mods.railcraft.Translations;
import mods.railcraft.api.util.EnumUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

public enum Season implements StringRepresentable {

  DEFAULT("default"),
  HALLOWEEN("halloween"),
  CHRISTMAS("christmas"),
  NONE("none");

  public static final StringRepresentable.EnumCodec<Season> CODEC =
      StringRepresentable.fromEnum(Season::values);
  private final String name;

  Season(String name) {
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
  public static Season fromName(String name) {
    return CODEC.byName(name, DEFAULT);
  }
}
