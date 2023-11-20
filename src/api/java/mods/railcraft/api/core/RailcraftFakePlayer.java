/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.core;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.FakePlayerFactory;

public final class RailcraftFakePlayer {
  private RailcraftFakePlayer() {}

  public static ServerPlayer get(final ServerLevel world, final double x, final double y,
      final double z) {
    ServerPlayer player = FakePlayerFactory.get(world, RailcraftConstants.FAKE_GAMEPROFILE);
    player.setPos(x, y, z);
    return player;
  }

  public static ServerPlayer get(final ServerLevel world, final BlockPos pos) {
    ServerPlayer player = FakePlayerFactory.get(world, RailcraftConstants.FAKE_GAMEPROFILE);
    player.setPos(pos.getX(), pos.getY(), pos.getZ());
    return player;
  }

  public static ServerPlayer get(ServerLevel world, double x, double y, double z,
      ItemStack stack, InteractionHand hand) {
    ServerPlayer player = get(world, x, y, z);
    player.setItemInHand(hand, stack);
    return player;
  }

  public static ServerPlayer get(ServerLevel world, BlockPos pos, ItemStack stack,
      InteractionHand hand) {
    ServerPlayer player = get(world, pos);
    player.setItemInHand(hand, stack);
    return player;
  }
}
