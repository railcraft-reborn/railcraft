package mods.railcraft.util.routing.expression.condition;

import mods.railcraft.util.PowerUtil;
import mods.railcraft.util.routing.RouterBlockEntity;
import mods.railcraft.util.routing.expression.Expression;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public interface Condition extends Expression {

  @Override
  default int evaluate(RouterBlockEntity routerBlockEntity, AbstractMinecart cart) {
    return matches(routerBlockEntity, cart) ? PowerUtil.FULL_POWER : PowerUtil.NO_POWER;
  }

  boolean matches(RouterBlockEntity routerBlockEntity, AbstractMinecart cart);
}
