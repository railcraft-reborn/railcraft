package mods.railcraft.world.level.block.entity;

import javax.annotation.Nullable;
import mods.railcraft.api.signals.IControllerProvider;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SimpleSignalController;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class SignalControllerBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements IControllerProvider {

  private final SimpleSignalController controller = new SimpleSignalController("nothing", this);
  public SignalAspect defaultAspect = SignalAspect.GREEN;
  public SignalAspect poweredAspect = SignalAspect.RED;
  private boolean redstoneSignal;

  private boolean loaded;

  public SignalControllerBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_CONTROLLER_BOX.get());
  }

  @Override
  public void tick() {
    super.tick();

    if (this.level.isClientSide()) {
      this.controller.tickClient();
      return;
    }

    // Can't use onLoad as it causes a deadlock for some reason...
    if (!this.loaded) {
      this.loaded = true;
      this.updateRedstoneState();
    }

    this.controller.tickServer();
    SignalAspect prevAspect = this.controller.getAspect();
    if (this.controller.isLinking()) {
      this.controller.setAspect(SignalAspect.BLINK_YELLOW);
    } else if (this.controller.isPaired())
      this.controller.setAspect(determineAspect());
    else
      this.controller.setAspect(SignalAspect.BLINK_RED);
    if (prevAspect != this.controller.getAspect())
      sendUpdateToClient();
  }

  @Override
  public void neighborChanged(BlockState state, Block neighborBlock, BlockPos pos) {
    super.neighborChanged(state, neighborBlock, pos);
    if (this.level.isClientSide())
      return;
    this.updateRedstoneState();
  }

  private void updateRedstoneState() {
    boolean redstoneSignal = this.level.hasNeighborSignal(this.getBlockPos());
    if (redstoneSignal != this.redstoneSignal) {
      this.redstoneSignal = redstoneSignal;
      this.sendUpdateToClient();
    }
  }

  private SignalAspect determineAspect() {
    SignalAspect newAspect = this.redstoneSignal ? this.poweredAspect : this.defaultAspect;
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      TileEntity blockEntity = this.adjacentCache.getTileOnSide(direction);
      if (blockEntity instanceof AbstractSignalBoxBlockEntity) {
        AbstractSignalBoxBlockEntity signalBox = (AbstractSignalBoxBlockEntity) blockEntity;
        if (signalBox.canTransferAspect()) {
          newAspect = SignalAspect.mostRestrictive(newAspect,
              signalBox.getBoxSignalAspect(direction.getOpposite()));
        }
      }
    }
    return newAspect;
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putBoolean("redstoneSignal", this.redstoneSignal);

    this.defaultAspect.writeToNBT(data, "defaultAspect");
    this.poweredAspect.writeToNBT(data, "poweredAspect");

    this.controller.writeToNBT(data);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.redstoneSignal = data.getBoolean("redstoneSignal");

    this.defaultAspect = SignalAspect.readFromNBT(data, "defaultAspect");
    this.poweredAspect = SignalAspect.readFromNBT(data, "poweredAspect");

    this.controller.readFromNBT(data);
  }

  @Override
  public void writePacketData(PacketBuffer data) {
    super.writePacketData(data);

    data.writeVarInt(this.defaultAspect.getId());
    data.writeVarInt(this.poweredAspect.getId());

    this.controller.writePacketData(data);
  }

  @Override
  public void readPacketData(PacketBuffer data) {
    super.readPacketData(data);

    this.defaultAspect = SignalAspect.byId(data.readVarInt());
    this.poweredAspect = SignalAspect.byId(data.readVarInt());

    this.controller.readPacketData(data);
  }

  @Override
  public SignalAspect getBoxSignalAspect(@Nullable Direction side) {
    return this.controller.getAspect();
  }

  @Override
  public SimpleSignalController getController() {
    return this.controller;
  }

  @Override
  public boolean canReceiveAspect() {
    return true;
  }
}
