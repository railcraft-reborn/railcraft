package mods.railcraft.util.routing.expression.condition;

import mods.railcraft.util.PowerUtil;
import mods.railcraft.util.routing.IBlockEntityRouting;
import mods.railcraft.util.routing.expression.Expression;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public interface Condition extends Expression {

  @Override
  default int evaluate(IBlockEntityRouting blockEntityRouting, AbstractMinecart cart) {
    return matches(blockEntityRouting, cart) ? PowerUtil.FULL_POWER : PowerUtil.NO_POWER;
  }

  boolean matches(IBlockEntityRouting blockEntityRouting, AbstractMinecart cart);
}
