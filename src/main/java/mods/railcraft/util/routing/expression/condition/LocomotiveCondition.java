package mods.railcraft.util.routing.expression.condition;

import java.util.Locale;
import java.util.function.Predicate;
import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.util.routing.RoutingStatementParser;
import mods.railcraft.util.routing.expression.Expression;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;

public class LocomotiveCondition {

  public static final String KEYWORD = "Loco";

  public static Expression parse(String line) throws RoutingLogicException {
    var statement = RoutingStatementParser.parse(KEYWORD, false, line);
    Predicate<Locomotive> predicate = switch (statement.value().toLowerCase(Locale.ROOT)) {
      case "electric" -> loco -> loco.getType() == RailcraftEntityTypes.ELECTRIC_LOCOMOTIVE.get();
      case "steam" -> loco -> loco.getType() == RailcraftEntityTypes.STEAM_LOCOMOTIVE.get();
      case "creative" -> loco -> loco.getType() == RailcraftEntityTypes.CREATIVE_LOCOMOTIVE.get();
      case "none" -> null;
      default -> throw new IllegalArgumentException("Unexpected value: " + statement.value());
    };
    return (router, minecart) -> {
      if (minecart instanceof Locomotive loco && predicate != null) {
        return predicate.test(loco);
      }
      return true;

    };
  }
}
