package mods.railcraft.util.routing.expression.condition;

import mods.railcraft.util.routing.RouterBlockEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class AND implements Condition {

  private final Condition a, b;

  public AND(Condition a, Condition b) {
    this.a = a;
    this.b = b;
  }

  @Override
  public boolean matches(RouterBlockEntity routerBlockEntity, AbstractMinecart cart) {
    return a.matches(routerBlockEntity, cart) && b.matches(routerBlockEntity, cart);
  }
}
