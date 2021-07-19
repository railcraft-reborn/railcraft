/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signals;

import mods.railcraft.api.core.CollectionToolsAPI;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class BlockSignalRelay extends BlockSignal {

  private final Map<BlockPos, SignalAspect> aspects =
      CollectionToolsAPI.blockPosMap(new HashMap<>());

  public BlockSignalRelay(String locTag, TileEntity tile) {
    super(locTag, tile, 2);
  }

  @Override
  protected void updateSignalAspect() {
    aspects.keySet().retainAll(getPeers());
    for (BlockPos otherCoord : getPeers()) {
      aspects.put(otherCoord, determineAspect(otherCoord));
    }
  }

  @Override
  public SignalAspect getSignalAspect() {
    if (isWaitingForRetest() || isLinking()) {
      return SignalAspect.BLINK_YELLOW;
    }
    if (!isPaired()) {
      return SignalAspect.BLINK_RED;
    }
    SignalAspect aspect = SignalAspect.GREEN;
    for (BlockPos otherCoord : getPeers()) {
      aspect = SignalAspect.mostRestrictive(aspect, aspects.get(otherCoord));
    }
    return aspect;
  }

  @Override
  protected SignalAspect getSignalAspectForPair(BlockPos otherCoord) {
    SignalAspect aspect = SignalAspect.GREEN;
    for (Map.Entry<BlockPos, SignalAspect> entry : aspects.entrySet()) {
      if (entry.getKey().equals(otherCoord)) {
        continue;
      }
      aspect = SignalAspect.mostRestrictive(aspect, entry.getValue());
    }
    return aspect;
  }

  @Override
  protected void saveNBT(CompoundNBT data) {
    super.saveNBT(data);
    ListNBT tagList = data.getList("aspects", 10);
    for (int i = 0; i < tagList.size(); i++) {
      CompoundNBT nbt = tagList.getCompound(i);
      BlockPos coord = SignalTools.readFromNBT(nbt, "coord");
      SignalAspect aspect = SignalAspect.readFromNBT(nbt, "aspect");
      aspects.put(coord, aspect);
    }
  }

  @Override
  protected void loadNBT(CompoundNBT data) {
    super.loadNBT(data);
    ListNBT tagList = new ListNBT();
    for (Map.Entry<BlockPos, SignalAspect> entry : aspects.entrySet()) {
      CompoundNBT nbt = new CompoundNBT();
      if (entry.getKey() != null && entry.getValue() != null) {
        SignalTools.writeToNBT(nbt, "coord", entry.getKey());
        entry.getValue().writeToNBT(nbt, "aspect");
        tagList.add(nbt);
      }
    }
    data.put("aspects", tagList);
  }
}
