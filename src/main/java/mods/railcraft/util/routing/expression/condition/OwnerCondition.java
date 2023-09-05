package mods.railcraft.util.routing.expression.condition;

import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.util.routing.RoutingStatementParser;
import mods.railcraft.util.routing.expression.Expression;
import mods.railcraft.world.entity.vehicle.CartTools;

public class OwnerCondition {

  public static final String KEYWORD = "Owner";

  public static Expression parse(String line) throws RoutingLogicException {
    var statement = RoutingStatementParser.parse(KEYWORD, false, line);
    return (router, minecart) -> {
      var owner = CartTools.getCartOwner(minecart);
      return owner != null && statement.value().equalsIgnoreCase(owner.getName());
    };
  }
}
