package mods.railcraft.util.routing.expression;

import mods.railcraft.util.routing.RouterBlockEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

@FunctionalInterface
public interface Expression {

  Expression FALSE = (router, minecart) -> false;
  Expression TRUE = (router, minecart) -> true;

  boolean evaluate(RouterBlockEntity router, AbstractMinecart minecart);

  default Expression negate() {
    return (router, minecart) -> !this.evaluate(router, minecart);
  }

  default Expression and(Expression other) {
    return (router, minecart) -> this.evaluate(router, minecart)
        && other.evaluate(router, minecart);
  }

  default Expression or(Expression other) {
    return (router, minecart) -> this.evaluate(router, minecart)
        || other.evaluate(router, minecart);
  }

  default Expression select(Expression success, Expression fail) {
    return (router, minecart) -> this.evaluate(router, minecart)
        ? success.evaluate(router, minecart)
        : fail.evaluate(router, minecart);
  }
}
