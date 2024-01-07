package mods.railcraft.util.routing.expression.condition;

import mods.railcraft.api.carts.NeedsFuel;
import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.util.routing.RoutingStatementParser;
import mods.railcraft.util.routing.expression.Expression;

public class RefuelCondition {

  public static final String KEYWORD = "NeedsRefuel";

  public static Expression parse(String line) throws RoutingLogicException {
    var statement = RoutingStatementParser.parse(KEYWORD, false, line);
    var needsRefuel = Boolean.parseBoolean(statement.value());
    return (router, rollingStock) -> rollingStock.train()
        .entities(rollingStock.level())
        .filter(NeedsFuel.class::isInstance)
        .map(NeedsFuel.class::cast)
        .anyMatch(x -> x.needsFuel() == needsRefuel);
  }
}
