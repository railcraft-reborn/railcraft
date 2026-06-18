package mods.railcraft.charge;

import java.util.List;
import org.jspecify.annotations.Nullable;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Util;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public final class ChargeSavedData extends SavedData {

  private static final int ABSENT_VALUE = -1;

  private static final SavedDataType<ChargeSavedData> TYPE = new SavedDataType<>(
      RailcraftConstants.id("charge.distribution"),
      ChargeSavedData::new,
      __ -> RecordCodecBuilder.create(instance -> instance.group(
          Codec.pair(
              BlockPos.CODEC.fieldOf(CompoundTagKeys.POS).codec(),
              ExtraCodecs.NON_NEGATIVE_INT.fieldOf(CompoundTagKeys.VALUE).codec()
          ).listOf().fieldOf(CompoundTagKeys.BATTERIES).forGetter(data -> from(data.chargeLevels))
      ).apply(instance, ChargeSavedData::new)));

  private final Object2IntMap<BlockPos> chargeLevels =
      Util.make(new Object2IntOpenHashMap<>(), map -> map.defaultReturnValue(ABSENT_VALUE));

  private ChargeSavedData(@Nullable ServerLevel level) {
  }

  private ChargeSavedData(List<Pair<BlockPos, Integer>> batteries) {
    for (var battery : batteries) {
      this.chargeLevels.put(battery.getFirst(), battery.getSecond().intValue());
    }
  }

  private static List<Pair<BlockPos, Integer>> from(Object2IntMap<BlockPos> chargeLevels) {
    return chargeLevels.object2IntEntrySet().stream()
        .map(x -> new Pair<>(x.getKey(), x.getIntValue()))
        .toList();
  }

  public void initBattery(ChargeStorageBlockImpl battery) {
    battery.setEnergyStored(this.chargeLevels.computeIfAbsent(battery.getBlockPos(),
        __ -> battery.getInitialCharge()));
    this.setDirty();
  }

  public void updateBatteryRecord(ChargeStorageBlockImpl battery) {
    this.chargeLevels.put(battery.getBlockPos(), battery.getAmountAsInt());
    this.setDirty();
  }

  public void removeBattery(BlockPos pos) {
    if (this.chargeLevels.removeInt(pos) != ABSENT_VALUE) {
      this.setDirty();
    }
  }

  public static ChargeSavedData getFor(ServerLevel level) {
    return level.getDataStorage().computeIfAbsent(TYPE);
  }
}
