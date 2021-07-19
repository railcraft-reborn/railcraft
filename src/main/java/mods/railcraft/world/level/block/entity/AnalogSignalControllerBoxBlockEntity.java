package mods.railcraft.world.level.block.entity;

import java.util.BitSet;
import java.util.EnumMap;
import java.util.Map;
import javax.annotation.Nullable;
import mods.railcraft.api.signals.IControllerProvider;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SimpleSignalController;
import mods.railcraft.plugins.PowerPlugin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

public class AnalogSignalControllerBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements IControllerProvider {

  private final SimpleSignalController controller =
      new SimpleSignalController("something", this);
  private int strongestSignal;

  public EnumMap<SignalAspect, BitSet> aspects = new EnumMap<>(SignalAspect.class);

  public AnalogSignalControllerBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.ANALOG_SIGNAL_CONTROLLER_BOX.get());
    for (SignalAspect aspect : SignalAspect.values()) {
      aspects.put(aspect, new BitSet());
    }
  }

  @Override
  public void tick() {
    super.tick();

    if (this.level.isClientSide()) {
      controller.tickClient();
      return;
    }
    controller.tickServer();
    SignalAspect prevAspect = controller.getAspect();
    if (controller.isLinking())
      controller.setAspect(SignalAspect.BLINK_YELLOW);
    else if (controller.isPaired())
      controller.setAspect(determineAspect());
    else
      controller.setAspect(SignalAspect.BLINK_RED);
    if (prevAspect != controller.getAspect())
      sendUpdateToClient();
  }

  @Override
  public void neighborChanged(BlockState state, Block neighborBlock, BlockPos pos) {
    super.neighborChanged(state, neighborBlock, pos);
    if (this.level.isClientSide())
      return;
    int s = getPowerLevel();
    if (s != strongestSignal) {
      strongestSignal = s;
      sendUpdateToClient();
    }
  }

  private int getPowerLevel() {
    int p = 0, tmp;
    for (Direction side : Direction.values()) {
      if (side == Direction.UP)
        continue;
      if (adjacentCache.getTileOnSide(side) instanceof AbstractSignalBoxBlockEntity)
        continue;
      if ((tmp = PowerPlugin.getSignal(this.level, getBlockPos(), side)) > p)
        p = tmp;
      if ((tmp = PowerPlugin.getSignal(this.level, getBlockPos().below(), side)) > p)
        p = tmp;
    }
    return p;
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
    strongestSignal = data.getInt("strongestSignal");

    this.aspects.clear();
    ListNBT aspectsNbt = data.getList("aspects", Constants.NBT.TAG_COMPOUND);
    for (INBT nbt : aspectsNbt) {
      CompoundNBT compoundNbt = (CompoundNBT) nbt;
      this.aspects.put(SignalAspect.byId(compoundNbt.getInt("id")),
          BitSet.valueOf(compoundNbt.getByteArray("bytes")));
    }

    controller.readFromNBT(data);
  }

  @Override
  public void writePacketData(PacketBuffer data) {
    super.writePacketData(data);

    for (Map.Entry<SignalAspect, BitSet> entry : aspects.entrySet()) {
      byte[] bytes = entry.getValue().toByteArray();
      data.writeVarInt(bytes.length);
      data.writeBytes(bytes);
    }

    controller.writePacketData(data);
  }

  @Override
  public void readPacketData(PacketBuffer data) {
    super.readPacketData(data);

    this.aspects.clear();
    for (SignalAspect aspect : SignalAspect.values()) {
      BitSet bitSet = BitSet.valueOf(data.readByteArray(data.readVarInt()));
      this.aspects.put(aspect, bitSet);
    }

    controller.readPacketData(data);
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
