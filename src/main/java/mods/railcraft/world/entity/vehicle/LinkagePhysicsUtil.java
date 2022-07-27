package mods.railcraft.world.entity.vehicle;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import mods.railcraft.api.carts.Link;
import mods.railcraft.api.carts.LinkageHandler;
import mods.railcraft.api.carts.LinkageManager;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.util.Vec2d;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public final class LinkagePhysicsUtil {

  private static final Logger logger = LogUtils.getLogger();

  public static final String LINK_A_TIMER = "linkA_timer";
  public static final String LINK_B_TIMER = "linkB_timer";
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

  private LinkagePhysicsUtil() {}

  /**
   * This is where the physics magic actually gets performed. It uses Spring Forces and Damping
   * Forces to maintain a fixed distance between carts.
   *
   * @param cart1 AbstractMinecartEntity
   * @param cart2 AbstractMinecartEntity
   */
  public static void adjustVelocity(AbstractMinecart cart1, AbstractMinecart cart2, Link linkType) {
    String timer = LINK_A_TIMER;
    if (linkType == Link.BACK)
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

    Vec2d cart1Pos = new Vec2d(cart1);
    Vec2d cart2Pos = new Vec2d(cart2);

    Vec2d unit = Vec2d.unit(cart2Pos, cart1Pos);

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

    boolean highSpeed = MinecartExtension.getOrThrow(cart1).isHighSpeed();

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

    Vec2d cart1Vel = new Vec2d(cart1.getDeltaMovement().x(), cart1.getDeltaMovement().z());
    Vec2d cart2Vel = new Vec2d(cart2.getDeltaMovement().x(), cart2.getDeltaMovement().z());

    double dot = Vec2d.subtract(cart2Vel, cart1Vel).dotProduct(unit);

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

  private static boolean canCartBeAdjustedBy(AbstractMinecart cart1, AbstractMinecart cart2) {
    if (cart1 == cart2)
      return false;
    if (cart1 instanceof LinkageHandler && !((LinkageHandler) cart1).canBeAdjusted(cart2))
      return false;
    return !TrackUtil.isCartLocked(cart1);
  }

  /**
   * Returns the optimal distance between two linked carts that the LinkageHandler will attempt to
   * maintain at all times.
   *
   * @param cart1 AbstractMinecartEntity
   * @param cart2 AbstractMinecartEntity
   * @return The optimal distance
   */
  private static float getOptimalDistance(AbstractMinecart cart1, AbstractMinecart cart2) {
    float dist = 0;
    if (cart1 instanceof LinkageHandler)
      dist += ((LinkageHandler) cart1).getOptimalDistance(cart2);
    else
      dist += LinkageManager.OPTIMAL_DISTANCE;
    if (cart2 instanceof LinkageHandler)
      dist += ((LinkageHandler) cart2).getOptimalDistance(cart1);
    else
      dist += LinkageManager.OPTIMAL_DISTANCE;
    return dist;
  }

  private static double limitForce(double force) {
    return Math.copySign(Math.min(Math.abs(force), FORCE_LIMITER), force);
  }
}
