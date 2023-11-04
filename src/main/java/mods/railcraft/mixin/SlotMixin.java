package mods.railcraft.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import mods.railcraft.util.ValidateSlots;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

@Mixin(value = Slot.class, remap = false)
public class SlotMixin {

  @Shadow
  @Final
  private int slot;

  @Shadow
  @Final
  private Container container;

  private boolean validateSlots;

  @Inject(method = "<init>(Lnet/minecraft/world/Container;III)V", at = @At("RETURN"))
  private void railcraft$init(Container container, int slot, int x, int y,
      CallbackInfo callbackInfo) {
    this.validateSlots = container.getClass().isAnnotationPresent(ValidateSlots.class);
  }

  @Overwrite
  public boolean mayPlace(ItemStack itemStack) {
    return !this.validateSlots || this.container.canPlaceItem(this.slot, itemStack);
  }
}
