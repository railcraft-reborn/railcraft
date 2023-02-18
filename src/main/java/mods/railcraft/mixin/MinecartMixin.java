package mods.railcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.world.entity.vehicle.Minecart;

@Mixin(Minecart.class)
public class MinecartMixin {

  @Redirect(method = "activateMinecart",
      at = @At(value = "INVOKE",
          target = "Lnet/minecraft/world/entity/vehicle/Minecart;ejectPassengers()V"))
  private void railcraft$bypassEjectPassengers(Minecart minecart) {}
}
