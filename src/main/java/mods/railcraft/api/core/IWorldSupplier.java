/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.core;

import net.minecraft.world.World;
import javax.annotation.Nullable;

import java.util.Objects;

/**
 * Created by CovertJaguar on 12/17/2018 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IWorldSupplier {

    @Nullable World theWorld();

    /**
     * A slightly more dangerous version.
     *
     * Be sure you only used this when you _know_ the world isn't null.
     */
    default World theWorldAsserted() {
        return Objects.requireNonNull(theWorld());
    }
}
