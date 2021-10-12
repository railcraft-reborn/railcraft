package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.gui.widget.GaugeWidget;
import mods.railcraft.world.entity.cart.SteamLocomotiveEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class SteamLocomotiveMenu extends LocomotiveMenu<SteamLocomotiveEntity> {

  public static final int HEIGHT = 205;

  public SteamLocomotiveMenu(int id, PlayerInventory playerInventory,
      SteamLocomotiveEntity locomotive) {
    super(RailcraftMenuTypes.STEAM_LOCOMOTIVE.get(), id, playerInventory, locomotive, HEIGHT);

    this.addWidget(
        new FluidGaugeWidget(this.getLocomotive().getTankManager().get(0), 53, 23, 176, 0, 16, 47));
    this.addWidget(
        new FluidGaugeWidget(this.getLocomotive().getTankManager().get(1), 17, 23, 176, 0, 16, 47));

    this.addWidget(new GaugeWidget(
        this.getLocomotive().getBoiler().getTemperatureGauge(), 40, 25, 176, 61, 6, 43));

    this.addSlot(new SlotWaterLimited(this.getLocomotive(), 0, 152, 20));
    this.addSlot(new SlotOutput(this.getLocomotive(), 1, 152, 56));
    this.addSlot(new SlotOutput(this.getLocomotive(), 2, 116, 56));
    this.addSlot(new SlotRailcraft(this.getLocomotive(), 3, 116, 20)); // Burn
    this.addSlot(new SlotRailcraft(this.getLocomotive(), 4, 80, 20)); // Fuel
    this.addSlot(new SlotRailcraft(this.getLocomotive(), 5, 80, 38)); // Fuel
    this.addSlot(new SlotRailcraft(this.getLocomotive(), 6, 80, 56)); // Fuel

    this.addDataSlot(new SimpleIntReferenceHolder(
        () -> (int) Math.round(SteamLocomotiveMenu.this.getLocomotive().getBoiler().getBurnTime()),
        this.getLocomotive().getBoiler()::setBurnTime));

    this.addDataSlot(new SimpleIntReferenceHolder(
        () -> (int) Math.round(this.getLocomotive().getBoiler().getCurrentItemBurnTime()),
        this.getLocomotive().getBoiler()::setCurrentItemBurnTime));
  }

  public static SteamLocomotiveMenu create(int id, PlayerInventory playerInventory,
      PacketBuffer data) {
    int entityId = data.readVarInt();
    Entity entity = playerInventory.player.level.getEntity(entityId);
    if (entity instanceof SteamLocomotiveEntity) {
      return new SteamLocomotiveMenu(id, playerInventory, (SteamLocomotiveEntity) entity);
    }
    throw new IllegalStateException("Cannot find locomotive with ID: " + entityId);
  }
}
