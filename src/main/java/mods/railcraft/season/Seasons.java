package mods.railcraft.season;

import java.util.Calendar;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.world.entity.vehicle.SeasonalCart;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public final class Seasons {

  public static final boolean HARVEST;
  public static final boolean HALLOWEEN;
  public static final boolean CHRISTMAS;
  public static final String GHOST_TRAIN = "Ghost Train";
  public static final String POLAR_EXPRESS = "Polar Express";

  static {
    if (RailcraftConfig.COMMON.seasonsEnabled.get()) {
      var cal = Calendar.getInstance();
      int month = cal.get(Calendar.MONTH);
      int day = cal.get(Calendar.DAY_OF_MONTH);

      CHRISTMAS = switch (RailcraftConfig.COMMON.christmas.get()) {
        case 1 ->  true;
        case 2 ->  false;
        default -> (month == Calendar.DECEMBER) || (month == Calendar.JANUARY);
      };

      HARVEST = switch (RailcraftConfig.COMMON.harvest.get()) {
        case 1 -> true;
        case 2 -> false;
        default -> (month == Calendar.OCTOBER) || (month == Calendar.NOVEMBER);
      };

      HALLOWEEN = switch (RailcraftConfig.COMMON.halloween.get()) {
        case 1 -> true;
        case 2 -> false;
        default ->
            (month == Calendar.OCTOBER && day >= 21) || (month == Calendar.NOVEMBER && day <= 10);
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
          || cart.hasCustomName() && GHOST_TRAIN.equals(cart.getCustomName().getContents());
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
    var level = cart.getLevel();
    return season == Season.DEFAULT
        ? (RailcraftConfig.CLIENT.polarExpressEnabled.get() && CHRISTMAS)
            || cart.hasCustomName() && POLAR_EXPRESS.equals(cart.getCustomName().getContents())
            || level.getBiome(cart.blockPosition()).value().shouldSnow(level, cart.blockPosition())
        : season == Season.CHRISTMAS;
  }
}
