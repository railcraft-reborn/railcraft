/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.core;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.UUID;

/**
 * Created by CovertJaguar on 3/31/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class RailcraftFakePlayer {
  private RailcraftFakePlayer() {}

  public static final GameProfile RAILCRAFT_USER_PROFILE =
      new GameProfile(UUID.nameUUIDFromBytes(RailcraftConstantsAPI.RAILCRAFT_PLAYER.getBytes()),
          RailcraftConstantsAPI.RAILCRAFT_PLAYER);
  public static final GameProfile UNKNOWN_USER_PROFILE =
      new GameProfile(null, RailcraftConstantsAPI.UNKNOWN_PLAYER);

  public static ServerPlayerEntity get(final ServerWorld world, final double x, final double y,
      final double z) {
    ServerPlayerEntity player = FakePlayerFactory.get(world, RAILCRAFT_USER_PROFILE);
    player.setPos(x, y, z);
    return player;
  }

  public static ServerPlayerEntity get(final ServerWorld world, final BlockPos pos) {
    ServerPlayerEntity player = FakePlayerFactory.get(world, RAILCRAFT_USER_PROFILE);
    player.setPos(pos.getX(), pos.getY(), pos.getZ());
    return player;
  }

  public static ServerPlayerEntity get(ServerWorld world, double x, double y, double z,
      ItemStack stack, Hand hand) {
    ServerPlayerEntity player = get(world, x, y, z);
    player.setItemInHand(hand, stack);
    return player;
  }

  public static ServerPlayerEntity get(ServerWorld world, BlockPos pos, ItemStack stack,
      Hand hand) {
    ServerPlayerEntity player = get(world, pos);
    player.setItemInHand(hand, stack);
    return player;
  }
}
