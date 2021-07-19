/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.carts;

public interface IExplosiveCart {

    /**
     * If set to true the cart should explode after
     * whatever fuse duration is set.
     *
     * @param primed true if ready to explode
     */
    void setPrimed(boolean primed);

    /**
     * Returns whether the cart is primed to explode.
     *
     * @return primed
     */
    boolean isPrimed();

    /**
     * Returns the length of the current fuse.
     *
     * @return fuse length in ticks
     */
    int getFuse();

    /**
     * Optional function to allow setting the fuse duration.
     *
     * Used by the Priming Track.
     *
     * @param fuse in ticks
     */
    void setFuse(int fuse);

    /**
     * Returns the blast radius, but I don't think anything currently uses this.
     *
     * @return blast radius
     */
    float getBlastRadius();

    /**
     * Optional function to allow setting the blast radius.
     *
     * @param radius the blast radius
     */
    void setBlastRadius(float radius);

    /**
     * Causes the cart to explode immediately.
     */
    void explode();
}
