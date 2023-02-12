package mods.railcraft.util.routing.expression.condition;

import org.apache.commons.lang3.StringUtils;
import mods.railcraft.util.routing.IBlockEntityRouting;
import mods.railcraft.util.routing.RoutingLogicException;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class NameCondition extends ParsedCondition {

  public NameCondition(String line) throws RoutingLogicException {
    super("Name", true, line);
  }

  @Override
  public boolean matches(IBlockEntityRouting blockEntityRouting, AbstractMinecart cart) {
    if (!cart.hasCustomName()) {
      return StringUtils.equalsIgnoreCase("null", value);
    }
    String customName = cart.getDisplayName().getString();
    if (isRegex) {
      return customName.matches(value);
    }
    return StringUtils.equalsIgnoreCase(customName, value);
  }
}
