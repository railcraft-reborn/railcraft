package mods.railcraft.util.routing.expression.condition;

import mods.railcraft.util.routing.RouterBlockEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class OR implements Condition {

  private final Condition a, b;

  public OR(Condition a, Condition b) {
    this.a = a;
    this.b = b;
  }

  @Override
  public boolean matches(RouterBlockEntity routerBlockEntity, AbstractMinecart cart) {
    return a.matches(routerBlockEntity, cart) || b.matches(routerBlockEntity, cart);
  }
}
