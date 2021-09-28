/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signals;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class BlockSignalRelay extends BlockSignalNetwork {

  private final Map<BlockPos, SignalAspect> aspects = new HashMap<>();

  public BlockSignalRelay(TileEntity blockEntity, Runnable sync) {
    super(blockEntity, 2, sync);
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
    if (!hasPeers()) {
      return SignalAspect.BLINK_RED;
    }
    SignalAspect aspect = SignalAspect.GREEN;
    for (BlockPos otherCoord : getPeers()) {
      aspect = SignalAspect.mostRestrictive(aspect, aspects.get(otherCoord));
    }
    return aspect;
  }

  @Override
  protected SignalAspect getSignalAspectForPeer(BlockPos otherCoord) {
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
  public CompoundNBT serializeNBT() {
    CompoundNBT tag = super.serializeNBT();
    ListNBT aspectsTag = new ListNBT();
    for (Map.Entry<BlockPos, SignalAspect> entry : this.aspects.entrySet()) {
      CompoundNBT entryTag = new CompoundNBT();
      if (entry.getKey() != null && entry.getValue() != null) {
        entryTag.put("pos", NBTUtil.writeBlockPos(entry.getKey()));
        entryTag.putString("aspect", entry.getValue().getName());
        aspectsTag.add(entryTag);
      }
    }
    tag.put("aspects", aspectsTag);
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundNBT tag) {
    super.deserializeNBT(tag);
    ListNBT aspectsTag = tag.getList("aspects", Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < aspectsTag.size(); i++) {
      CompoundNBT entryTag = aspectsTag.getCompound(i);
      BlockPos pos = NBTUtil.readBlockPos(entryTag.getCompound("pos"));
      SignalAspect aspect = SignalAspect.getByName(entryTag.getString("aspect"))
          .orElse(SignalAspect.OFF);
      this.aspects.put(pos, aspect);
    }
  }
}
