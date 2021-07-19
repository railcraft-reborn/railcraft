package mods.railcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import mods.railcraft.event.MinecartInteractEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraftforge.common.MinecraftForge;

@Mixin(Entity.class)
public class EntityMixin {

  @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
  public void interact(PlayerEntity player, Hand hand,
      CallbackInfoReturnable<ActionResultType> callbackInfo) {
    if ((Object) this instanceof AbstractMinecartEntity) {
      AbstractMinecartEntity cart = (AbstractMinecartEntity) (Object) this;
      if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(cart, player, hand))) {
        callbackInfo.setReturnValue(ActionResultType.CONSUME);
      }
    }
  }
}
