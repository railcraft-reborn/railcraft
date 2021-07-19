package mods.railcraft.util.inventory;

import javax.annotation.Nullable;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.Direction;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public final class ItemHandlerFactory {

  public static IItemHandlerModifiable wrap(IInventory inventory, @Nullable Direction side) {
    if (inventory instanceof ISidedInventory && side != null) {
      return new SidedInvWrapper((ISidedInventory) inventory, side);
    }
    return new InvWrapper(inventory);
  }

  private ItemHandlerFactory() {}
}
