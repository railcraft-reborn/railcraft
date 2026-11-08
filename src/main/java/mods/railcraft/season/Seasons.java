package mods.railcraft.season;

import java.time.LocalDate;
import java.time.Month;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.world.entity.vehicle.SeasonalCart;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public final class Seasons {

  public static final String GHOST_TRAIN = "Ghost Train";
  public static final String POLAR_EXPRESS = "Polar Express";

  private Seasons() {}

  /**
   * Evaluated on every call, so config changes apply without restarting.
   */
  private static boolean isChristmas() {
    return switch (RailcraftConfig.CLIENT.christmas.get()) {
      case 1 -> true;
      case 2 -> false;
      default -> {
        var month = LocalDate.now().getMonth();
        yield month == Month.DECEMBER || month == Month.JANUARY;
      }
    };
  }

  /**
   * Clientside only.
   *
   * @param cart The cart.
   */
  public static boolean isGhostTrain(AbstractMinecart cart) {
    if (!RailcraftConfig.CLIENT.seasonsEnabled.get()) {
      return false;
    }
    var season = cart instanceof SeasonalCart seasonal ? seasonal.getSeason() : Season.DEFAULT;
    if (season == Season.DEFAULT) {
      return (RailcraftConfig.CLIENT.ghostTrainEnabled.get() && isHalloween())
          || cart.hasCustomName() && GHOST_TRAIN.equals(cart.getCustomName().getString());
    }
    return season == Season.HALLOWEEN;
  }

  /**
   * Clientside only.
   *
   * @param cart The cart.
   */
  public static boolean isPolarExpress(AbstractMinecart cart) {
    if (!RailcraftConfig.CLIENT.seasonsEnabled.get()) {
      return false;
    }
    var season = cart instanceof SeasonalCart seasonal ? seasonal.getSeason() : Season.DEFAULT;
    var level = cart.level();
    return season == Season.DEFAULT
        ? (RailcraftConfig.CLIENT.polarExpressEnabled.get() && isChristmas())
            || cart.hasCustomName() && POLAR_EXPRESS.equals(cart.getCustomName().getString())
            || level.getBiome(cart.blockPosition()).value().shouldSnow(level, cart.blockPosition())
        : season == Season.CHRISTMAS;
  }

  /**
   * Clientside only. Evaluated on every call, so config changes apply without restarting.
   */
  public static boolean isHalloween() {
    if (!RailcraftConfig.CLIENT.seasonsEnabled.get()) {
      return false;
    }
    return switch (RailcraftConfig.CLIENT.halloween.get()) {
      case 1 -> true;
      case 2 -> false;
      default -> {
        var date = LocalDate.now();
        var month = date.getMonth();
        var day = date.getDayOfMonth();
        yield (month == Month.OCTOBER && day >= 21) || (month == Month.NOVEMBER && day <= 10);
      }
    };
  }
}
