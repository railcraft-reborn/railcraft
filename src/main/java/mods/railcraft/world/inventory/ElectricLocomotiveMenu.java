package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.ChargeBatteryIndicator;
import mods.railcraft.gui.widget.GaugeWidget;
import mods.railcraft.world.entity.cart.locomotive.ElectricLocomotiveEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.FriendlyByteBuf;

public class ElectricLocomotiveMenu extends LocomotiveMenu<ElectricLocomotiveEntity> {

  private final ChargeBatteryIndicator chargeIndicator;

  public ElectricLocomotiveMenu(int id, Inventory playerInv,
      ElectricLocomotiveEntity loco) {
    super(RailcraftMenuTypes.ELECTRIC_LOCOMOTIVE.get(), id, playerInv, loco);
    this.chargeIndicator = new ChargeBatteryIndicator(loco.getBatteryCart());
    this.addWidget(new GaugeWidget(this.chargeIndicator, 57, 20, 176, 0, 62, 8, false));
  }

  public static ElectricLocomotiveMenu create(int id, Inventory playerInventory,
      FriendlyByteBuf data) {
    int entityId = data.readVarInt();
    Entity entity = playerInventory.player.level.getEntity(entityId);
    if (entity instanceof ElectricLocomotiveEntity) {
      return new ElectricLocomotiveMenu(id, playerInventory, (ElectricLocomotiveEntity) entity);
    }
    throw new IllegalStateException("Cannot find locomotive with ID: " + entityId);
  }
}
