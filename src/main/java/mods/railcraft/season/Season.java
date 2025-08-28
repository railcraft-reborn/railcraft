package mods.railcraft.season;

import mods.railcraft.Translations;
import mods.railcraft.api.util.EnumUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public enum Season implements StringRepresentable {

  DEFAULT(Translations.Season.DEFAULT),
  HALLOWEEN(Translations.Season.HALLOWEEN),
  CHRISTMAS(Translations.Season.CHRISTMAS),
  NONE(Translations.Season.NONE);

  public static final StringRepresentable.EnumCodec<Season> CODEC =
      StringRepresentable.fromEnum(Season::values);
  public static final StreamCodec<FriendlyByteBuf, Season> STREAM_CODEC =
      NeoForgeStreamCodecs.enumCodec(Season.class);
  private final String name;
  private final String translationKey;

  Season(String translationKey) {
    this.translationKey = translationKey;
    this.name = translationKey.substring(translationKey.lastIndexOf('.') + 1);
  }

  public Component getDisplayName() {
    return Component.translatable(this.translationKey);
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }

  public Season getNext() {
    return EnumUtil.next(this, values());
  }
}
