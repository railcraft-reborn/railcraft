package mods.railcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.attachment.RailcraftAttachmentTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.phys.Vec3;

@Mixin(value = AbstractMinecart.class)
public class AbstractMinecartMixin {

  private AbstractMinecart self() {
    return (AbstractMinecart) (Object) this;
  }

  @Inject(method = "tick", at = @At("HEAD"))
  public void railcraft$tick(CallbackInfo callbackInfo) {
    RollingStock.getOrThrow(self()).tick();
  }

  @Redirect(method = "comeOffTrack",
      at = @At(value = "INVOKE",
          target = "Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;getMaxSpeed(Lnet/minecraft/server/level/ServerLevel;)D"))
  private double railcraft$bypassgetMaxSpeed(AbstractMinecart minecart, ServerLevel level) {
    if (minecart.onGround()) {
      return minecart.getBehavior().getMaxSpeed(level);
    }

    return minecart.getData(RailcraftAttachmentTypes.MAX_SPEED_AIR_LATERAL)
        .map(Double::valueOf)
        .orElse(minecart.getBehavior().getMaxSpeed(level));
  }

  @Inject(method = "comeOffTrack",
      at = @At(value = "INVOKE",
          target = "Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"))
  private void railcraft$beforeMove(ServerLevel level, CallbackInfo ci) {
    float maxSpeedAirVertical = self().getData(RailcraftAttachmentTypes.MAX_SPEED_AIR_VERTICAL);

    if (maxSpeedAirVertical > 0 && self().getDeltaMovement().y > maxSpeedAirVertical) {
      if(Math.abs(self().getDeltaMovement().x) < 0.3f && Math.abs(self().getDeltaMovement().z) < 0.3f) {
        self().setDeltaMovement(new Vec3(self().getDeltaMovement().x, 0.15f, self().getDeltaMovement().z));
      } else {
        self().setDeltaMovement(new Vec3(self().getDeltaMovement().x, maxSpeedAirVertical, self().getDeltaMovement().z));
      }
    }
  }

  @Redirect(method = "comeOffTrack",
      at = @At(value = "INVOKE",
          target = "Lnet/minecraft/world/phys/Vec3;scale(D)Lnet/minecraft/world/phys/Vec3;",
          ordinal = 1))
  private Vec3 railcraft$fixScale(Vec3 instance, double factor) {
    return instance.scale(self().getData(RailcraftAttachmentTypes.AIR_DRAG));
  }
}
