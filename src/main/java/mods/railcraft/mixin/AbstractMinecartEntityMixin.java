package mods.railcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import mods.railcraft.Railcraft;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;

@Mixin(AbstractMinecartEntity.class)
public class AbstractMinecartEntityMixin {

  @Inject(method = "tick", at = @At("HEAD"))
  public void tick(CallbackInfo callbackInfo) {
    AbstractMinecartEntity cart = (AbstractMinecartEntity) (Object) this;
    if (!cart.level.isClientSide()) {
      Railcraft.getInstance().getLinkageHandler().handleTick(cart);
      Railcraft.getInstance().getMinecartHandler().handleTick(cart);
    }
  }
}
