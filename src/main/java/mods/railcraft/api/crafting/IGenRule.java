/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.crafting;

import net.minecraft.util.text.ITextComponent;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

/**
 *
 */
public interface IGenRule extends Predicate<Random> {

    /**
     * Returns if this rule permits the generation of the output entry.
     *
     * <p>The returned result of this method should be consistent.</p>
     *
     * @param random The random instance to test against
     * @return True if an output entry can be generated
     */
    @Override
    boolean test(Random random);

    /**
     * Returns brief description of the rules for generating this entry.
     */
    List<ITextComponent> getToolTip();
}
