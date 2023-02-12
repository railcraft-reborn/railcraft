package mods.railcraft.util.routing.expression;

import mods.railcraft.util.routing.IBlockEntityRouting;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public interface Expression {

  int evaluate(IBlockEntityRouting blockEntityRouting, AbstractMinecart cart);
}
