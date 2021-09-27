package mods.railcraft.world.level.block.entity.signal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import mods.railcraft.api.signals.BlockSignal;
import mods.railcraft.api.signals.IBlockSignal;
import mods.railcraft.api.signals.SignalControllerProvider;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SignalTools;
import mods.railcraft.api.signals.SimpleBlockSignal;
import mods.railcraft.api.signals.SimpleSignalController;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;

public class BlockSignalBlockEntity extends AbstractSignalBlockEntity
    implements SignalControllerProvider, IBlockSignal {

  private static final Logger logger = LogManager.getLogger();

  private final SimpleSignalController controller = new SimpleSignalController("nothing", this);
  private final BlockSignal signalBlock = new SimpleBlockSignal("nothing", this);

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
      controller.tickClient();
      signalBlock.tickClient();
      return;
    }
    controller.tickServer();
    signalBlock.tickServer();
    SignalAspect prevAspect = controller.getAspect();
    if (controller.isLinking()) {
      controller.setAspect(SignalAspect.BLINK_YELLOW);
    } else {
      controller.setAspect(signalBlock.getSignalAspect());
    }

    if (prevAspect != controller.getAspect()) {
      syncToClient();
    }
    if (SignalTools.printSignalDebug && prevAspect != SignalAspect.BLINK_RED
        && controller.getAspect() == SignalAspect.BLINK_RED) {
      logger.info("Signal block changed aspect to BLINK_RED: source:[{}]", this.getBlockPos());
    }
  }

  @Override
  public SignalAspect getSignalAspect() {
    return controller.getAspect();
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    signalBlock.writeToNBT(data);
    controller.writeToNBT(data);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    signalBlock.readFromNBT(data);
    controller.readFromNBT(data);
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    controller.writeSyncData(data);
    signalBlock.writeSyncData(data);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    controller.readSyncData(data);
    signalBlock.readSyncData(data);
  }

  @Override
  public SimpleSignalController getController() {
    return controller;
  }

  @Override
  public BlockSignal getBlockSignal() {
    return signalBlock;
  }
}
