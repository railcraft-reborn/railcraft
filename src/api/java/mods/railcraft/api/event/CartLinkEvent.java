/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.event;

import mods.railcraft.api.carts.RollingStock;
import net.neoforged.bus.api.Event;

/**
 * These events are fired on {@link net.neoforged.neoforge.common.NeoForge#EVENT_BUS}.
 */
public class CartLinkEvent extends Event {

  private final RollingStock one;
  private final RollingStock two;

  protected CartLinkEvent(RollingStock one, RollingStock two) {
    this.one = one;
    this.two = two;
  }

  public RollingStock getCartOne() {
    return this.one;
  }

  public RollingStock getCartTwo() {
    return this.two;
  }

  public static final class Link extends CartLinkEvent {

    public Link(RollingStock one, RollingStock two) {
      super(one, two);
    }
  }

  public static final class Unlink extends CartLinkEvent {

    public Unlink(RollingStock one, RollingStock two) {
      super(one, two);
    }
  }
}
