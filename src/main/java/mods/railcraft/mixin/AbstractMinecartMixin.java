package mods.railcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import mods.railcraft.api.carts.RollingStock;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

@Mixin(value = AbstractMinecart.class, remap = false)
public class AbstractMinecartMixin {

  @Inject(method = "tick", at = @At("HEAD"))
  public void tick(CallbackInfo callbackInfo) {
    RollingStock.getOrThrow((AbstractMinecart) (Object) this).tick();
  }
}
