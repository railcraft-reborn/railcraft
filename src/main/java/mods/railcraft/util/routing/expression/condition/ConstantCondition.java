package mods.railcraft.util.routing.expression.condition;

import mods.railcraft.util.routing.RouterBlockEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class ConstantCondition implements Condition {

  public static final ConstantCondition TRUE = new ConstantCondition(true);
  public static final ConstantCondition FALSE = new ConstantCondition(false);
  private final boolean value;

  private ConstantCondition(boolean value) {
    this.value = value;
  }

  @Override
  public boolean matches(RouterBlockEntity routerBlockEntity, AbstractMinecart cart) {
    return this.value;
  }
}
