package mods.railcraft.util.container;

import org.jetbrains.annotations.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

public final class ItemHandlerFactory {

  public static IItemHandlerModifiable wrap(Container inventory, @Nullable Direction side) {
    if (inventory instanceof WorldlyContainer && side != null) {
      return new SidedInvWrapper((WorldlyContainer) inventory, side);
    }
    return new InvWrapper(inventory);
  }

  private ItemHandlerFactory() {}
}
