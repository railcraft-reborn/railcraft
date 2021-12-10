package mods.railcraft.util.container;

import javax.annotation.Nullable;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.core.Direction;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public final class ItemHandlerFactory {

  public static IItemHandlerModifiable wrap(Container inventory, @Nullable Direction side) {
    if (inventory instanceof WorldlyContainer && side != null) {
      return new SidedInvWrapper((WorldlyContainer) inventory, side);
    }
    return new InvWrapper(inventory);
  }

  private ItemHandlerFactory() {}
}
