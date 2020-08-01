/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020
 http://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/

package mods.railcraft.common.blocks.aesthetics.brick;

import mods.railcraft.common.blocks.IRailcraftBlock;
import mods.railcraft.common.plugins.forge.CraftingPlugin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

import java.util.Objects;

import static mods.railcraft.common.blocks.aesthetics.brick.BrickVariant.BRICK;

/**
 * Created by CovertJaguar on 7/29/2020 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class BlockBrickStairs extends BlockStairs implements IRailcraftBlock {
    public final BrickTheme brickTheme;

    public BlockBrickStairs(BrickTheme brickTheme) {
        super(Objects.requireNonNull(brickTheme.getState(BRICK)));
        this.brickTheme = brickTheme;
    }

    @Override
    public void defineRecipes() {
        CraftingPlugin.addShapedRecipe(getStack(8),
                "I  ",
                "II ",
                "III",
                'I', brickTheme, BRICK);
        CraftingPlugin.addShapedRecipe(brickTheme.getStack(3, BRICK),
                "II",
                "II",
                'I', getStack());
    }

    @Override
    public Block getObject() {
        return this;
    }
}