/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.item;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;

/**
 * Created by CovertJaguar on 3/6/2017 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface SpikeMaulTarget {

  List<? extends Supplier<? extends Block>> getSpikeMaulVariants();
}
