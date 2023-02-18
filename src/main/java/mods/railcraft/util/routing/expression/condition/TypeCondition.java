package mods.railcraft.util.routing.expression.condition;

import mods.railcraft.util.routing.RouterBlockEntity;
import mods.railcraft.util.routing.RoutingLogicException;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.registries.ForgeRegistries;

public class TypeCondition extends ParsedCondition {

  public TypeCondition(String line) throws RoutingLogicException {
    super("Type", false, line);
  }

  @Override
  public boolean matches(RouterBlockEntity routerBlockEntity, AbstractMinecart cart) {
    var cartType = ForgeRegistries.ENTITY_TYPES.getKey(cart.getType()).toString();
    return value.equalsIgnoreCase(cartType);
  }
}
