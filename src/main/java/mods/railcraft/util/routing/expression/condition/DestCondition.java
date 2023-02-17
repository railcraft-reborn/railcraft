package mods.railcraft.util.routing.expression.condition;

import org.apache.commons.lang3.StringUtils;
import mods.railcraft.api.carts.Routable;
import mods.railcraft.util.routing.IBlockEntityRouting;
import mods.railcraft.util.routing.RoutingLogicException;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class DestCondition extends ParsedCondition {

  public DestCondition(String line) throws RoutingLogicException {
    super("Dest", true, line);
  }

  @Override
  public boolean matches(IBlockEntityRouting blockEntityRouting, AbstractMinecart cart) {
    if (cart instanceof Routable routableCart) {
      String cartDest = routableCart.getDestination();
      if (StringUtils.equalsIgnoreCase("null", value)) {
        return StringUtils.isBlank(cartDest);
      }
      if (StringUtils.isBlank(cartDest)) {
        return false;
      }
      if (isRegex) {
        return cartDest.matches(value);
      }
      return cartDest.startsWith(value);
    }
    return false;
  }
}
