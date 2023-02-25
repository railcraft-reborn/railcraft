package mods.railcraft.util.routing.expression.condition;

import java.util.Locale;
import mods.railcraft.Translations;
import mods.railcraft.api.carts.Paintable;
import mods.railcraft.util.routing.RouterBlockEntity;
import mods.railcraft.util.routing.RoutingLogicException;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.DyeColor;

public class ColorCondition extends ParsedCondition {

  private final DyeColor primary, secondary;

  public ColorCondition(String line) throws RoutingLogicException {
    super("Color", false, line);
    var colors = value.toLowerCase(Locale.ROOT).split(",");
    if ("Any".equals(colors[0]) || "*".equals(colors[0])) {
      primary = null;
    } else {
      primary = DyeColor.byName(colors[0], null);
      if (primary == null) {
        throw new RoutingLogicException(Translations.RoutingTable.UNRECOGNIZED_KEYWORD, colors[0]);
      }
    }
    if (colors.length == 1 || colors[1].equals("Any") || colors[1].equals("*")) {
      secondary = null;
    } else {
      secondary = DyeColor.byName(colors[1], null);
      if (secondary == null) {
        throw new RoutingLogicException(Translations.RoutingTable.UNRECOGNIZED_KEYWORD, colors[1]);
      }
    }
  }

  @Override
  public boolean matches(RouterBlockEntity routerBlockEntity, AbstractMinecart cart) {
    if (cart instanceof Paintable paintedCart) {
      return (primary == null || primary.equals(paintedCart.getPrimaryDyeColor())) &&
          (secondary == null || secondary.equals(paintedCart.getSecondaryDyeColor()));
    }
    return false;
  }
}
