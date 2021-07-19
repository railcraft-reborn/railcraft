/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.items;

import javax.annotation.Nullable;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * This interface should be implemented by any cart item, but it is generally optional.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IMinecartItem {

  /**
   * Controls whether this cart item can be placed by the Cart and Train Dispensers.
   * <p/>
   * Generally, you can ignore the placeCart() function if this returns false.
   *
   * @return true if it can be placed, false otherwise
   */
  boolean canBePlacedByNonPlayer(ItemStack cart);

  /**
   * Places a cart at the specified location.
   * <p/>
   * Implementing this function is optional.
   *
   * @param owner the name of the player placing the cart or "[MyMod]" with the brackets
   * @param cart An ItemStack that contains the cart
   * @param world The World
   * @return the cart placed or null if failed
   */
  @Nullable
  AbstractMinecartEntity placeCart(GameProfile owner, ItemStack cart, ServerWorld world,
      BlockPos pos);
}
