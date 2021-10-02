package mods.railcraft.season;

import java.util.Calendar;
import mods.railcraft.Railcraft;
import mods.railcraft.carts.RailcraftCart;
import mods.railcraft.client.ClientDist;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;

/**
 * Created by CovertJaguar on 10/7/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class Seasons {

  public static final boolean HARVEST;
  public static final boolean HALLOWEEN;
  public static final boolean CHRISTMAS;
  public static final String GHOST_TRAIN = "Ghost Train";
  public static final String POLAR_EXPRESS = "Polar Express";

  static {
    if (Railcraft.commonConfig.enableSeasons.get()) {
      Calendar cal = Calendar.getInstance();
      int month = cal.get(Calendar.MONTH);
      int day = cal.get(Calendar.DAY_OF_MONTH);

      switch (Railcraft.commonConfig.christmas.get()) {
        case 1:
          CHRISTMAS = true;
          break;
        case 2:
          CHRISTMAS = false;
          break;
        default:
          CHRISTMAS = month == Calendar.DECEMBER || month == Calendar.JANUARY;
          break;
      }

      switch (Railcraft.commonConfig.harvest.get()) {
        case 1:
          HARVEST = true;
          break;
        case 2:
          HARVEST = false;
          break;
        default:
          HARVEST = month == Calendar.OCTOBER || month == Calendar.NOVEMBER;
          break;
      }

      switch (Railcraft.commonConfig.halloween.get()) {
        case 1:
          HALLOWEEN = true;
          break;
        case 2:
          HALLOWEEN = false;
          break;
        default:
          HALLOWEEN =
              (month == Calendar.OCTOBER && day >= 21) || (month == Calendar.NOVEMBER && day <= 10);
          break;
      }

    } else {
      HARVEST = false;
      HALLOWEEN = false;
      CHRISTMAS = false;
    }
  }

  public static boolean isGhostTrain(AbstractMinecartEntity cart) {
    Season season =
        cart instanceof RailcraftCart ? ((RailcraftCart) cart).getSeason() : Season.DEFAULT;
    if (season == Season.DEFAULT)
      return (ClientDist.clientConfig.enableGhostTrain.get() && HALLOWEEN)
          || cart.hasCustomName() && GHOST_TRAIN.equals(cart.getCustomName().getContents());
    return season == Season.HALLOWEEN;
  }

  public static boolean isPolarExpress(AbstractMinecartEntity cart) {
    Season season =
        cart instanceof RailcraftCart ? ((RailcraftCart) cart).getSeason() : Season.DEFAULT;
    if (season == Season.DEFAULT)
      return (ClientDist.clientConfig.enablePolarExpress.get() && CHRISTMAS)
          || cart.hasCustomName() && POLAR_EXPRESS.equals(cart.getCustomName().getContents())
          || cart.level.getBiome(cart.blockPosition()).shouldSnow(cart.level, cart.blockPosition());
    return season == Season.CHRISTMAS;
  }

}
