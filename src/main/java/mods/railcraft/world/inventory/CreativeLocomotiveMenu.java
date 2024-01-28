package mods.railcraft.world.inventory;

import mods.railcraft.world.entity.vehicle.locomotive.CreativeLocomotive;
import net.minecraft.world.entity.player.Inventory;

public class CreativeLocomotiveMenu extends LocomotiveMenu<CreativeLocomotive> {

  public CreativeLocomotiveMenu(int id, Inventory playerInv, CreativeLocomotive loco) {
    super(RailcraftMenuTypes.CREATIVE_LOCOMOTIVE.get(), id, playerInv, loco);
  }
}
