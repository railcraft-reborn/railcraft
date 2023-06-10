/*------------------------------------------------------------------------------
 Copyright (c) Railcraft, 2011-2023

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;

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
  boolean canWhack(Player player, InteractionHand hand, ItemStack crowbar, BlockPos pos);

  /**
   * Callback to do damage to the item.
   *
   * @param player the player
   * @param crowbar the crowbar
   * @param pos the block
   */
  void onWhack(Player player, InteractionHand hand, ItemStack crowbar, BlockPos pos);

  /**
   * Controls whether you can link a cart.
   *
   * @param player the player
   * @param crowbar the crowbar
   * @param cart the cart @return true if can link a cart
   */
  boolean canLink(Player player, InteractionHand hand, ItemStack crowbar, AbstractMinecart cart);

  /**
   * Callback to do damage.
   *
   * @param player the player
   * @param crowbar the crowbar
   * @param cart the cart
   */
  void onLink(Player player, InteractionHand hand, ItemStack crowbar, AbstractMinecart cart);

  /**
   * Controls whether you can boost a cart.
   *
   * @param player the player
   * @param crowbar the crowbar
   * @param cart the cart @return true if can boost a cart
   */
  boolean canBoost(Player player, InteractionHand hand, ItemStack crowbar, AbstractMinecart cart);

  /**
   * Callback to do damage, boosting a cart usually does more damage than normal usage.
   *
   * @param player the player
   * @param crowbar the crowbar
   * @param cart the cart
   */
  void onBoost(Player player, InteractionHand hand, ItemStack crowbar, AbstractMinecart cart);
}
