package mods.railcraft.util.routing.expression;

import mods.railcraft.util.routing.RouterBlockEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

@FunctionalInterface
public interface Expression {

  Expression FALSE = (router, cart) -> false;
  Expression TRUE = (router, cart) -> true;

  boolean evaluate(RouterBlockEntity router, AbstractMinecart cart);

  default Expression negate() {
    return (router, cart) -> !this.evaluate(router, cart);
  }

  default Expression and(Expression other) {
    return (router, cart) -> this.evaluate(router, cart) && other.evaluate(router, cart);
  }

  default Expression or(Expression other) {
    return (router, cart) -> this.evaluate(router, cart) || other.evaluate(router, cart);
  }

  default Expression select(Expression success, Expression fail) {
    return (router, cart) -> this.evaluate(router, cart)
        ? success.evaluate(router, cart)
        : fail.evaluate(router, cart);
  }
}
