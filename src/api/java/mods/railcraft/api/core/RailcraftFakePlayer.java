/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.core;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.UUID;

/**
 * Created by CovertJaguar on 3/31/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public final class RailcraftFakePlayer {
  private RailcraftFakePlayer() {}

  public static final GameProfile RAILCRAFT_USER_PROFILE =
      new GameProfile(UUID.nameUUIDFromBytes(RailcraftConstantsAPI.RAILCRAFT_PLAYER.getBytes()),
          RailcraftConstantsAPI.RAILCRAFT_PLAYER);
  public static final GameProfile UNKNOWN_USER_PROFILE =
      new GameProfile(null, RailcraftConstantsAPI.UNKNOWN_PLAYER);

  public static ServerPlayer get(final ServerLevel world, final double x, final double y,
      final double z) {
    ServerPlayer player = FakePlayerFactory.get(world, RAILCRAFT_USER_PROFILE);
    player.setPos(x, y, z);
    return player;
  }

  public static ServerPlayer get(final ServerLevel world, final BlockPos pos) {
    ServerPlayer player = FakePlayerFactory.get(world, RAILCRAFT_USER_PROFILE);
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
