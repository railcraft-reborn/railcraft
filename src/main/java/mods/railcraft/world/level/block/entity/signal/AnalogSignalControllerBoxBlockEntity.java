package mods.railcraft.world.level.block.entity.signal;

import java.util.BitSet;
import java.util.EnumMap;
import java.util.Map;
import javax.annotation.Nullable;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SignalControllerProvider;
import mods.railcraft.api.signals.SimpleSignalController;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;

public class AnalogSignalControllerBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements SignalControllerProvider {

  private final SimpleSignalController controller =
      new SimpleSignalController("something", this);
  private int strongestSignal;

  public EnumMap<SignalAspect, BitSet> aspects = new EnumMap<>(SignalAspect.class);

  public AnalogSignalControllerBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.ANALOG_SIGNAL_CONTROLLER_BOX.get());
    for (SignalAspect aspect : SignalAspect.values()) {
      this.aspects.put(aspect, new BitSet());
    }
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      this.controller.tickClient();
      return;
    }
    this.controller.tickServer();
    SignalAspect prevAspect = this.controller.getAspect();
    if (this.controller.isLinking())
      this.controller.setAspect(SignalAspect.BLINK_YELLOW);
    else if (controller.isPaired())
      this.controller.setAspect(determineAspect());
    else
      this.controller.setAspect(SignalAspect.BLINK_RED);
    if (prevAspect != controller.getAspect())
      syncToClient();
  }

  @Override
  public void neighborChanged() {
    super.neighborChanged();
    if (this.level.isClientSide())
      return;
    int signal = this.getSignal();
    if (signal != this.strongestSignal) {
      this.strongestSignal = signal;
      this.syncToClient();
    }
  }

  private int getSignal() {
    int signal = 0, tmp;
    for (Direction direction : Direction.values()) {
      if (direction == Direction.UP)
        continue;
      if (this.adjacentCache.getTileOnSide(direction) instanceof AbstractSignalBoxBlockEntity)
        continue;
      if ((tmp = this.level.getSignal(this.getBlockPos(), direction)) > signal)
        signal = tmp;
      if ((tmp = this.level.getSignal(this.getBlockPos().below(), direction)) > signal)
        signal = tmp;
    }
    return signal;
  }

  private SignalAspect determineAspect() {
    SignalAspect aspect = SignalAspect.OFF;
    for (Map.Entry<SignalAspect, BitSet> entry : aspects.entrySet()) {
      SignalAspect current = entry.getKey();
      if (entry.getValue().get(strongestSignal))
        aspect =
            (aspect == SignalAspect.OFF) ? current : SignalAspect.mostRestrictive(aspect, current);
    }
    return aspect;
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putInt("strongestSignal", strongestSignal);

    ListNBT aspectsNbt = new ListNBT();
    for (Map.Entry<SignalAspect, BitSet> entry : aspects.entrySet()) {
      CompoundNBT nbt = new CompoundNBT();
      nbt.putInt("id", entry.getKey().getId());
      nbt.putByteArray("bytes", entry.getValue().toByteArray());
    }
    data.put("aspects", aspectsNbt);

    controller.writeToNBT(data);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.strongestSignal = data.getInt("strongestSignal");

    this.aspects.clear();
    ListNBT aspectsNbt = data.getList("aspects", Constants.NBT.TAG_COMPOUND);
    for (INBT nbt : aspectsNbt) {
      CompoundNBT compoundNbt = (CompoundNBT) nbt;
      this.aspects.put(SignalAspect.getById(compoundNbt.getInt("id")),
          BitSet.valueOf(compoundNbt.getByteArray("bytes")));
    }

    this.controller.readFromNBT(data);
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);

    for (Map.Entry<SignalAspect, BitSet> entry : aspects.entrySet()) {
      byte[] bytes = entry.getValue().toByteArray();
      data.writeVarInt(bytes.length);
      data.writeBytes(bytes);
    }

    controller.writeSyncData(data);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);

    this.aspects.clear();
    for (SignalAspect aspect : SignalAspect.values()) {
      BitSet bitSet = BitSet.valueOf(data.readByteArray(data.readVarInt()));
      this.aspects.put(aspect, bitSet);
    }

    controller.readSyncData(data);
  }

  @Override
  public SignalAspect getBoxSignalAspect(@Nullable Direction side) {
    return controller.getAspect();
  }

  @Override
  public SimpleSignalController getController() {
    return controller;
  }
}
