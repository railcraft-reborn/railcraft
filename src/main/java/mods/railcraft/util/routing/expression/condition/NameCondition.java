package mods.railcraft.util.routing.expression.condition;

import java.util.function.Predicate;
import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.util.routing.RoutingStatementParser;
import mods.railcraft.util.routing.expression.Expression;

public class NameCondition {

  public static final String KEYWORD = "Name";

  public static Expression parse(String line) throws RoutingLogicException {
    var statement = RoutingStatementParser.parse(KEYWORD, true, line);
    Predicate<String> predicate = statement.isRegex()
        ? statement.pattern().asMatchPredicate()
        : statement.value()::equalsIgnoreCase;
    return (router, rollingStock) -> rollingStock.entity().hasCustomName()
        ? predicate.test(rollingStock.entity().getCustomName().getString())
        : statement.value().equalsIgnoreCase("null");
  }
}
