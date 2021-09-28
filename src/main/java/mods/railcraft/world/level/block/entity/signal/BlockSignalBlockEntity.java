package mods.railcraft.world.level.block.entity.signal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import mods.railcraft.api.signals.BlockSignal;
import mods.railcraft.api.signals.BlockSignalNetwork;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SignalController;
import mods.railcraft.api.signals.SignalTools;
import mods.railcraft.api.signals.SimpleBlockSignal;
import mods.railcraft.api.signals.SimpleSignalControllerNetwork;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;

public class BlockSignalBlockEntity extends AbstractSignalBlockEntity
    implements SignalController, BlockSignal {

  private static final Logger logger = LogManager.getLogger();

  private final SimpleSignalControllerNetwork signalControllerNetwork =
      new SimpleSignalControllerNetwork(this, this::syncToClient);
  private final BlockSignalNetwork signalNetwork = new SimpleBlockSignal(this, this::syncToClient);

  public BlockSignalBlockEntity() {
    this(RailcraftBlockEntityTypes.SIGNAL.get());
  }

  public BlockSignalBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      this.signalControllerNetwork.tickClient();
      this.signalNetwork.tickClient();
      return;
    }
    this.signalControllerNetwork.tickServer();
    this.signalNetwork.tickServer();
    SignalAspect lastAspect = this.signalControllerNetwork.getAspect();
    if (this.signalControllerNetwork.isLinking()) {
      this.signalControllerNetwork.setAspect(SignalAspect.BLINK_YELLOW);
    } else {
      this.signalControllerNetwork.setAspect(this.signalNetwork.getSignalAspect());
    }

    if (lastAspect != this.signalControllerNetwork.getAspect()) {
      this.syncToClient();
    }
    if (SignalTools.printSignalDebug && lastAspect != SignalAspect.BLINK_RED
        && this.signalControllerNetwork.getAspect() == SignalAspect.BLINK_RED) {
      logger.info("Signal block changed aspect to BLINK_RED: source:[{}]", this.getBlockPos());
    }
  }

  @Override
  public SignalAspect getSignalAspect() {
    return this.signalControllerNetwork.getAspect();
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.put("signalNetwork", this.signalNetwork.serializeNBT());
    data.put("signalControllerNetwork", this.signalControllerNetwork.serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.signalNetwork.deserializeNBT(data.getCompound("signalNetwork"));
    this.signalControllerNetwork.deserializeNBT(data.getCompound("signalControllerNetwork"));
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    this.signalControllerNetwork.writeSyncData(data);
    this.signalNetwork.writeSyncData(data);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.signalControllerNetwork.readSyncData(data);
    this.signalNetwork.readSyncData(data);
  }

  @Override
  public SimpleSignalControllerNetwork getSignalControllerNetwork() {
    return this.signalControllerNetwork;
  }

  @Override
  public BlockSignalNetwork getSignalNetwork() {
    return this.signalNetwork;
  }

  @Override
  public ITextComponent getPrimaryNetworkName() {
    return this.signalNetwork.getName();
  }
}
