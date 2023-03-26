package mods.railcraft.util.routing.expression.condition;

import mods.railcraft.util.routing.RouterBlockEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class NOT implements Condition {

  private final Condition a;

  public NOT(Condition a) {
    this.a = a;
  }

  @Override
  public boolean matches(RouterBlockEntity routerBlockEntity, AbstractMinecart cart) {
    return !this.a.matches(routerBlockEntity, cart);
  }
}
