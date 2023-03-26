package mods.railcraft.util.routing.expression;

import mods.railcraft.util.PowerUtil;
import mods.railcraft.util.routing.RouterBlockEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class ConstantExpression implements Expression {

  private final int value;

  public ConstantExpression(int value) {
    if (value < PowerUtil.NO_POWER || value > PowerUtil.FULL_POWER) {
      throw new IllegalArgumentException("Illegal constant value");
    }
    this.value = value;
  }

  @Override
  public int evaluate(RouterBlockEntity routerBlockEntity, AbstractMinecart cart) {
    return value;
  }
}
