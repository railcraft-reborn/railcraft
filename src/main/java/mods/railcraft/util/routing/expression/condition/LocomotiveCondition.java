package mods.railcraft.util.routing.expression.condition;

import mods.railcraft.util.routing.RouterBlockEntity;
import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class LocomotiveCondition extends ParsedCondition {

  public LocomotiveCondition(String line) throws RoutingLogicException {
    super("Loco", false, line);
  }

  @Override
  public boolean evaluate(RouterBlockEntity routerBlockEntity, AbstractMinecart cart) {
    if (cart instanceof Locomotive loco) {
      if (value.equalsIgnoreCase("Electric")) {
        return loco.getType() == RailcraftEntityTypes.ELECTRIC_LOCOMOTIVE.get();
      }
      if (value.equalsIgnoreCase("Steam")) {
        return loco.getType() == RailcraftEntityTypes.STEAM_LOCOMOTIVE.get();
      }
      if (value.equalsIgnoreCase("Creative")) {
        return loco.getType() == RailcraftEntityTypes.CREATIVE_LOCOMOTIVE.get();
      }
      if (value.equalsIgnoreCase("None")) {
        return false;
      }
    }
    return value.equalsIgnoreCase("None");
  }
}
