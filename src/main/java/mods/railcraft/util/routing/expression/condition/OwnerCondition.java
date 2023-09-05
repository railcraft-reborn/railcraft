package mods.railcraft.util.routing.expression.condition;

import com.mojang.authlib.GameProfile;
import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.util.routing.RoutingStatementParser;
import mods.railcraft.util.routing.expression.Expression;

public class OwnerCondition {

  public static final String KEYWORD = "Owner";

  public static Expression parse(String line) throws RoutingLogicException {
    var statement = RoutingStatementParser.parse(KEYWORD, false, line);
    return (router, rollingStock) -> rollingStock.owner()
        .map(GameProfile::getName)
        .filter(statement.value()::equalsIgnoreCase)
        .isPresent();
  }
}
