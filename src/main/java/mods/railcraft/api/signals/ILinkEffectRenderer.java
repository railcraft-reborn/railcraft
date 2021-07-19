/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signals;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
@OnlyIn(Dist.CLIENT)
public interface ILinkEffectRenderer {

  boolean isTuningAuraActive();

  void tuningEffect(TileEntity start, TileEntity dest);
}
