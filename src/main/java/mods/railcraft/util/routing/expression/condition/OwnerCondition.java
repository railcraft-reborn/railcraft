package mods.railcraft.util.routing.expression.condition;

import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.util.routing.RoutingStatementParser;
import mods.railcraft.util.routing.expression.Expression;
import net.minecraft.server.players.NameAndId;

public class OwnerCondition {

  public static final String KEYWORD = "Owner";

  public static Expression parse(String line) throws RoutingLogicException {
    var statement = RoutingStatementParser.parse(KEYWORD, false, line);
    return (router, rollingStock) -> rollingStock.owner()
        .map(NameAndId::name)
        .filter(statement.value()::equalsIgnoreCase)
        .isPresent();
  }
}
