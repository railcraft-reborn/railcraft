package mods.railcraft.world.inventory;

import mods.railcraft.world.level.block.entity.ManipulatorBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;

public class ManipulatorMenu<T extends ManipulatorBlockEntity> extends RailcraftMenu {

  private T manipulator;
  private final boolean hasCartFilter;

  protected ManipulatorMenu(T manipulator, ContainerType<?> type, int id,
      PlayerInventory inventory) {
    this(manipulator, true, type, id, inventory);
  }

  protected ManipulatorMenu(T manipulator, boolean hasCartFilter,
      ContainerType<?> type, int id, PlayerInventory inventory) {
    super(type, id, inventory);
    this.manipulator = manipulator;
    this.hasCartFilter = hasCartFilter;

    addSlots(manipulator);

    if (hasCartFilter) {
      this.addSlot(new SlotMinecartPhantom(this.manipulator.getCartFilters(), 0, 71, 26));
      this.addSlot(new SlotMinecartPhantom(this.manipulator.getCartFilters(), 1, 89, 26));
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
  public boolean stillValid(PlayerEntity player) {
    return this.manipulator.stillValid(player);
  }

  public boolean hasCartFilter() {
    return hasCartFilter;
  }
}
