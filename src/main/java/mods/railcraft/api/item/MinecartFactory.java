/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.item;

import javax.annotation.Nullable;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;

@FunctionalInterface
public interface MinecartFactory {

  @Nullable
  AbstractMinecartEntity createMinecart(
      ItemStack itemStack, double x, double y, double z, ServerWorld level);
}
