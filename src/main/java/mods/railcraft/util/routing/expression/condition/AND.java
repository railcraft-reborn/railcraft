package mods.railcraft.util.routing.expression.condition;

import mods.railcraft.util.routing.IBlockEntityRouting;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class AND implements Condition {

  private final Condition a, b;

  public AND(Condition a, Condition b) {
    this.a = a;
    this.b = b;
  }

  @Override
  public boolean matches(IBlockEntityRouting blockEntityRouting, AbstractMinecart cart) {
    return a.matches(blockEntityRouting, cart) && b.matches(blockEntityRouting, cart);
  }
}
