package mods.railcraft.battery;

import com.google.common.collect.Streams;

import java.util.Random;

import mods.railcraft.api.charge.CapabilityCharge;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.IBatteryCart;
import mods.railcraft.world.entity.cart.Train;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;

/**
 * This interface provides a simple means of using or producing Electricity within a train.
 *
 * @author CovertJaguar (https://www.railcraft.info/)
 */
public class CartBattery extends SimpleBattery implements IBatteryCart {

  static final int DRAW_INTERVAL = 8;
  protected static final Random rand = new Random();

  protected final IBatteryCart.Type type;
  protected final int lossPerTick;
  protected int draw;
  protected int clock = rand.nextInt();
  protected int drewFromTrack;

  public CartBattery() {
    this(IBatteryCart.Type.USER, 5000, 0);
  }

  public CartBattery(Type type, int capacity) {
    this(type, capacity, 0, 0);
  }

  public CartBattery(Type type, int capacity, int lossPerTick) {
    this(type, capacity, lossPerTick, 0);
  }

  public CartBattery(Type type, int capacity, int lossPerTick, int initialCharge) {
    super(capacity, initialCharge);
    this.type = type;
    this.lossPerTick = lossPerTick;
  }

  @Override
  public void setCharge(int charge) {
    // if (type == IBatteryCart.Type.USER) {
    //   return;
    // }
    super.setCharge(charge);
  }

  @Override
  public int addCharge(int charge, boolean simulate) {
    if (type == IBatteryCart.Type.USER) {
      return 0;
    }
    return super.addCharge(charge, simulate);
  }

  @Override
  public int getLosses() {
    return lossPerTick;
  }

  @Override
  public int getDraw() {
    return draw;
  }

  @Override
  public Type getType() {
    return type;
  }

  protected void removeLosses() {
    if (lossPerTick > 0) {
      if (charge >= lossPerTick) {
        charge -= lossPerTick;
      } else {
        charge = 0;
      }
    }
  }

  /*
   * ********************************************************************
   * The following functions must be called from your EntityMinecart subclass
   * ********************************************************************
   */

  /**
   * Update the battery and tries to draw charge from other carts. <b>Server side only.</b>
   *
   * <p>Must be called once per tick while on tracks by the owning object.
   * @param owner The minecart
   */
  @Override
  public void tick(AbstractMinecartEntity owner) {
    clock++;
    removeLosses();

    draw = (draw * 24 + chargeDrawnThisTick) / 25;
    chargeDrawnThisTick = 0;

    if (drewFromTrack > 0) {
      drewFromTrack--;
    } else if (type == IBatteryCart.Type.USER
        && charge < (int)((float)capacity * 0.5F)
        && (clock % DRAW_INTERVAL) == 0) {
      Train.streamCarts(owner)
          .flatMap(c -> Streams.stream(c.getCapability(CapabilityCharge.CART_BATTERY).resolve()))
          .filter(c -> ((CartBattery) c).getType() != IBatteryCart.Type.USER
              && c.getCharge() > 0).findAny()
          .ifPresent(c -> charge += c.removeCharge(capacity - charge, false));
    }
  }

  /**
   * Update the battery and tries to draw charge from the track. <b>Server side only.</b>
   *
   * <p>If you want to be able to draw power from the track, this function needs to
   * be called once per tick. Generally this means overriding
   * the EntityMinecart.moveAlongTrack() function. You don't have to call this
   * function if you don't care about drawing from tracks.
   *
   * <p/>
   * <blockquote>
   * <pre>
   * {@code
   * protected void moveAlongTrack(BlockPos pos, IBlockState state)
   *  {
   *     super.moveAlongTrack(pos, state);
   *     cartBattery.tickOnTrack(this, pos);
   *  }
   * }
   * </pre>
   * </blockquote>
   * @param owner The minecart
   * @param pos The position of the cart, to check for electrified tracks
   */
  @Override
  public void tickOnTrack(AbstractMinecartEntity owner, BlockPos pos) {
    if (owner.level.isClientSide()) { // this should not happen. If it did that means bad code.
      return;
    }
    if (type == Type.USER && needsCharging()) {
      int drawnFromTrack = Charge.distribution.network(owner.level)
          .access(pos)
          .removeCharge(capacity - charge, false);
      if (drawnFromTrack > 0.0) {
        drewFromTrack = DRAW_INTERVAL * 4;
      }
      charge += drawnFromTrack;
    }
  }
}
