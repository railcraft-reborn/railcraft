package mods.railcraft.mixin;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import mods.railcraft.RailcraftConfig;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {

  @Shadow
  @Final
  private Entity entity;

  /**
   * Minecarts send their position every 3 ticks, and the client moves the cart in a straight line
   * between two positions. A fast cart goes through a curve within those ticks, so on the client
   * it cuts across instead of following the track. Sending the position every tick keeps it on
   * the curve. Nothing is sent while the cart stands still.
   */
  @ModifyExpressionValue(method = "sendChanges",
      at = @At(value = "FIELD",
          target = "Lnet/minecraft/server/level/ServerEntity;updateInterval:I",
          opcode = Opcodes.GETFIELD))
  private int railcraft$syncMinecartsEveryTick(int updateInterval) {
    return this.entity instanceof AbstractMinecart
        && RailcraftConfig.isSmoothCartMovementEnabled() ? 1 : updateInterval;
  }
}
