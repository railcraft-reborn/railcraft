/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.carts;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;

/**
 * This class replaces IItemTransfer for controlling how items move through a train.
 * It is entirely optional to implement this class, default values will be determined based on several factors.
 *
 * It is not required that every cart implementing this also has an inventory, but if you wish to accept or provide
 * items you should implement IInventory or provide an IItemHandler capability.
 *
 * <p/>
 * Created by CovertJaguar on 5/9/2015.
 *
 * @see mods.railcraft.api.carts.TrainTransferHelper
 */
public interface IItemCart {
    /**
     * This function controls whether a cart can pass push or pull requests.
     * This function is only called if the cart cannot fulfill the request itself.
     * <p/>
     * If this interface is not implemented, a default value will be inferred based on the size of the inventory of the Minecart.
     * Anything with eight or more slots will be assumed to allow passage.
     *
     * @return true if can pass push and pull requests
     */
    boolean canPassItemRequests(ItemStack stack);

    /**
     * This function controls whether a cart will accept a pushed Item.
     * Even if this function returns true, there still must be a slot that accepts the item in question before it can be added to the cart.
     * <p/>
     * If this interface is not implemented, it is assumed to be true.
     *
     * @param requester the AbstractMinecartEntity that initiated the action
     * @param stack     the ItemStack
     * @return true if the cart can accept the item
     */
    boolean canAcceptPushedItem(AbstractMinecart requester, ItemStack stack);

    /**
     * This function controls whether a cart will fulfill a pull request for a specific item.
     * Even if this function returns true, there still must be a slot that can extract the item in question before it can be removed from the cart.
     * <p/>
     * If this interface is not implemented, it is assumed to be true.
     *
     * @param requester the AbstractMinecartEntity that initiated the action
     * @param stack     the ItemStack
     * @return true if the cart can provide the item
     */
    boolean canProvidePulledItem(AbstractMinecart requester, ItemStack stack);
}
