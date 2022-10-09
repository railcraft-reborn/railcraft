package mods.railcraft.world.inventory;

import mods.railcraft.world.inventory.slot.PhantomMinecartSlot;
import mods.railcraft.world.level.block.entity.manipulator.ManipulatorBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

public class ManipulatorMenu<T extends ManipulatorBlockEntity> extends RailcraftMenu {

  private final T manipulator;
  private final boolean hasCartFilter;

  protected ManipulatorMenu(MenuType<?> type, int id, Inventory inventory, T manipulator) {
    this(type, id, inventory, manipulator, true);
  }

  protected ManipulatorMenu(MenuType<?> type, int id, Inventory inventory, T manipulator,
      boolean hasCartFilter) {
    super(type, id, inventory.player, manipulator::stillValid);
    this.manipulator = manipulator;
    this.hasCartFilter = hasCartFilter;

    this.addSlots(manipulator);

    if (hasCartFilter) {
      this.addSlot(new PhantomMinecartSlot(this.manipulator.getCartFilters(), 0, 71, 26));
      this.addSlot(new PhantomMinecartSlot(this.manipulator.getCartFilters(), 1, 89, 26));
    }
    this.addInventorySlots(inventory);
  }

  public T getManipulator() {
    return this.manipulator;
  }

  protected void addSlots(T manipulator) {}

  @Override
  public boolean stillValid(Player player) {
    return this.manipulator.stillValid(player);
  }

  public boolean hasCartFilter() {
    return hasCartFilter;
  }
}
