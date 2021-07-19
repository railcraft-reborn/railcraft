/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.events;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraftforge.eventbus.api.Event;

/**
 * These events are fired on {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}.
 *
 * <p>
 * In order to cancel linking, call
 * {@link mods.railcraft.api.carts.ILinkageManager#breakLink(AbstractMinecartEntity, AbstractMinecartEntity)}
 * </p>
 */
public class CartLinkEvent extends Event {

  private final AbstractMinecartEntity one;
  private final AbstractMinecartEntity two;

  CartLinkEvent(AbstractMinecartEntity one, AbstractMinecartEntity two) {
    this.one = one;
    this.two = two;
  }

  public AbstractMinecartEntity getCartOne() {
    return one;
  }

  public AbstractMinecartEntity getCartTwo() {
    return two;
  }

  public static final class Link extends CartLinkEvent {

    public Link(AbstractMinecartEntity one, AbstractMinecartEntity two) {
      super(one, two);
    }
  }

  public static final class Unlink extends CartLinkEvent {

    public Unlink(AbstractMinecartEntity one, AbstractMinecartEntity two) {
      super(one, two);
    }
  }
}
