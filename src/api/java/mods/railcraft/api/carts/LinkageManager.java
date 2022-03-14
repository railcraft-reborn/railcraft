/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.carts;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * The LinkageManager contains all the functions needed to link and interact with linked carts.
 * <p/>
 * To obtain an instance of this interface, call {@link CartUtil#linkageManager()}.
 * <p/>
 * Each cart can up to two links. They are called Link A and Link B. Some carts will have only Link
 * A, for example the Tunnel Bore.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 * @see CartUtil , ILinkableCart
 */
public interface LinkageManager {

  /**
   * The default max distance at which carts can be linked, divided by 2.
   */
  float LINKAGE_DISTANCE = 1.25F;
  /**
   * The default distance at which linked carts are maintained, divided by 2.
   */
  float OPTIMAL_DISTANCE = 0.78F;

  /**
   * Allows or disallows the cart to automatically link to the next cart it collides with.
   *
   * @param cart The minecart
   * @param autoLink Whether the auto link feature is enabled
   * @return True if tries to disable link or enable link while there is any free link
   */
  default boolean setAutoLink(AbstractMinecart cart, boolean autoLink) {
    return false;
  }

  default boolean hasAutoLink(AbstractMinecart cart) {
    return false;
  }

  default boolean tryAutoLink(AbstractMinecart cart1, AbstractMinecart cart2) {
    return false;
  }

  /**
   * Creates a link between two carts, but only if there is nothing preventing such a link.
   *
   * @return True if the link succeeded.
   */
  default boolean createLink(AbstractMinecart cart1, AbstractMinecart cart2) {
    return false;
  }

  default boolean hasFreeLink(AbstractMinecart cart) {
    return false;
  }

  /**
   * Returns the cart linked to Link A or null if nothing is currently occupying Link A.
   *
   * @param cart The cart for which to get the link
   * @return The linked cart or null
   */
  default @Nullable AbstractMinecart getLinkedCartA(AbstractMinecart cart) {
    return null;
  }

  /**
   * Returns the cart linked to Link B or null if nothing is currently occupying Link B.
   *
   * @param cart The cart for which to get the link
   * @return The linked cart or null
   */
  default @Nullable AbstractMinecart getLinkedCartB(AbstractMinecart cart) {
    return null;
  }

  /**
   * Returns true if the two carts are linked to each other.
   *
   * @return True if linked
   */
  default boolean areLinked(AbstractMinecart cart1, AbstractMinecart cart2) {
    return false;
  }

  /**
   * Breaks a link between two carts, if any link exists.
   */
  default void breakLink(AbstractMinecart cart1, AbstractMinecart cart2) {}

  /**
   * Breaks all links the cart has.
   */
  default void breakLinks(AbstractMinecart cart) {}

  /**
   * Counts how many carts are in the train.
   *
   * @param cart Any cart in the train
   * @return The number of carts in the train
   */
  default int countCartsInTrain(AbstractMinecart cart) {
    return 0;
  }

  /**
   * Returns a Stream which will iterate over every cart in the provided cart's train.
   *
   * There is no guarantee of order.
   *
   * If called on the client, it will only contain the passed cart object. There is no linkage
   * information on the client.
   */
  default Stream<AbstractMinecart> streamTrain(AbstractMinecart cart) {
    return Stream.empty();
  }

  static boolean hasLink(AbstractMinecart minecart, Link link) {
    return !(minecart instanceof LinkageHandler handler)
        || handler.isLinkable() && (link != Link.BACK || handler.hasTwoLinks());
  }
}
