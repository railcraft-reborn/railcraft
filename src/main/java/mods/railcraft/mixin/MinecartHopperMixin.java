package mods.railcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.container.manipulator.ContainerManipulator;
import net.minecraft.world.entity.vehicle.MinecartHopper;

@Mixin(value = MinecartHopper.class)
public class MinecartHopperMixin {

  private static final int PUSH_COOLDOWN_TICKS = 5;

  private int pushTime = PUSH_COOLDOWN_TICKS;

  @Inject(method = "tick", at = @At("RETURN"))
  private void railcraft$pushItems(CallbackInfo callbackInfo) {
    var self = (MinecartHopper) (Object) this;
    if (!self.level().isClientSide()
        && self.isAlive()
        && this.pushTime-- == 0) {
      tryPushItem(self);
      this.pushTime = PUSH_COOLDOWN_TICKS;
    }
  }

  private static void tryPushItem(MinecartHopper self) {
    var manipulator = ContainerManipulator.of(self);
    var rollingStock = RollingStock.getOrThrow(self);
    var full = true;
    // Push full stacks whenever possible
    for (var slot : manipulator) {
      if (slot.isFull()) {
        slot.setItem(rollingStock.pushItem(slot.item()));
      }

      if (slot.isEmpty()) {
        full = false;
      }
    }

    if (!full) {
      return;
    }

    // If all slots are occupied, try to clear one of the slots.
    for (var slot : manipulator) {
      var left = rollingStock.pushItem(slot.item());
      slot.setItem(left);
      if (left.isEmpty()) {
        return;
      }
    }
  }
}
