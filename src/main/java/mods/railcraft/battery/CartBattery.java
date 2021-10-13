package mods.railcraft.battery;

import com.google.common.collect.Streams;

import java.util.Random;

import mods.railcraft.api.charge.CapabilitiesCharge;
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
  protected final float lossPerTick;
  protected float draw;
  protected int clock = rand.nextInt();
  protected int drewFromTrack;

  public CartBattery() {
    this(IBatteryCart.Type.USER, 5000.0F, 0.0F);
  }

  public CartBattery(Type type, float capacity) {
    this(type, capacity, 0.0F);
  }

  public CartBattery(Type type, float capacity, float lossPerTick) {
    super(capacity);
    this.type = type;
    this.lossPerTick = lossPerTick;
  }

  @Override
  public void setCharge(float charge) {
    if (type == IBatteryCart.Type.USER) {
      return;
    }
    super.setCharge(charge);
  }

  @Override
  public float addCharge(float charge, boolean simulate) {
    if (type == IBatteryCart.Type.USER) {
      return 0.0F;
    }
    return super.addCharge(charge, simulate);
  }

  @Override
  public float getLosses() {
    return lossPerTick;
  }

  @Override
  public float getDraw() {
    return draw;
  }

  @Override
  public Type getType() {
    return type;
  }

  protected void removeLosses() {
    if (lossPerTick > 0.0F) {
      if (charge >= lossPerTick) {
        charge -= lossPerTick;
      } else {
        charge = 0.0F;
      }
    }
  }

  /*
   * ********************************************************************
   * The following functions must be called from your EntityMinecart subclass
   * ********************************************************************
   */

  /**
   * Must be called once per tick while on tracks by the owning object. Server
   * side only.
   * <p/>
   * <blockquote>
   *
   * <pre>
   * {@code
   * public void onEntityUpdate()
   *  {
   *     super.onEntityUpdate();
   *     if (!world.isRemote)
   *        cartBattery.tick(this);
   *  }
   * }
   * </pre>
   *
   * </blockquote>
   */
  @Override
  public void tick(AbstractMinecartEntity owner) {
    clock++;
    removeLosses();

    draw = (draw * 24.0F + chargeDrawnThisTick) / 25.0F;
    chargeDrawnThisTick = 0.0F;

    if (drewFromTrack > 0) {
      drewFromTrack--;
    } else if (type == IBatteryCart.Type.USER
        && charge < (capacity * 0.5F)
        && clock % DRAW_INTERVAL == 0) {
      Train.streamCarts(owner)
          .flatMap(c -> Streams.stream(c.getCapability(CapabilitiesCharge.CART_BATTERY).resolve()))
          .filter(c -> c.getType() != IBatteryCart.Type.USER && c.getCharge() > 0).findAny()
          .ifPresent(c -> charge += c.removeCharge(capacity - charge, false));
    }
  }

  /**
   * If you want to be able to draw power from the track, this function needs to
   * be called once per tick. Server side only. Generally this means overriding
   * the EntityMinecart.moveAlongTrack() function. You don't have to call this
   * function if you don't care about drawing from tracks.
   * <p/>
   * <blockquote>
   *
   * <pre>
   * {@code
   * protected void moveAlongTrack(BlockPos pos, IBlockState state)
   *  {
   *     super.moveAlongTrack(pos, state);
   *     cartBattery.tickOnTrack(this, pos);
   *  }
   * }
   * </pre>
   *
   * </blockquote>
   */
  @Override
  public void tickOnTrack(AbstractMinecartEntity owner, BlockPos pos) {
    if (!owner.level.isClientSide() && type == Type.USER && needsCharging()) {
      double drawnFromTrack = Charge.distribution.network(owner.level)
          .access(pos)
          .removeCharge(capacity - charge, false);
      if (drawnFromTrack > 0.0) {
        drewFromTrack = DRAW_INTERVAL * 4;
      }
      charge += drawnFromTrack;
    }
  }
}
