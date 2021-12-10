/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.event;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.core.BlockPos;
import net.minecraftforge.eventbus.api.Event;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class CartLockdownEvent extends Event {

  public final AbstractMinecart cart;
  public final BlockPos pos;

  protected CartLockdownEvent(AbstractMinecart cart, BlockPos pos) {
    this.cart = cart;
    this.pos = pos;
  }

  /**
   * This event is posted every tick that a LockType Track (Lockdown, Holding, Boarding) is holding
   * onto a minecart.
   */
  public static final class Lock extends CartLockdownEvent {

    public Lock(AbstractMinecart cart, BlockPos pos) {
      super(cart, pos);
    }
  }

  /**
   * This event is posted every tick that a LockType Track (Lockdown, Holding, Boarding) is
   * releasing a minecart.
   */
  public static final class Release extends CartLockdownEvent {

    public Release(AbstractMinecart cart, BlockPos pos) {
      super(cart, pos);
    }
  }
}
