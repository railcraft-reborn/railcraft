package mods.railcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.carts.RollingStock;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

@Mixin(value = AbstractMinecart.class)
public class AbstractMinecartMixin {

  /**
   * Vanilla only notices a cart turning around when its yaw changes by 170 degrees or more in a
   * tick. A cart turning around on a curve changes by less, so it would keep the new yaw and face
   * the other way.
   */
  @Unique
  private static final float FLIP_CUTOFF = 120;

  /**
   * The server sends a minecart's position every tick (see {@link ServerEntityMixin}).
   */
  @Unique
  private static final int LERP_STEPS = 2;

  @Shadow
  private int lerpSteps;

  @Inject(method = "tick", at = @At("HEAD"))
  public void railcraft$tick(CallbackInfo callbackInfo) {
    RollingStock.getOrThrow((AbstractMinecart) (Object) this).tick();
  }

  @Inject(method = "tick", at = @At("TAIL"))
  public void railcraft$fixFlip(CallbackInfo callbackInfo) {
    var cart = (AbstractMinecart) (Object) this;
    if (cart.level().isClientSide()) {
      return;
    }
    var distance = Mth.degreesDifference(cart.yRotO, cart.getYRot());
    if (distance < -FLIP_CUTOFF || distance >= FLIP_CUTOFF) {
      cart.setYRot((cart.getYRot() + 180) % 360);
      cart.flipped = !cart.flipped;
    }
  }

  /**
   * Vanilla moves the cart towards a new position over 5 ticks, because the position only arrives
   * every 3 ticks. The cart then lags behind and cuts across curves. Now that the position arrives
   * every tick, the cart can follow it closely.
   */
  @Inject(method = "lerpTo", at = @At("TAIL"))
  private void railcraft$followServerPosition(double x, double y, double z, float yRot,
      float xRot, int steps, CallbackInfo callbackInfo) {
    if (RailcraftConfig.isSmoothCartMovementEnabled()) {
      this.lerpSteps = Math.min(this.lerpSteps, LERP_STEPS);
    }
  }
}
