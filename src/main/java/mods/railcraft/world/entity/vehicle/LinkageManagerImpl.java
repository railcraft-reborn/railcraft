package mods.railcraft.world.entity.vehicle;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import mods.railcraft.api.carts.Link;
import mods.railcraft.api.carts.LinkageHandler;
import mods.railcraft.api.carts.LinkageManager;
import mods.railcraft.api.event.CartLinkEvent;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.common.MinecraftForge;

/**
 * The LinkageManager contains all the functions needed to link and interacted with linked carts.
 * <p/>
 * One concept if import is that of the Linkage Id. Every cart is given a unique identifier by the
 * LinkageManager the first time it encounters the cart.
 * <p/>
 * This identifier is stored in the entity's NBT data between world loads so that links are
 * persistent rather than transitory.
 * <p/>
 * Links are also stored in NBT data as an Integer value that contains the Linkage Id of the cart it
 * is linked to.
 * <p/>
 * Generally you can ignore most of this and use the functions that don't require or return Linkage
 * Ids.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public enum LinkageManagerImpl implements LinkageManager {

  INSTANCE;

  private static final Logger logger = LogUtils.getLogger();

  /**
   * Returns the linkage id of the cart and adds the cart the linkage cache.
   *
   * @param cart The AbstractMinecartEntity
   * @return The linkage id
   */
  public UUID getLinkageId(AbstractMinecart cart) {
    return cart.getUUID();
  }

  /**
   * Returns the square of the max distance two carts can be and still be linkable.
   *
   * @param cart1 First Cart
   * @param cart2 Second Cart
   * @return The square of the linkage distance
   */
  private float getLinkageDistanceSq(AbstractMinecart cart1, AbstractMinecart cart2) {
    float dist = 0;
    if (cart1 instanceof LinkageHandler) {
      dist += ((LinkageHandler) cart1).getLinkageDistance(cart2);
    } else {
      dist += LINKAGE_DISTANCE;
    }
    if (cart2 instanceof LinkageHandler) {
      dist += ((LinkageHandler) cart2).getLinkageDistance(cart1);
    } else {
      dist += LINKAGE_DISTANCE;
    }
    return dist * dist;
  }

  @Override
  public boolean setAutoLink(AbstractMinecart minecart, boolean autoLink) {
    var extension = MinecartExtension.getOrThrow(minecart);

    if (!autoLink) {
      extension.setAutoLinkEnabled(autoLink);
      return true;
    }

    var result = false;
    for (var link : Link.values()) {
      if (extension.hasFreeLink(link)) {
        extension.setAutoLinkEnabled(link, true);
        result = true;
        logger.debug("Cart {}({}) Set To Auto Link on Link {} With First Collision.",
            this.getLinkageId(minecart), minecart.getDisplayName().getString(), link);
      }
    }
    return result;
  }

  @Override
  public boolean hasAutoLink(AbstractMinecart minecart) {
    var extension = MinecartExtension.getOrThrow(minecart);

    if (!this.hasFreeLink(minecart)) {
      extension.setAutoLinkEnabled(false);
    }

    for (var link : Link.values()) {
      if (extension.isAutoLinkEnabled(link)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean tryAutoLink(AbstractMinecart cart1, AbstractMinecart cart2) {
    if ((this.hasAutoLink(cart1) || this.hasAutoLink(cart2)) && this.createLink(cart1, cart2)) {
      logger.debug("Automatically Linked Carts {}({}) and {}({}).", getLinkageId(cart1),
          cart1.getDisplayName().getString(), this.getLinkageId(cart2),
          cart2.getDisplayName().getString());
      return true;
    }
    return false;
  }

  /**
   * Returns true if there is nothing preventing the two carts from being linked.
   *
   * @param cart1 First Cart
   * @param cart2 Second Cart
   * @return True if can be linked
   */
  private boolean canLinkCarts(AbstractMinecart cart1, AbstractMinecart cart2) {
    if (cart1 == cart2) {
      return false;
    }

    if (!this.hasFreeLink(cart1) || !this.hasFreeLink(cart2)) {
      return false;
    }

    if (cart1 instanceof LinkageHandler handler) {
      if (!handler.canLink(cart2)) {
        return false;
      }
    }

    if (cart2 instanceof LinkageHandler handler) {
      if (!handler.canLink(cart1)) {
        return false;
      }
    }

    if (this.areLinked(cart1, cart2)) {
      return false;
    }

    if (cart1.distanceToSqr(cart2) > this.getLinkageDistanceSq(cart1, cart2)) {
      return false;
    }

    return !Train.areInSameTrain(cart1, cart2);
  }

  /**
   * Creates a link between two carts, but only if there is nothing preventing such a link.
   *
   * @param cart1 First Cart
   * @param cart2 Second Cart
   * @return True if the link succeeded.
   */
  @Override
  public boolean createLink(AbstractMinecart cart1, AbstractMinecart cart2) {
    if (!this.canLinkCarts(cart1, cart2)) {
      return false;
    }

    MinecartExtension.getOrThrow(cart1).link(cart2);
    MinecartExtension.getOrThrow(cart2).link(cart1);

    if (cart1 instanceof LinkageHandler handler) {
      handler.onLinkCreated(cart2);
    }

    if (cart2 instanceof LinkageHandler handler) {
      handler.onLinkCreated(cart1);
    }

    MinecraftForge.EVENT_BUS.post(new CartLinkEvent.Link(cart1, cart2));

    return true;
  }

  @Override
  public boolean hasFreeLink(AbstractMinecart minecart) {
    return MinecartExtension.getOrThrow(minecart).hasFreeLink();
  }

  /**
   * Returns the cart linked to LinkType A or null if nothing is currently occupying LinkType A.
   *
   * @param cart The cart for which to get the link
   * @return The linked cart or null
   */
  @Override
  @Nullable
  public AbstractMinecart getLinkedCartA(AbstractMinecart cart) {
    return MinecartExtension.getOrThrow(cart).getLinkedMinecart(Link.FRONT).orElse(null);
  }

  /**
   * Returns the cart linked to LinkType B or null if nothing is currently occupying LinkType B.
   *
   * @param cart The cart for which to get the link
   * @return The linked cart or null
   */
  @Override
  @Nullable
  public AbstractMinecart getLinkedCartB(AbstractMinecart cart) {
    return MinecartExtension.getOrThrow(cart).getLinkedMinecart(Link.BACK).orElse(null);
  }

  /**
   * Returns true if the two carts are linked directly to each other.
   *
   * @param cart1 First Cart
   * @param cart2 Second Cart
   * @return True if linked
   */
  @Override
  public boolean areLinked(AbstractMinecart cart1, AbstractMinecart cart2) {
    return this.areLinked(cart1, cart2, true);
  }

  /**
   * Returns true if the two carts are linked directly to each other.
   *
   * @param cart1 First Cart
   * @param cart2 Second Cart
   * @param strict true if both carts should have linking data pointing to the other cart, false if
   *        its ok if only one cart has the data (this is technically an invalid state, but its been
   *        known to happen)
   * @return True if linked
   */
  public boolean areLinked(AbstractMinecart cart1, AbstractMinecart cart2,
      boolean strict) {
    if (cart1 == cart2) {
      return false;
    }

    var cart1Linked = MinecartExtension.getOrThrow(cart1).isLinked(cart2);
    var cart2Linked = MinecartExtension.getOrThrow(cart2).isLinked(cart1);

    if (cart1Linked != cart2Linked) {
      logger.warn(
          "Linking discrepancy between carts {}({}) and {}({}): The first cart reports {} for linked while the second one reports {}!",
          this.getLinkageId(cart1), cart1.getDisplayName().getString(), this.getLinkageId(cart2),
          cart2.getDisplayName().getString(),
          cart1Linked, cart2Linked);
    }

    if (strict) {
      return cart1Linked && cart2Linked;
    } else {
      return cart1Linked || cart2Linked;
    }
  }

  /**
   * Repairs an asymmetrical link between carts
   *
   * @param cart1 First Cart
   * @param cart2 Second Cart
   * @return true if the repair was successful.
   */
  public boolean repairLink(AbstractMinecart cart1, AbstractMinecart cart2) {
    var repaired =
        this.repairLinkUnidirectional(cart1, cart2) && this.repairLinkUnidirectional(cart2, cart1);
    if (repaired) {
      Train.repairTrain(cart1, cart2);
    } else {
      this.breakLink(cart1, cart2);
    }
    return repaired;
  }

  private boolean repairLinkUnidirectional(AbstractMinecart from, AbstractMinecart to) {
    var fromExtension = MinecartExtension.getOrThrow(from);
    return fromExtension.isLinked(to) || fromExtension.link(to);
  }

  @Override
  public void breakLink(AbstractMinecart one, AbstractMinecart two) {
    var linkOne = MinecartExtension.getOrThrow(one).getLink(two).orElse(null);
    var linkTwo = MinecartExtension.getOrThrow(two).getLink(one).orElse(null);
    this.breakLinkInternal(one, two, linkOne, linkTwo);
  }

  @Override
  public void breakLinks(AbstractMinecart cart) {
    this.breakLinkA(cart);
    this.breakLinkB(cart);
  }

  /**
   * Break only link A.
   *
   * @param cart Cart
   */
  private void breakLinkA(AbstractMinecart cart) {
    var other = this.getLinkedCartA(cart);
    if (other == null) {
      return;
    }

    var otherLink = MinecartExtension.getOrThrow(other).getLink(cart).orElse(null);
    this.breakLinkInternal(cart, other, Link.FRONT, otherLink);
  }

  /**
   * Break only link B.
   *
   * @param cart Cart
   */
  private void breakLinkB(AbstractMinecart cart) {
    var other = this.getLinkedCartB(cart);
    if (other == null) {
      return;
    }

    var otherLink = MinecartExtension.getOrThrow(other).getLink(cart).orElse(null);
    this.breakLinkInternal(cart, other, Link.BACK, otherLink);
  }

  /**
   * Breaks a bidirectional link with all the arguments given.
   *
   * This has the most argument and tries to prevent a recursion.
   *
   * @param one One of the carts given
   * @param two The cart, given or calculated via a link
   * @param linkOne The link from one, given or calculated
   * @param linkTwo The link from two, calculated
   */
  private void breakLinkInternal(AbstractMinecart one, AbstractMinecart two,
      @Nullable Link linkOne,
      @Nullable Link linkTwo) {
    if ((linkOne == null) != (linkTwo == null)) {
      logger.warn(
          "Linking discrepancy between carts {}({}) and {}({}): The first cart reports {} for linked while the second one reports {}!",
          getLinkageId(one), one.getDisplayName().getString(), getLinkageId(two),
          two.getDisplayName().getString(),
          linkOne == null,
          linkTwo == null);
    }

    if (linkOne != null) {
      this.breakLinkUnidirectional(one, two, linkOne);
    }
    if (linkTwo != null) {
      this.breakLinkUnidirectional(two, one, linkTwo);
    }

    MinecraftForge.EVENT_BUS.post(new CartLinkEvent.Unlink(one, two));
  }

  private void breakLinkUnidirectional(AbstractMinecart cart, AbstractMinecart other, Link link) {
    MinecartExtension.getOrThrow(cart).removeLink(link);

    if (cart instanceof LinkageHandler linkable) {
      linkable.onLinkBroken(other);
    }

    logger.debug("Cart {}({}) unidirectionally unlinked {}({}) at ({}).", getLinkageId(cart),
        cart.getDisplayName().getString(),
        this.getLinkageId(other), other, link.name());
  }

  /**
   * Counts how many carts are in the train.
   *
   * @param cart Any cart in the train
   * @return The number of carts in the train
   */
  @Override
  public int countCartsInTrain(AbstractMinecart cart) {
    return Train.get(cart).map(Train::size).orElse(1);
  }

  @Override
  public Stream<AbstractMinecart> streamTrain(AbstractMinecart cart) {
    return Train.streamCarts(cart);
  }

  public Iterable<AbstractMinecart> linkIterator(AbstractMinecart start, Link link) {
    if (MinecartExtension.getOrThrow(start).getLinkedMinecart(link).isEmpty()) {
      return Collections.emptyList();
    }
    return () -> new Iterator<AbstractMinecart>() {

      @Nullable
      private AbstractMinecart last;
      @Nullable
      private AbstractMinecart next;
      private AbstractMinecart current = start;

      /**
       * Calculates the next minecart. Returns null if it cannot find one.
       *
       * @return The next minecart to be returned by the iterator, or null
       */
      @Nullable
      private AbstractMinecart calculateNext() {
        if (this.last == null) {
          return MinecartExtension.getOrThrow(this.current).getLinkedMinecart(link).orElse(null);
        }

        var cartA = LinkageManagerImpl.this.getLinkedCartA(this.current);
        if (cartA != null && cartA != this.last) {
          return cartA;
        }

        var cartB = LinkageManagerImpl.this.getLinkedCartB(this.current);
        return cartB == this.last ? null : cartB;
      }

      @Override
      public boolean hasNext() {
        if (this.next == null) {
          this.next = this.calculateNext();
        }
        return this.next != null;
      }

      @Override
      public AbstractMinecart next() {
        if (this.next == null) {
          this.next = this.calculateNext();
          if (this.next == null) {
            throw new NoSuchElementException();
          }
        }

        this.last = this.current;
        this.current = this.next;
        this.next = null;
        return this.current;
      }
    };
  }
}
