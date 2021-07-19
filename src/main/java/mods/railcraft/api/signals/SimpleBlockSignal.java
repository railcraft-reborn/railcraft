/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signals;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class SimpleBlockSignal extends BlockSignal {

    private SignalAspect aspect = SignalAspect.BLINK_RED;

    public SimpleBlockSignal(String locTag, TileEntity tile) {
        super(locTag, tile, 1);
    }

    @Override
    public void updateSignalAspect() {
        aspect = determineAspect(peers.peek());
    }

    @Override
    public SignalAspect getSignalAspect() {
        return aspect;
    }

    @Override
    protected SignalAspect getSignalAspectForPair(BlockPos otherCoord) {
        return SignalAspect.GREEN;
    }

    @Override
    protected void saveNBT(CompoundNBT data) {
        super.saveNBT(data);
        aspect.writeToNBT(data, "aspect");
    }

    @Override
    protected void loadNBT(CompoundNBT data) {
        super.loadNBT(data);
        aspect = SignalAspect.readFromNBT(data, "aspect");
    }
}
