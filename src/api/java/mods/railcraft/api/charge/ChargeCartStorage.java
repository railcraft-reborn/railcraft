package mods.railcraft.api.charge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.neoforged.neoforge.energy.IEnergyStorage;

public interface ChargeCartStorage extends IEnergyStorage {

  /**
   * Returns the per-tick loss of charge in the cart battery.
   */
  double getLosses();

  /**
   * Returns the approximated average charge used in the last 25 ticks.
   */
  double getDraw();

  /**
   * Update the battery and tries to draw charge from other carts.
   *
   * @param owner The cart that carries the battery
   */
  void tick(AbstractMinecart owner);

  /**
   * Update the battery and tries to draw charge from the track.
   *
   * @param owner The cart that carries the battery
   * @param pos The position of the track
   */
  void tickOnTrack(AbstractMinecart owner, BlockPos pos);
}
