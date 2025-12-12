package mods.railcraft.util.container;

import org.jspecify.annotations.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;

public final class ItemHandlerFactory {

  public static ResourceHandler<ItemResource> wrap(Container inventory, @Nullable Direction side) {
    if (inventory instanceof WorldlyContainer && side != null) {
      return new WorldlyContainerWrapper((WorldlyContainer) inventory, side);
    }
    return VanillaContainerWrapper.of(inventory);
  }

  private ItemHandlerFactory() {}
}
