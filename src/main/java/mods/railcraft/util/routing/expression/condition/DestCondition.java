package mods.railcraft.util.routing.expression.condition;

import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;
import mods.railcraft.api.carts.Routable;
import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.util.routing.RoutingStatementParser;
import mods.railcraft.util.routing.expression.Expression;

public class DestCondition {

  public static final String KEYWORD = "Dest";

  public static Expression parse(String line) throws RoutingLogicException {
    var statement = RoutingStatementParser.parse(KEYWORD, true, line);
    Predicate<String> predicate = statement.isRegex()
        ? statement.pattern().asMatchPredicate()
        : s -> s.startsWith(statement.value());
    return (router, minecart) -> {
      if (minecart instanceof Routable routable) {
        var destination = routable.getDestination();
        if (StringUtils.equalsIgnoreCase("null", statement.value())) {
          return StringUtils.isBlank(destination);
        }
        if (StringUtils.isBlank(destination)) {
          return false;
        }
        return predicate.test(destination);
      }
      return false;
    };
  }
}
