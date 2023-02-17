package mods.railcraft.util.routing.expression.condition;

import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.fuel.INeedsFuel;
import mods.railcraft.util.routing.IBlockEntityRouting;
import mods.railcraft.util.routing.RoutingLogicException;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class RefuelCondition extends ParsedCondition {

  private final boolean needsRefuel;

  public RefuelCondition(String line) throws RoutingLogicException {
    super("NeedsRefuel", false, line);
    this.needsRefuel = Boolean.parseBoolean(value);
  }

  @Override
  public boolean matches(IBlockEntityRouting blockEntityRouting, AbstractMinecart cart) {
    return RollingStock.getOrThrow(cart)
        .train()
        .stream()
        .map(RollingStock::entity)
        .filter(x -> x instanceof INeedsFuel)
        .map(x -> (INeedsFuel) x)
        .anyMatch(x -> x.needsFuel() == this.needsRefuel);
  }
}
