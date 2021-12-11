package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.util.container.filters.StackFilters;
import mods.railcraft.world.entity.vehicle.TankMinecart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.network.FriendlyByteBuf;

public class TankMinecartMenu extends RailcraftMenu {

  private final FluidGaugeWidget fluidGuage;

  public TankMinecartMenu(int id, Inventory playerInventory,
      TankMinecart tankMinecart) {
    super(RailcraftMenuTypes.TANK_MINECART.get(), id, playerInventory);

    this.addWidget(this.fluidGuage =
        new FluidGaugeWidget(tankMinecart.getTankManager().get(0), 35, 23, 176, 0, 16, 47));

    this.addSlot(new FluidFilterSlot(tankMinecart.getFilterInv(), 0, 80, 21));
    this.addSlot(new ItemFilterSlot(
        StackFilters.FLUID_CONTAINER, tankMinecart.getInvLiquids(), 0, 116, 21));
    this.addSlot(new OutputSlot(tankMinecart.getInvLiquids(), 1, 116, 57));
    this.addSlot(new OutputSlot(tankMinecart.getInvLiquids(), 2, 80, 57));

    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 9; x++) {
        this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
      }
    }

    for (int x = 0; x < 9; x++) {
      this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
    }
  }

  public FluidGaugeWidget getFluidGauge() {
    return this.fluidGuage;
  }

  public static TankMinecartMenu create(int id, Inventory playerInventory,
      FriendlyByteBuf data) {
    int entityId = data.readVarInt();
    Entity entity = playerInventory.player.level.getEntity(entityId);
    if (entity instanceof TankMinecart) {
      return new TankMinecartMenu(id, playerInventory, (TankMinecart) entity);
    }
    throw new IllegalStateException("Cannot find tank minecart with ID: " + entityId);
  }
}
