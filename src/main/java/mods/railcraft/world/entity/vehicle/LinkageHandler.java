package mods.railcraft.world.entity.vehicle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import mods.railcraft.api.carts.ILinkableCart;
import mods.railcraft.api.carts.LinkageManager;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.util.Vec2D;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTools;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public final class LinkageHandler {

  private static final Logger logger = LogManager.getLogger();

  public static final String LINK_A_TIMER = "linkA_timer";
  public static final String LINK_B_TIMER = "linkB_timer";
  public static final double LINK_DRAG = 0.95;
  public static final float MAX_DISTANCE = 8F;
  private static final float STIFFNESS = 0.7F;
  private static final float HS_STIFFNESS = 0.7F;
  // private static final float TRANSFER = 0.15f;
  private static final float DAMPING = 0.4F;
  private static final float HS_DAMPING = 0.3F;
  private static final float FORCE_LIMITER = 6F;
  // private static final int TICK_HISTORY = 200;
  // private static Map<AbstractMinecartEntity, CircularVec3Queue> history = new
  // MapMaker().weakKeys().makeMap();

  /**
   * Returns the optimal distance between two linked carts that the LinkageHandler will attempt to
   * maintain at all times.
   *
   * @param cart1 AbstractMinecartEntity
   * @param cart2 AbstractMinecartEntity
   * @return The optimal distance
   */
  private float getOptimalDistance(AbstractMinecart cart1, AbstractMinecart cart2) {
    float dist = 0;
    if (cart1 instanceof ILinkableCart)
      dist += ((ILinkableCart) cart1).getOptimalDistance(cart2);
    else
      dist += LinkageManager.OPTIMAL_DISTANCE;
    if (cart2 instanceof ILinkableCart)
      dist += ((ILinkableCart) cart2).getOptimalDistance(cart1);
    else
      dist += LinkageManager.OPTIMAL_DISTANCE;
    return dist;
  }

  private boolean canCartBeAdjustedBy(AbstractMinecart cart1, AbstractMinecart cart2) {
    if (cart1 == cart2)
      return false;
    if (cart1 instanceof ILinkableCart && !((ILinkableCart) cart1).canBeAdjusted(cart2))
      return false;
    return !TrackUtil.isCartLocked(cart1);
  }

  /**
   * This is where the physics magic actually gets performed. It uses Spring Forces and Damping
   * Forces to maintain a fixed distance between carts.
   *
   * @param cart1 AbstractMinecartEntity
   * @param cart2 AbstractMinecartEntity
   */
  protected void adjustVelocity(AbstractMinecart cart1, AbstractMinecart cart2,
      LinkageManagerImpl.LinkType linkType) {
    String timer = LINK_A_TIMER;
    if (linkType == LinkageManagerImpl.LinkType.LINK_B)
      timer = LINK_B_TIMER;
    if (!cart1.level.dimension().equals(cart2.level.dimension())) {
      short count = cart1.getPersistentData().getShort(timer);
      count++;
      if (count > 200) {
        logger.debug("Carts in different dimensions, breaking link.");
        LinkageManagerImpl.INSTANCE.breakLink(cart1, cart2);
      }
      cart1.getPersistentData().putShort(timer, count);
      return;
    }
    cart1.getPersistentData().putShort(timer, (short) 0);

    double dist = cart1.distanceTo(cart2);
    if (dist > MAX_DISTANCE) {
      logger.debug("Max distance exceeded, breaking link.");
      LinkageManagerImpl.INSTANCE.breakLink(cart1, cart2);
      return;
    }

    boolean adj1 = canCartBeAdjustedBy(cart1, cart2);
    boolean adj2 = canCartBeAdjustedBy(cart2, cart1);

    Vec2D cart1Pos = new Vec2D(cart1);
    Vec2D cart2Pos = new Vec2D(cart2);

    Vec2D unit = Vec2D.unit(cart2Pos, cart1Pos);

    // Energy transfer

    // double transX = TRANSFER * (cart2.motionX - cart1.motionX);
    // double transZ = TRANSFER * (cart2.motionZ - cart1.motionZ);
    //
    // transX = limitForce(transX);
    // transZ = limitForce(transZ);
    //
    // if(adj1) {
    // cart1.motionX += transX;
    // cart1.motionZ += transZ;
    // }
    //
    // if(adj2) {
    // cart2.motionX -= transX;
    // cart2.motionZ -= transZ;
    // }

    // Spring force

    float optDist = getOptimalDistance(cart1, cart2);
    double stretch = dist - optDist;
    // stretch = Math.max(0.0, stretch);
    // if(Math.abs(stretch) > 0.5) {
    // stretch *= 2;
    // }

    boolean highSpeed = HighSpeedTools.isTravellingHighSpeed(cart1);

    double stiffness = highSpeed ? HS_STIFFNESS : STIFFNESS;
    double springX = stiffness * stretch * unit.getX();
    double springZ = stiffness * stretch * unit.getY();

    springX = limitForce(springX);
    springZ = limitForce(springZ);

    if (adj1) {
      cart1.setDeltaMovement(cart1.getDeltaMovement().add(springX, 0.0D, springZ));
    }

    if (adj2) {
      cart2.setDeltaMovement(cart2.getDeltaMovement().subtract(springX, 0.0D, springZ));
    }

    // Damping

    Vec2D cart1Vel = new Vec2D(cart1.getDeltaMovement().x(), cart1.getDeltaMovement().z());
    Vec2D cart2Vel = new Vec2D(cart2.getDeltaMovement().x(), cart2.getDeltaMovement().z());

    double dot = Vec2D.subtract(cart2Vel, cart1Vel).dotProduct(unit);

    double damping = highSpeed ? HS_DAMPING : DAMPING;
    double dampX = damping * dot * unit.getX();
    double dampZ = damping * dot * unit.getY();

    dampX = limitForce(dampX);
    dampZ = limitForce(dampZ);

    if (adj1) {
      cart1.setDeltaMovement(cart1.getDeltaMovement().add(dampX, 0.0D, dampZ));
    }

    if (adj2) {
      cart2.setDeltaMovement(cart2.getDeltaMovement().subtract(dampX, 0.0D, dampZ));

    }
  }

  private double limitForce(double force) {
    return Math.copySign(Math.min(Math.abs(force), FORCE_LIMITER), force);
  }

  /**
   * This function inspects the links and determines if any physics adjustments need to be made.
   *
   * @param cart AbstractMinecartEntity
   */
  private void adjustCart(AbstractMinecart cart) {
    if (isLaunched(cart))
      return;

    if (isOnElevator(cart))
      return;

    boolean linkedA = adjustLinkedCart(cart, LinkageManagerImpl.LinkType.LINK_A);
    boolean linkedB = adjustLinkedCart(cart, LinkageManagerImpl.LinkType.LINK_B);
    boolean linked = linkedA || linkedB;

    // Centroid
    // List<BlockPos> points =
    // Train.streamCarts(cart).map(Entity::getPosition).collect(Collectors.toList());
    // Vec2D centroid = new Vec2D(MathTools.centroid(points));
    //
    // Vec2D cartPos = new Vec2D(cart);
    // Vec2D unit = Vec2D.unit(cartPos, centroid);
    //
    // double amount = 0.2;
    // double pushX = amount * unit.getX();
    // double pushZ = amount * unit.getY();
    //
    // pushX = limitForce(pushX);
    // pushZ = limitForce(pushZ);
    //
    // cart.motionX += pushX;
    // cart.motionZ += pushZ;

    // Drag
    if (linked && !HighSpeedTools.isTravellingHighSpeed(cart)) {
      cart.setDeltaMovement(cart.getDeltaMovement().multiply(LINK_DRAG, 1.0D, LINK_DRAG));
    }

    // Speed & End Drag
    Train.get(cart).ifPresent(train -> {
      if (train.isTrainEnd(cart)) {
        train.refreshMaxSpeed();
        // if (linked && !(cart instanceof EntityLocomotive)) {
        // double drag = 0.97;
        // cart.motionX *= drag;
        // cart.motionZ *= drag;
        // }
      }
    });

  }

  private boolean adjustLinkedCart(AbstractMinecart cart, LinkageManagerImpl.LinkType linkType) {
    boolean linked = false;
    LinkageManagerImpl lm = LinkageManagerImpl.INSTANCE;
    AbstractMinecart link = lm.getLinkedCart(cart, linkType);
    if (link != null) {
      // sanity check to ensure links are consistent
      if (!lm.areLinked(cart, link)) {
        // TODO something should happen here
        // boolean success = lm.repairLink(cart, link);
      }
      if (!isLaunched(link) && !isOnElevator(link)) {
        linked = true;
        adjustVelocity(cart, link, linkType);
        // adjustCartFromHistory(cart, link);
      }
    }
    return linked;
  }

  // /**
  // * Determines whether a cart is leading another.
  // *
  // * @param leader AbstractMinecartEntity
  // * @param follower AbstractMinecartEntity
  // * @return true if leader is leading follower
  // */
  // private boolean isCartLeading(AbstractMinecartEntity leader, AbstractMinecartEntity follower) {
  // return true; // magic goes here
  // }

  // /**
  // * Adjust the current cart's position based on the linked cart its following
  // * so that it follows the same path at a set distance.
  // *
  // * @param current AbstractMinecartEntity
  // * @param linked AbstractMinecartEntity
  // */
  // private void adjustCartFromHistory(AbstractMinecartEntity current, AbstractMinecartEntity
  // linked) {
  // // If we are leading, we don't want to adjust anything
  // if (isCartLeading(current, linked))
  // return;
  //
  // CircularVec3Queue leaderHistory = history.get(linked);
  //
  // // Optimal distance is how far apart the carts should be
  // double optimalDist = getOptimalDistance(current, linked);
  // optimalDist *= optimalDist;
  //
  // double currentDistance = linked.getDistanceSq(current);
  //
  // // Search the history for the point closest to the optimal distance.
  // // There may be some issues with it choosing the wrong side of the cart.
  // // Probably needs some kind of logic to compare the distance from the
  // // new position to the current position and determine if its a valid position.
  // Vec3 closestPoint = null;
  // Vec3 linkedVec = new Vec3(linked.posX, linked.posY, linked.posZ);
  // double distance = Math.abs(optimalDist - currentDistance);
  // for (Vec3 pos : leaderHistory) {
  // double historyDistance = linkedVec.squareDistanceTo(pos);
  // double diff = Math.abs(optimalDist - historyDistance);
  // if (diff < distance) {
  // closestPoint = pos;
  // distance = diff;
  // }
  // }
  //
  // // If we found a point closer to our desired distance, move us there
  // if (closestPoint != null)
  // current.setPosition(closestPoint.x, closestPoint.y, closestPoint.z);
  // }

  // /**
  // * Saved the position history of the cart every tick in a Circular Buffer.
  // *
  // * @param cart AbstractMinecartEntity
  // */
  // private void savePosition(AbstractMinecartEntity cart) {
  // CircularVec3Queue myHistory = history.get(cart);
  // if (myHistory == null) {
  // myHistory = new CircularVec3Queue(TICK_HISTORY);
  // history.put(cart, myHistory);
  // }
  // myHistory.add(cart.posX, cart.posY, cart.posZ);
  // }

  /**
   * This is our entry point, its triggered once per tick per cart.
   *
   * @param event MinecartUpdateEvent
   */
  public void handleTick(AbstractMinecart cart) {
    // Physics done here
    adjustCart(cart);

    // savePosition(cart);
  }

  public boolean isLaunched(AbstractMinecart cart) {
    int launched = cart.getPersistentData().getInt(CartConstants.TAG_LAUNCHED);
    return launched > 0;
  }

  public boolean isOnElevator(AbstractMinecart cart) {
    int elevator = cart.getPersistentData().getByte(CartConstants.TAG_ELEVATOR);
    return elevator > 0;
  }

  // @SubscribeEvent
  // public void canMinecartTick(EntityEvent.CanUpdate event) {
  // if (event.getEntity() instanceof AbstractMinecartEntity) {
  // AbstractMinecartEntity cart = (AbstractMinecartEntity) event.getEntity();
  // if (Train.streamCarts(cart).flatMap(Streams.toType(EntityCartWorldspike.class))
  // .anyMatch(EntityCartWorldspike::hasActiveTicket)) {
  // event.setCanUpdate(true);
  // }
  // }
  // }
}
