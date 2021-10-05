/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.carts;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import javax.annotation.Nullable;

import java.util.Set;

/**
 * This interface it used to define an item that can
 * be used as a bore head for the Tunnel Bore.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface IBoreHead {

    /**
     * Modify this set to allow bore heads to have other tool classes by default.
     */
    Set<String> toolClasses = Sets.newHashSet("pickaxe", "axe", "shovel");

    /**
     * Return the texture file used for this bore head.
     *
     * @return The texture file path
     */
    ResourceLocation getBoreTexture();

    /**
     * Return the harvest level of this bore head.
     *
     * This value is compared against the tool classes
     * "pickaxe", "axe", and "shovel" to determine if the
     * block is harvestable by the bore head.
     *
     * @return The harvest level
     */
    default int getHarvestLevel() {
        return 0;
    }

    /**
     * Return the dig speed modifier of this bore head.
     *
     * This value controls how much faster or slow this bore head
     * mines each layer compared to the default time.
     *
     * @return The dig speed modifier
     */
    double getDigModifier();

    /**
     * Exists for ease of implementation.
     *
     * Call {@code IBoreHead.super.getHarvestLevel} to call this method in a subclass.
     */
    default int getHarvestLevel(ItemStack stack, String toolClass, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
        return getToolClasses(stack).contains(toolClass) ? getHarvestLevel() : -1;
    }

    /**
     * Exists for ease of implementation.
     *
     * Call {@code IBoreHead.super.getToolClasses} to call this method in a subclass.
     */
    default Set<String> getToolClasses(ItemStack stack) {
        return toolClasses;
    }
}
