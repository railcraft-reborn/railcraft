package mods.railcraft.world.inventory;

import mods.railcraft.world.level.block.entity.manipulator.CartDispenserBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class CartDispenserMenu extends ManipulatorMenu<CartDispenserBlockEntity> {

  public CartDispenserMenu(int id, Inventory inventory,
      CartDispenserBlockEntity manipulator) {
    super(RailcraftMenuTypes.ITEM_MANIPULATOR.get(), id, inventory, manipulator);
  }

  @Override
  protected void addSlots(CartDispenserBlockEntity manipulator) {

  }
}
