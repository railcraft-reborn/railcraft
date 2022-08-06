package mods.railcraft.season;

import mods.railcraft.Translations.Tips;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum Season {

  DEFAULT,
  HALLOWEEN,
  CHRISTMAS,
  NONE;

  public MutableComponent getTranslation() {
    return Component.translatable(switch (this.ordinal()) {
      case 0 -> Tips.CRAWBAR_SEASON_DEFAULT;
      case 1 -> Tips.CRAWBAR_SEASON_HALLOWEEN;
      case 2 -> Tips.CRAWBAR_SEASON_CHRISTMAS;
      case 3 -> Tips.CRAWBAR_SEASON_NONE;
      default -> "translation.not.implemented";
    });
  }

  public Season getNext() {
    return values()[(this.ordinal() + 1) % values().length];
  }
}
