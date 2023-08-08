package mods.railcraft.charge;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mods.railcraft.api.charge.Charge;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public final class ChargeSavedData extends SavedData {

  private static final int ABSENT_VALUE = -1;

  private static final String DATA_TAG_PREFIX = "railcraft.charge.";
  private final Object2IntMap<BlockPos> chargeLevels =
      Util.make(new Object2IntOpenHashMap<>(), map -> map.defaultReturnValue(ABSENT_VALUE));

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
    for (var entry : this.chargeLevels.object2IntEntrySet()) {
      var entryTag = new CompoundTag();
      entryTag.put("pos", NbtUtils.writeBlockPos(entry.getKey()));
      entryTag.putInt("value", entry.getIntValue());
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
    battery.setEnergyStored(this.chargeLevels.computeIfAbsent(battery.getBlockPos(),
        __ -> battery.getInitialCharge()));
    this.setDirty();
  }

  public void updateBatteryRecord(ChargeStorageBlockImpl battery) {
    this.chargeLevels.put(battery.getBlockPos(), battery.getEnergyStored());
    this.setDirty();
  }

  public void removeBattery(BlockPos pos) {
    if (this.chargeLevels.removeInt(pos) != ABSENT_VALUE) {
      this.setDirty();
    }
  }
}
