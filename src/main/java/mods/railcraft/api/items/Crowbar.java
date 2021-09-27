/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.items;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
@ActivationBlockingItem
public interface Crowbar {

  /**
   * Controls non-rotational interactions with blocks. Crowbar specific stuff.
   * <p/>
   * Rotational interaction is handled by the Block.rotateBlock() function, which should be called
   * from the Item.onUseFirst() function of your tool.
   *
   * @param player the player
   * @param crowbar the crowbar
   * @param pos the block @return true if can whack a block
   */
  boolean canWhack(PlayerEntity player, Hand hand, ItemStack crowbar, BlockPos pos);

  /**
   * Callback to do damage to the item.
   *
   * @param player the player
   * @param crowbar the crowbar
   * @param pos the block
   */
  void onWhack(PlayerEntity player, Hand hand, ItemStack crowbar, BlockPos pos);

  /**
   * Controls whether you can link a cart.
   *
   * @param player the player
   * @param crowbar the crowbar
   * @param cart the cart @return true if can link a cart
   */
  boolean canLink(PlayerEntity player, Hand hand, ItemStack crowbar, AbstractMinecartEntity cart);

  /**
   * Callback to do damage.
   *
   * @param player the player
   * @param crowbar the crowbar
   * @param cart the cart
   */
  void onLink(PlayerEntity player, Hand hand, ItemStack crowbar, AbstractMinecartEntity cart);

  /**
   * Controls whether you can boost a cart.
   *
   * @param player the player
   * @param crowbar the crowbar
   * @param cart the cart @return true if can boost a cart
   */
  boolean canBoost(PlayerEntity player, Hand hand, ItemStack crowbar, AbstractMinecartEntity cart);

  /**
   * Callback to do damage, boosting a cart usually does more damage than normal usage.
   *
   * @param player the player
   * @param crowbar the crowbar
   * @param cart the cart
   */
  void onBoost(PlayerEntity player, Hand hand, ItemStack crowbar, AbstractMinecartEntity cart);
}
