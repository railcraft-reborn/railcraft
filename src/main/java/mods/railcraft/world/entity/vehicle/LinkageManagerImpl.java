package mods.railcraft.world.entity.vehicle;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Level;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import mods.railcraft.api.carts.ILinkableCart;
import mods.railcraft.api.carts.LinkageManager;
import mods.railcraft.api.event.CartLinkEvent;
import mods.railcraft.util.MathTools;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLLoader;

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

  public static final String AUTO_LINK_A = "rcAutoLinkA";
  public static final String AUTO_LINK_B = "rcAutoLinkB";
  public static final String LINK_A_HIGH = "rcLinkAHigh";
  public static final String LINK_A_LOW = "rcLinkALow";
  public static final String LINK_B_HIGH = "rcLinkBHigh";
  public static final String LINK_B_LOW = "rcLinkBLow";

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
    if (cart1 instanceof ILinkableCart) {
      dist += ((ILinkableCart) cart1).getLinkageDistance(cart2);
    } else {
      dist += LINKAGE_DISTANCE;
    }
    if (cart2 instanceof ILinkableCart) {
      dist += ((ILinkableCart) cart2).getLinkageDistance(cart1);
    } else {
      dist += LINKAGE_DISTANCE;
    }
    return dist * dist;
  }

  private void removeAutoLinks(AbstractMinecart cart) {
    for (LinkType link : LinkType.VALUES) {
      cart.getPersistentData().remove(link.autoLink);
    }
  }

  @Override
  public boolean setAutoLink(AbstractMinecart cart, boolean autoLink) {
    if (autoLink) {
      boolean ret = false;
      for (LinkType link : LinkType.VALUES) {
        if (hasFreeLink(cart, link)) {
          cart.getPersistentData().putBoolean(link.autoLink, true);
          ret = true;
          logger.debug("Cart {}({}) Set To Auto Link on Link {} With First Collision.",
              getLinkageId(cart),
              cart.getDisplayName(), link);
        }
      }
      return ret;
    } else {
      removeAutoLinks(cart);
      return true;
    }
  }

  @Override
  public boolean hasAutoLink(AbstractMinecart cart) {
    if (!hasFreeLink(cart)) {
      removeAutoLinks(cart);
    }
    return cart.getPersistentData().getBoolean(AUTO_LINK_A)
        || cart.getPersistentData().getBoolean(AUTO_LINK_B);
  }

  @Override
  public boolean tryAutoLink(AbstractMinecart cart1, AbstractMinecart cart2) {
    if ((hasAutoLink(cart1) || hasAutoLink(cart2)) && createLink(cart1, cart2)) {
      logger.debug("Automatically Linked Carts {}({}) and {}({}).", getLinkageId(cart1),
          cart1.getDisplayName(),
          getLinkageId(cart2), cart2.getDisplayName());
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

    if (!hasFreeLink(cart1) || !hasFreeLink(cart2)) {
      return false;
    }

    if (cart1 instanceof ILinkableCart) {
      if (!((ILinkableCart) cart1).canLink(cart2)) {
        return false;
      }
    }

    if (cart2 instanceof ILinkableCart) {
      if (!((ILinkableCart) cart2).canLink(cart1)) {
        return false;
      }
    }

    if (areLinked(cart1, cart2)) {
      return false;
    }

    if (cart1.distanceToSqr(cart2) > getLinkageDistanceSq(cart1, cart2)) {
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
    if (canLinkCarts(cart1, cart2)) {
      setLinkUnidirectional(cart1, cart2);
      setLinkUnidirectional(cart2, cart1);

      if (cart1 instanceof ILinkableCart) {
        ((ILinkableCart) cart1).onLinkCreated(cart2);
      }
      if (cart2 instanceof ILinkableCart) {
        ((ILinkableCart) cart2).onLinkCreated(cart1);
      }
      MinecraftForge.EVENT_BUS.post(new CartLinkEvent.Link(cart1, cart2));
      return true;
    }
    return false;
  }

  @Override
  public boolean hasFreeLink(AbstractMinecart cart) {
    return Arrays.stream(LinkType.VALUES).anyMatch(link -> hasFreeLink(cart, link));
  }

  public boolean hasFreeLink(AbstractMinecart cart, LinkType type) {
    if (!hasLink(cart, type)) {
      return false;
    }
    return MathTools.isNil(getLink(cart, type));
  }

  public boolean hasLink(AbstractMinecart cart, LinkType linkType) {
    if (cart instanceof ILinkableCart) {
      ILinkableCart linkable = (ILinkableCart) cart;
      return linkable.isLinkable() && (linkType != LinkType.LINK_B || linkable.hasTwoLinks());
    }
    return true;
  }

  private boolean setLinkUnidirectional(AbstractMinecart from, AbstractMinecart to) {
    for (LinkType link : LinkType.VALUES) {
      if (hasFreeLink(from, link)) {
        setLinkUnidirectional(from, to, link);
        return true;
      }
    }
    return false;
  }

  // Note: returns a nil uuid (0) if the link does not exist
  public UUID getLink(AbstractMinecart cart, LinkType linkType) {
    long high = cart.getPersistentData().getLong(linkType.tagHigh);
    long low = cart.getPersistentData().getLong(linkType.tagLow);
    return new UUID(high, low);
  }

  public UUID getLinkA(AbstractMinecart cart) {
    return getLink(cart, LinkType.LINK_A);
  }

  public UUID getLinkB(AbstractMinecart cart) {
    return getLink(cart, LinkType.LINK_B);
  }

  private void setLinkUnidirectional(AbstractMinecart source, AbstractMinecart target,
      LinkType linkType) {
    // hasFreeLink(source, linkType) checked
    UUID id = getLinkageId(target);
    source.getPersistentData().putLong(linkType.tagHigh, id.getMostSignificantBits());
    source.getPersistentData().putLong(linkType.tagLow, id.getLeastSignificantBits());
    source.getPersistentData().remove(linkType.autoLink); // So we don't need to worry outside
  }

  /**
   * Returns the cart linked to LinkType A or null if nothing is currently occupying LinkType A.
   *
   * @param cart The cart for which to get the link
   * @return The linked cart or null
   */
  @Override
  public @Nullable AbstractMinecart getLinkedCartA(AbstractMinecart cart) {
    return getLinkedCart(cart, LinkType.LINK_A);
  }

  /**
   * Returns the cart linked to LinkType B or null if nothing is currently occupying LinkType B.
   *
   * @param cart The cart for which to get the link
   * @return The linked cart or null
   */
  @Override
  public @Nullable AbstractMinecart getLinkedCartB(AbstractMinecart cart) {
    return getLinkedCart(cart, LinkType.LINK_B);
  }

  public @Nullable AbstractMinecart getLinkedCart(AbstractMinecart cart,
      LinkType type) {
    return CartTools.getCartFromUUID(cart.level, getLink(cart, type));
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
    return areLinked(cart1, cart2, true);
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

    UUID id1 = getLinkageId(cart1);
    UUID id2 = getLinkageId(cart2);
    boolean cart1Linked = id2.equals(getLinkA(cart1)) || id2.equals(getLinkB(cart1));
    boolean cart2Linked = id1.equals(getLinkA(cart2)) || id1.equals(getLinkB(cart2));

    if (cart1Linked != cart2Linked) {

      logger.log(FMLLoader.isProduction() ? Level.WARN : Level.DEBUG,
          "Linking discrepancy between carts {}({}) and {}({}): The first cart reports {} for linked while the second one reports {}!",
          getLinkageId(cart1), cart1.getDisplayName(), getLinkageId(cart2), cart2.getDisplayName(),
          cart1Linked,
          cart2Linked);
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
    boolean repaired =
        repairLinkUnidirectional(cart1, cart2) && repairLinkUnidirectional(cart2, cart1);
    if (repaired) {
      Train.repairTrain(cart1, cart2);
    } else {
      breakLink(cart1, cart2);
    }
    return repaired;
  }

  private boolean repairLinkUnidirectional(AbstractMinecart from, AbstractMinecart to) {
    UUID link = getLinkageId(to);

    return link.equals(getLinkA(from)) || link.equals(getLinkB(from))
        || setLinkUnidirectional(from, to);
  }

  @Override
  public void breakLink(AbstractMinecart one, AbstractMinecart two) {
    LinkType linkOne = getLinkType(one, two);
    LinkType linkTwo = getLinkType(two, one);

    breakLinkInternal(one, two, linkOne, linkTwo);
  }

  @Override
  public void breakLinks(AbstractMinecart cart) {
    breakLinkA(cart);
    breakLinkB(cart);
  }

  /**
   * Break only link A.
   *
   * @param cart Cart
   */
  private void breakLinkA(AbstractMinecart cart) {
    AbstractMinecart other = getLinkedCartA(cart);
    if (other == null) {
      return;
    }

    LinkType otherLink = getLinkType(other, cart);
    breakLinkInternal(cart, other, LinkType.LINK_A, otherLink);
  }

  /**
   * Break only link B.
   *
   * @param cart Cart
   */
  private void breakLinkB(AbstractMinecart cart) {
    AbstractMinecart other = getLinkedCartB(cart);
    if (other == null) {
      return;
    }

    LinkType otherLink = getLinkType(other, cart);
    breakLinkInternal(cart, other, LinkType.LINK_B, otherLink);
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
      @Nullable LinkType linkOne,
      @Nullable LinkType linkTwo) {
    if ((linkOne == null) != (linkTwo == null)) {
      logger.log(FMLLoader.isProduction() ? Level.WARN : Level.DEBUG,
          "Linking discrepancy between carts {}({}) and {}({}): The first cart reports {} for linked while the second one reports {}!",
          getLinkageId(one), one.getDisplayName(), getLinkageId(two), two.getDisplayName(),
          linkOne == null,
          linkTwo == null);
    }

    if (linkOne != null) {
      breakLinkUnidirectional(one, two, linkOne);
    }
    if (linkTwo != null) {
      breakLinkUnidirectional(two, one, linkTwo);
    }

    MinecraftForge.EVENT_BUS.post(new CartLinkEvent.Unlink(one, two));
  }

  private @Nullable LinkType getLinkType(AbstractMinecart from, AbstractMinecart to) {
    UUID linkTo = getLinkageId(to);
    return Arrays.stream(LinkType.VALUES).filter(link -> linkTo.equals(getLink(from, link)))
        .findFirst().orElse(null);
  }

  private void breakLinkUnidirectional(AbstractMinecart cart, AbstractMinecart other,
      LinkType linkType) {
    removeLinkTags(cart, linkType);
    if (cart instanceof ILinkableCart) {
      ((ILinkableCart) cart).onLinkBroken(other);
    }

    logger.debug("Cart {0}({1}) unidirectionally unlinked {2}({3}) at ({4}).", getLinkageId(cart),
        cart.getDisplayName(),
        getLinkageId(other), other, linkType.name());
  }

  private void removeLinkTags(AbstractMinecart cart, LinkType linkType) {
    cart.getPersistentData().remove(linkType.tagHigh);
    cart.getPersistentData().remove(linkType.tagLow);
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

  public Iterable<AbstractMinecart> linkIterator(final AbstractMinecart start,
      final LinkType type) {
    if (MathTools.isNil(getLink(start, type))) {
      return Collections.emptyList();
    }
    return () -> new Iterator<AbstractMinecart>() {
      private final LinkageManagerImpl lm = INSTANCE;
      private @Nullable AbstractMinecart last;
      private @Nullable AbstractMinecart next;
      private AbstractMinecart current = start;

      /**
       * Calculates the next minecart. Returns null if it cannot find one.
       *
       * @return The next minecart to be returned by the iterator, or null
       */
      private @Nullable AbstractMinecart calculateNext() {
        if (last == null) {
          return lm.getLinkedCart(current, type);
        }
        AbstractMinecart cartA = lm.getLinkedCartA(current);
        if (cartA != null && cartA != last) {
          return cartA;
        }

        AbstractMinecart cartB = lm.getLinkedCartB(current);
        return cartB == last ? null : cartB;
      }

      @Override
      public boolean hasNext() {
        if (next == null) {
          next = calculateNext();
        }
        return next != null;
      }

      @Override
      public AbstractMinecart next() {
        if (next == null) {
          next = calculateNext();

          if (next == null) {
            throw new NoSuchElementException();
          }
        }

        last = current;
        current = next;
        next = null;
        return current;
      }
    };
  }

  public enum LinkType {
    LINK_A(LINK_A_HIGH, LINK_A_LOW, AUTO_LINK_A),
    LINK_B(LINK_B_HIGH, LINK_B_LOW, AUTO_LINK_B);

    public static final LinkType[] VALUES = values();
    public final String tagHigh;
    public final String tagLow;
    public final String autoLink;

    LinkType(String tagHigh, String tagLow, String autoLink) {
      this.tagHigh = tagHigh;
      this.tagLow = tagLow;
      this.autoLink = autoLink;
    }
  }
}
