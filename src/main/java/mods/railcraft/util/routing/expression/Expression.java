package mods.railcraft.util.routing.expression;

import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.util.routing.RouterBlockEntity;

@FunctionalInterface
public interface Expression {

  Expression FALSE = (router, rollingStock) -> false;
  Expression TRUE = (router, rollingStock) -> true;

  boolean evaluate(RouterBlockEntity router, RollingStock rollingStock);

  default Expression negate() {
    return (router, rollingStock) -> !this.evaluate(router, rollingStock);
  }

  default Expression and(Expression other) {
    return (router, rollingStock) -> this.evaluate(router, rollingStock)
        && other.evaluate(router, rollingStock);
  }

  default Expression or(Expression other) {
    return (router, rollingStock) -> this.evaluate(router, rollingStock)
        || other.evaluate(router, rollingStock);
  }

  default Expression select(Expression success, Expression fail) {
    return (router, rollingStock) -> this.evaluate(router, rollingStock)
        ? success.evaluate(router, rollingStock)
        : fail.evaluate(router, rollingStock);
  }
}
