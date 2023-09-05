package mods.railcraft.util.routing.expression.condition;

import java.util.Locale;
import mods.railcraft.Translations;
import mods.railcraft.api.carts.Paintable;
import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.util.routing.RoutingStatementParser;
import mods.railcraft.util.routing.expression.Expression;
import net.minecraft.world.item.DyeColor;

public class ColorCondition {

  public static final String KEYWORD = "Color";

  public static Expression parse(String line) throws RoutingLogicException {
    var statement = RoutingStatementParser.parse(KEYWORD, false, line);
    var colors = statement.value().toLowerCase(Locale.ROOT).split(",");

    var primary = parseColor(colors[0]);
    var secondary = colors.length < 2 ? null : parseColor(colors[1]);

    return (router, minecart) -> {
      if (minecart instanceof Paintable paintable) {
        return (primary == null || primary.equals(paintable.getPrimaryDyeColor())) &&
            (secondary == null || secondary.equals(paintable.getSecondaryDyeColor()));
      }
      return false;
    };
  }

  private static DyeColor parseColor(String name) throws RoutingLogicException {
    if ("Any".equals(name) || "*".equals(name)) {
      return null;
    }
    var color = DyeColor.byName(name, null);
    if (color == null) {
      throw new RoutingLogicException(Translations.RoutingTable.UNRECOGNIZED_KEYWORD, name);
    }
    return color;
  }
}
