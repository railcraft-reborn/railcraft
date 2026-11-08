package mods.railcraft.season;

import java.time.LocalDate;
import java.time.Month;
import org.jspecify.annotations.Nullable;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.client.renderer.entity.state.RailcraftMinecartRenderState;
import mods.railcraft.world.entity.vehicle.SeasonalCart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.Level;

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
   */
  public static boolean isGhostTrain(MinecartRenderState state) {
    return isGhostTrain(getSeason(state), state.nameTag);
  }

  /**
   * Clientside only.
   */
  public static boolean isGhostTrain(AbstractMinecart cart) {
    var season = cart instanceof SeasonalCart seasonal ? seasonal.getSeason() : Season.DEFAULT;
    return isGhostTrain(season, cart.getCustomName());
  }

  private static boolean isGhostTrain(Season season, @Nullable Component nameTag) {
    if (!RailcraftConfig.CLIENT.seasonsEnabled.get()) {
      return false;
    }
    if (season == Season.DEFAULT) {
      return (RailcraftConfig.CLIENT.ghostTrainEnabled.get() && isHalloween())
          || (nameTag != null && GHOST_TRAIN.equals(nameTag.getString()));
    }
    return season == Season.HALLOWEEN;
  }

  /**
   * Clientside only.
   */
  public static boolean isPolarExpress(MinecartRenderState state) {
    var season = getSeason(state);
    var level = Minecraft.getInstance().level;
    var pos = BlockPos.containing(state.x, state.y, state.z);
    return isPolarExpress(season, state.nameTag, level, pos);
  }

  /**
   * Clientside only.
   */
  public static boolean isPolarExpress(AbstractMinecart cart) {
    var season = cart instanceof SeasonalCart seasonal ? seasonal.getSeason() : Season.DEFAULT;
    var level = cart.level();
    return isPolarExpress(season, cart.getCustomName(), level, cart.blockPosition());
  }

  private static boolean isPolarExpress(Season season, @Nullable Component nameTag,
      Level level, BlockPos pos) {
    if (!RailcraftConfig.CLIENT.seasonsEnabled.get()) {
      return false;
    }
    if (season == Season.DEFAULT) {
      return (RailcraftConfig.CLIENT.polarExpressEnabled.get() && isChristmas())
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
