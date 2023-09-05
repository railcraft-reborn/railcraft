package mods.railcraft.util.routing.expression.condition;

import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.util.routing.RoutingStatementParser;
import mods.railcraft.util.routing.expression.Expression;
import net.minecraftforge.registries.ForgeRegistries;

public class TypeCondition {

  public static final String KEYWORD = "Type";

  public static Expression parse(String line) throws RoutingLogicException {
    var statement = RoutingStatementParser.parse(KEYWORD, false, line);
    return (router, minecart) -> statement.value().equalsIgnoreCase(
        ForgeRegistries.ENTITY_TYPES.getKey(minecart.getType()).toString());
  }
}
