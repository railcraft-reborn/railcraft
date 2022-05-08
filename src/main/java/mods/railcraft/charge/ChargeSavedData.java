package mods.railcraft.charge;

import java.util.HashMap;
import java.util.Map;
import mods.railcraft.api.charge.Charge;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * Created by CovertJaguar on 8/1/2016 for Railcraft.
 */
public final class ChargeSavedData extends SavedData {

  private static final String DATA_TAG_PREFIX = "railcraft.charge.";
  private final Map<BlockPos, Integer> chargeLevels = new HashMap<>();

  public static ChargeSavedData getFor(Charge network, ServerLevel level) {
    return level.getDataStorage().computeIfAbsent(tag -> {
      var manager = new ChargeSavedData();
      manager.load(tag);
      return manager;

    }, ChargeSavedData::new, DATA_TAG_PREFIX + network.getSerializedName());
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    var batteriesTag = new ListTag();
    for (var entry : this.chargeLevels.entrySet()) {
      var entryTag = new CompoundTag();
      entryTag.put("pos", NbtUtils.writeBlockPos(entry.getKey()));
      entryTag.putInt("value", entry.getValue());
      batteriesTag.add(entryTag);
    }
    tag.put("batteries", batteriesTag);
    return tag;
  }

  private void load(CompoundTag tag) {
    var batteriesTag = tag.getList("batteries", Tag.TAG_COMPOUND);
    for (int i = 0; i < batteriesTag.size(); i++) {
      var entryTag = batteriesTag.getCompound(i);
      var pos = NbtUtils.readBlockPos(entryTag.getCompound("pos"));
      if (pos != null) {
        this.chargeLevels.put(pos, entryTag.getInt("value"));
      }
    }
  }

  public void initBattery(ChargeStorageBlockImpl battery) {
    battery.setEnergyStored(
        this.chargeLevels.computeIfAbsent(battery.getBlockPos(),
            blockPos -> battery.getInitialCharge()));
    this.setDirty();
  }

  public void updateBatteryRecord(ChargeStorageBlockImpl battery) {
    this.chargeLevels.put(battery.getBlockPos(), battery.getEnergyStored());
    this.setDirty();
  }

  public void removeBattery(BlockPos pos) {
    if (this.chargeLevels.remove(pos) != null) {
      this.setDirty();
    }
  }
}
