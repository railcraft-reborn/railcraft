package mods.railcraft.world.inventory;

import mods.railcraft.world.level.block.entity.ManipulatorBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

public class ManipulatorMenu<T extends ManipulatorBlockEntity> extends RailcraftMenu {

  private T manipulator;
  private final boolean hasCartFilter;

  protected ManipulatorMenu(MenuType<?> type, int id, Inventory inventory,
      T manipulator) {
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

    if (inventory != null) {
      for (int i = 0; i < 3; i++) {
        for (int k = 0; k < 9; k++) {
          this.addSlot(new Slot(inventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
        }
      }

      for (int j = 0; j < 9; j++) {
        this.addSlot(new Slot(inventory, j, 8 + j * 18, 142));
      }
    }
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
