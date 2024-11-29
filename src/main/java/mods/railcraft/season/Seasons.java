package mods.railcraft.season;

import java.time.LocalDate;
import java.time.Month;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.client.renderer.entity.state.RailcraftMinecartRenderState;
import mods.railcraft.world.entity.vehicle.SeasonalCart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;

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
  public static boolean isGhostTrain(MinecartRenderState state) {
    Season season = getSeason(state);
    if (season == Season.DEFAULT) {
      return (RailcraftConfig.CLIENT.ghostTrainEnabled.get() && HALLOWEEN)
          || GHOST_TRAIN.equals(state.nameTag.getString());
    }
    return season == Season.HALLOWEEN;
  }

  /**
   * Clientside only.
   */
  public static boolean isPolarExpress(MinecartRenderState state) {
    var season = getSeason(state);
    var level = Minecraft.getInstance().level;
    var pos = BlockPos.containing(state.posOnRail);
    return isPolarExpress(season, state.nameTag, level, pos);
  }

  /**
   * Clientside only.
   */
  public static boolean isPolarExpress(AbstractMinecart cart) {
    var season = cart instanceof SeasonalCart seasonal ? seasonal.getSeason() : Season.DEFAULT;
    var level = Minecraft.getInstance().level;
    return isPolarExpress(season, cart.getCustomName(), level, cart.blockPosition());
  }

  private static boolean isPolarExpress(Season season, @Nullable Component nameTag,
      Level level, BlockPos pos) {
    if (season == Season.DEFAULT) {
      return (RailcraftConfig.CLIENT.polarExpressEnabled.get() && CHRISTMAS)
          || (nameTag != null && POLAR_EXPRESS.equals(nameTag.getString()))
          || level.getBiome(pos).value().shouldSnow(level, pos);
    }
    return season == Season.CHRISTMAS;
  }

  private static Season getSeason(MinecartRenderState state) {
    if (state instanceof RailcraftMinecartRenderState renderState) {
      return renderState.season;
    }
    return Season.DEFAULT;
  }

  /**
   * Clientside only.
   */
  public static boolean isHalloween() {
    return HALLOWEEN;
  }
}
