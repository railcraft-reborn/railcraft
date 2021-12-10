/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.item;

import javax.annotation.Nullable;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;

@FunctionalInterface
public interface MinecartFactory {

  @Nullable
  AbstractMinecart createMinecart(
      ItemStack itemStack, double x, double y, double z, ServerLevel level);
}
