package mods.railcraft.util.routing;

import net.minecraft.world.item.ItemStack;

public interface IBlockEntityRouting {

  ItemStack getRoutingTable();

  void setRoutingTable(ItemStack stack);

  boolean isPowered();
}
