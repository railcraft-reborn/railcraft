package mods.railcraft.util.routing.expression.condition;

import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.util.routing.RoutingStatementParser;
import mods.railcraft.util.routing.expression.Expression;
import net.minecraft.core.registries.BuiltInRegistries;

public class TypeCondition {

  public static final String KEYWORD = "Type";

  public static Expression parse(String line) throws RoutingLogicException {
    var statement = RoutingStatementParser.parse(KEYWORD, false, line);
    return (router, rollingStock) -> statement.value().equalsIgnoreCase(
        BuiltInRegistries.ENTITY_TYPE.getKey(rollingStock.entity().getType()).toString());
  }
}
