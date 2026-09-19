package mods.railcraft.api.carts;

import net.minecraft.server.level.ServerLevel;

public interface CartAdvanceable {

  /**
   * Temporary fix that allows carts to perform actions when moving on tracks. </br>
   * We can't use {@link net.minecraft.world.entity.vehicle.AbstractMinecart#moveAlongTrack(ServerLevel) moveAlongTrack}
   * because it is only called in {@link net.minecraft.world.entity.vehicle.NewMinecartBehavior NewMinecartBehavior}
   */
  void advanceOnTrack(ServerLevel serverLevel);
}
