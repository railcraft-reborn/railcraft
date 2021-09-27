package mods.railcraft.world.level.block.entity.signal;

import javax.annotation.Nullable;
import mods.railcraft.api.signals.BlockSignal;
import mods.railcraft.api.signals.BlockSignalRelay;
import mods.railcraft.api.signals.IBlockSignal;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SimpleSignalController;
import mods.railcraft.plugins.PowerPlugin;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public class BlockSignalRelayBoxBlockEntity extends ActionSignalBoxBlockEntity
    implements IBlockSignal, IAspectProvider, IRedstoneEmitter {

  private final SimpleSignalController controller =
      new SimpleSignalController("nothing", this);
  private final BlockSignal blockSignal = new BlockSignalRelay("nothing", this);

  public BlockSignalRelayBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_RELAY_BOX.get());
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      controller.tickClient();
      blockSignal.tickClient();
      return;
    }
    controller.tickServer();
    blockSignal.tickServer();
    SignalAspect prevAspect = controller.getAspect();
    if (controller.isLinking())
      controller.setAspect(SignalAspect.BLINK_YELLOW);
    else
      controller.setAspect(blockSignal.getSignalAspect());
    if (prevAspect != controller.getAspect()) {
      updateNeighboringSignalBoxes();
      sendUpdateToClient();
    }
  }

  private void updateNeighboringSignalBoxes() {
    notifyBlocksOfNeighborChange();
    for (Direction side : Direction.Plane.HORIZONTAL) {
      TileEntity tile = this.adjacentCache.getTileOnSide(side);
      if (tile instanceof AbstractSignalBoxBlockEntity) {
        AbstractSignalBoxBlockEntity box = (AbstractSignalBoxBlockEntity) tile;
        box.neighboringSignalBoxChanged(this, side);
      }
    }
  }

  @Override
  public int getSignal(Direction side) {
    if (this.adjacentCache
        .getTileOnSide(side.getOpposite()) instanceof AbstractSignalBoxBlockEntity) {
      return PowerPlugin.NO_POWER;
    }
    return this.isEmittingRedstone(side) ? PowerPlugin.FULL_POWER : PowerPlugin.NO_POWER;
  }

  @Override
  public boolean isEmittingRedstone(Direction side) {
    return doesActionOnAspect(getBoxSignalAspect(side));
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);

    blockSignal.writeToNBT(data);
    controller.writeToNBT(data);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);

    blockSignal.readFromNBT(data);
    controller.readFromNBT(data);
  }

  @Override
  public void writePacketData(PacketBuffer data) {
    super.writePacketData(data);
    controller.writePacketData(data);
    blockSignal.writePacketData(data);
  }

  @Override
  public void readPacketData(PacketBuffer data) {
    super.readPacketData(data);
    controller.readPacketData(data);
    blockSignal.readPacketData(data);
  }

  @Override
  public BlockSignal getBlockSignal() {
    return this.blockSignal;
  }

  @Override
  public void doActionOnAspect(SignalAspect aspect, boolean trigger) {
    super.doActionOnAspect(aspect, trigger);
    updateNeighboringSignalBoxes();
  }

  @Override
  public SignalAspect getBoxSignalAspect(@Nullable Direction side) {
    return controller.getAspect();
  }

  @Override
  public boolean canTransferAspect() {
    return true;
  }

  @Override
  public SignalAspect getTriggerAspect() {
    return getBoxSignalAspect(null);
  }

}
