package mods.railcraft.util.routing.expression.condition;

import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.util.routing.RoutingStatementParser;
import mods.railcraft.util.routing.expression.Expression;

public class RedstoneCondition {

  public static final String KEYWORD = "Redstone";;

  public static Expression parse(String line) throws RoutingLogicException {
    var statement = RoutingStatementParser.parse(KEYWORD, false, line);
    var powered = Boolean.parseBoolean(statement.value());
    return (router, rollingStock) -> router.isPowered() == powered;
  }
}
