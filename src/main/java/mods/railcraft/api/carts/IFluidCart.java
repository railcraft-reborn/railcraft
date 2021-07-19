/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.carts;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraftforge.fluids.FluidStack;

/**
 * Replaces ILiquidTransfer with a simpler interface for moving Fluids between Minecarts.
 * <p/>
 * Created by CovertJaguar on 5/9/2015.
 *
 * @see mods.railcraft.api.carts.ITrainTransferHelper
 */
public interface IFluidCart {
    /**
     * This function controls whether a cart can pass push or pull requests.
     * This function is only called if the cart cannot fulfill the request itself.
     * <p/>
     * If this interface is not implemented, a default value will be inferred based on the size of the tanks of the Minecart.
     * Anything with eight or more buckets will be assumed to allow passage, but only if the contained fluid matches the request.
     *
     * @return true if can pass push and pull requests
     */
    boolean canPassFluidRequests(FluidStack fluid);

    /**
     * This function controls whether a cart will accept a pushed Fluid.
     * Even if this function returns true, there still must be a tank that accepts the Fluid in question before it can be added to the cart.
     * <p/>
     * If this interface is not implemented, it is assumed to be true.
     *
     * @param requester the AbstractMinecartEntity that initiated the action
     * @param fluid     the Fluid
     * @return true if cart will accept the fluid
     */
    boolean canAcceptPushedFluid(AbstractMinecartEntity requester, FluidStack fluid);

    /**
     * This function controls whether a cart will fulfill a pull request for a specific Fluid.
     * Even if this function returns true, there still must be a tank that can extract the Fluid in question before it can be removed from the cart.
     * <p/>
     * If this interface is not implemented, it is assumed to be true.
     *
     * @param requester the AbstractMinecartEntity that initiated the action
     * @param fluid     the Fluid
     * @return true if the cart can provide the fluid
     */
    boolean canProvidePulledFluid(AbstractMinecartEntity requester, FluidStack fluid);

    /**
     * Set by the Liquid Loader while filling, primarily used for rendering a
     * visible change while being filled.
     *
     * @param filling true if the cart is being filled from above
     */
    default void setFilling(boolean filling) {
    }
}
