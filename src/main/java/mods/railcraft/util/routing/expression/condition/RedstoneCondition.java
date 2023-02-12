package mods.railcraft.util.routing.expression.condition;

import mods.railcraft.util.routing.IBlockEntityRouting;
import mods.railcraft.util.routing.RoutingLogicException;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class RedstoneCondition extends ParsedCondition {

  private final boolean powered;

  public RedstoneCondition(String line) throws RoutingLogicException {
    super("Redstone", false, line);
    this.powered = Boolean.parseBoolean(value);
  }

  @Override
  public boolean matches(IBlockEntityRouting blockEntityRouting, AbstractMinecart cart) {
    return powered == blockEntityRouting.isPowered();
  }
}
