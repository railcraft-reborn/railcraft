/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.core;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayerFactory;

public final class RailcraftFakePlayer {
  private RailcraftFakePlayer() {}

  public static ServerPlayer get(ServerLevel level, double x, double y, double z) {
    var player = FakePlayerFactory.get(level, RailcraftConstants.FAKE_GAMEPROFILE);
    player.setPos(x, y, z);
    return player;
  }

  public static ServerPlayer get(ServerLevel level, final Vec3 pos) {
    return get(level, pos.x, pos.y, pos.z);
  }

  public static ServerPlayer get(ServerLevel level, final BlockPos pos) {
    return get(level, pos.getX(), pos.getY(), pos.getZ());
  }
}
