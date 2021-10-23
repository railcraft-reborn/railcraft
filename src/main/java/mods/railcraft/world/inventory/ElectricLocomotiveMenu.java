package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.ChargeBatteryIndicator;
import mods.railcraft.gui.widget.GaugeWidget;
import mods.railcraft.world.entity.cart.locomotive.ElectricLocomotiveEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class ElectricLocomotiveMenu extends LocomotiveMenu<ElectricLocomotiveEntity> {

  private final ChargeBatteryIndicator chargeIndicator;

  public ElectricLocomotiveMenu(int id, PlayerInventory playerInv,
      ElectricLocomotiveEntity loco) {
    super(RailcraftMenuTypes.ELECTRIC_LOCOMOTIVE.get(), id, playerInv, loco);
    this.chargeIndicator = new ChargeBatteryIndicator(loco.getBatteryCart());
    this.addWidget(new GaugeWidget(this.chargeIndicator, 57, 20, 176, 0, 62, 8, false));
  }

  public static ElectricLocomotiveMenu create(int id, PlayerInventory playerInventory,
      PacketBuffer data) {
    int entityId = data.readVarInt();
    Entity entity = playerInventory.player.level.getEntity(entityId);
    if (entity instanceof ElectricLocomotiveEntity) {
      return new ElectricLocomotiveMenu(id, playerInventory, (ElectricLocomotiveEntity) entity);
    }
    throw new IllegalStateException("Cannot find locomotive with ID: " + entityId);
  }
}
