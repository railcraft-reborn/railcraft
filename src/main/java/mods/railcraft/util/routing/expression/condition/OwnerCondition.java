package mods.railcraft.util.routing.expression.condition;

import org.apache.commons.lang3.StringUtils;
import mods.railcraft.util.routing.IBlockEntityRouting;
import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.world.entity.vehicle.CartTools;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class OwnerCondition extends ParsedCondition {

  public OwnerCondition(String line) throws RoutingLogicException {
    super("Owner", false, line);
  }

  @Override
  public boolean matches(IBlockEntityRouting blockEntityRouting, AbstractMinecart cart) {
    var owner = CartTools.getCartOwner(cart);
    if (owner == null) {
      return false;
    }
    return StringUtils.equalsIgnoreCase(value, owner.getName());
  }
}
