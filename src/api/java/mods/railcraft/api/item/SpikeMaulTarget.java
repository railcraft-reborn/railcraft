/*------------------------------------------------------------------------------
 Copyright (c) Railcraft, 2011-2023

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.item;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;

public interface SpikeMaulTarget {

  List<? extends Supplier<? extends Block>> getSpikeMaulVariants();
}
