package mods.railcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
  private static final float FLIP_CUTOFF = 120;

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
}
