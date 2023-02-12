package mods.railcraft.util.routing.expression;

import mods.railcraft.util.routing.IBlockEntityRouting;
import mods.railcraft.util.routing.expression.condition.Condition;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class IF implements Expression {

  private final Condition cond;
  private final Expression then, else_;

  public IF(Condition cond, Expression then, Expression else_) {
    this.cond = cond;
    this.then = then;
    this.else_ = else_;
  }

  @Override
  public int evaluate(IBlockEntityRouting blockEntityRouting, AbstractMinecart cart) {
    return (cond.matches(blockEntityRouting, cart) ? then : else_)
        .evaluate(blockEntityRouting, cart);
  }
}
