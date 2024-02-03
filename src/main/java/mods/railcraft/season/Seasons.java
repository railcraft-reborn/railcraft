package mods.railcraft.season;

import java.time.LocalDate;
import java.time.Month;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.world.entity.vehicle.SeasonalCart;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public final class Seasons {

  private static final boolean HARVEST;
  private static final boolean HALLOWEEN;
  private static final boolean CHRISTMAS;
  public static final String GHOST_TRAIN = "Ghost Train";
  public static final String POLAR_EXPRESS = "Polar Express";

  static {
    if (RailcraftConfig.COMMON.seasonsEnabled.get()) {
      var date = LocalDate.now();
      var month = date.getMonth();
      var day = date.getDayOfMonth();

      CHRISTMAS = switch (RailcraftConfig.COMMON.christmas.get()) {
        case 1 -> true;
        case 2 -> false;
        default -> (month == Month.DECEMBER) || (month == Month.JANUARY);
      };

      HARVEST = switch (RailcraftConfig.COMMON.harvest.get()) {
        case 1 -> true;
        case 2 -> false;
        default -> (month == Month.OCTOBER) || (month == Month.NOVEMBER);
      };

      HALLOWEEN = switch (RailcraftConfig.COMMON.halloween.get()) {
        case 1 -> true;
        case 2 -> false;
        default -> (month == Month.OCTOBER && day >= 21)
            || (month == Month.NOVEMBER && day <= 10);
      };
    } else {
      HARVEST = false;
      HALLOWEEN = false;
      CHRISTMAS = false;
    }
  }

  /**
   * Clientside only.
   *
   * @param cart The cart.
   */
  public static boolean isGhostTrain(AbstractMinecart cart) {
    var season = cart instanceof SeasonalCart seasonal ? seasonal.getSeason() : Season.DEFAULT;
    if (season == Season.DEFAULT) {
      return (RailcraftConfig.CLIENT.ghostTrainEnabled.get() && HALLOWEEN)
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
    var season = cart instanceof SeasonalCart seasonal ? seasonal.getSeason() : Season.DEFAULT;
    var level = cart.level();
    return season == Season.DEFAULT
        ? (RailcraftConfig.CLIENT.polarExpressEnabled.get() && CHRISTMAS)
            || cart.hasCustomName() && POLAR_EXPRESS.equals(cart.getCustomName().getString())
            || level.getBiome(cart.blockPosition()).value().shouldSnow(level, cart.blockPosition())
        : season == Season.CHRISTMAS;
  }

  /**
   * Clientside only.
   */
  public static boolean isHalloween() {
    return HALLOWEEN;
  }
}
