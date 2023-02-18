package mods.railcraft.util.routing.expression;

import mods.railcraft.util.routing.RouterBlockEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public interface Expression {

  int evaluate(RouterBlockEntity routerBlockEntity, AbstractMinecart cart);
}
