package mods.railcraft.world.level.block.entity.signal;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import javax.annotation.Nullable;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalControllerProvider;
import mods.railcraft.api.signal.SignalReceiver;
import mods.railcraft.api.signal.SignalReceiverProvider;
import mods.railcraft.api.signal.SimpleSignalController;
import mods.railcraft.api.signal.SingleSignalReceiver;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.signal.SignalBoxBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public class SignalInterlockBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements SignalControllerProvider, SignalReceiverProvider {

  private final SimpleSignalController signalController =
      new SimpleSignalController(1, this::syncToClient, this, true);

  private final SingleSignalReceiver signalReceiver = new SingleSignalReceiver(this,
      this::syncToClient, this::signalAspectChanged);

  private InterlockController interlockController;

  private SignalAspect neighborSignalAspect;

  public SignalInterlockBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_INTERLOCK_BOX.get());
  }

  @Override
  public void load() {
    if (!this.level.isClientSide()) {
      if (this.interlockController == null) {
        for (Direction direction : Direction.values()) {
          TileEntity blockEntity =
              this.level.getBlockEntity(this.getBlockPos().relative(direction));
          if (blockEntity instanceof SignalInterlockBoxBlockEntity) {
            SignalInterlockBoxBlockEntity signalBox = (SignalInterlockBoxBlockEntity) blockEntity;
            if (signalBox.interlockController != null) {
              this.interlockController = signalBox.interlockController;
              break;
            }
          }
        }
        if (this.interlockController == null) {
          this.interlockController = new InterlockController();
        }
        this.interlockController.add(this);
      }
      this.signalReceiver.refresh();
      this.signalController.refresh();
    }
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    this.signalController.removed();
    this.signalReceiver.removed();
    if (this.interlockController != null) {
      this.interlockController.remove(this);
    }
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      this.signalController.spawnTuningAuraParticles();
      return;
    }
  }

  @Override
  public void neighborSignalBoxChanged(AbstractSignalBoxBlockEntity neighborSignalBox,
      Direction neighborDirection, boolean removed) {
    this.neighborSignalAspect = SignalAspect.GREEN;
    for (Direction direction : Direction.values()) {
      TileEntity blockEntity = this.level.getBlockEntity(this.getBlockPos().relative(direction));
      if (blockEntity instanceof AbstractSignalBoxBlockEntity) {
        AbstractSignalBoxBlockEntity signalBox = (AbstractSignalBoxBlockEntity) blockEntity;
        if (SignalBoxBlock.isAspectEmitter(signalBox.getBlockState()))
          this.neighborSignalAspect = SignalAspect.mostRestrictive(this.neighborSignalAspect,
              signalBox.getSignalAspect(direction.getOpposite()));
      }
    }
  }

  private void signalAspectChanged(SignalAspect signalAspect) {
    if (this.interlockController != null) {
      if (signalAspect.ordinal() <= SignalAspect.YELLOW.ordinal()) {
        this.interlockController.requestLock(this);
      } else {
        this.interlockController.discardLock(this);
      }
    }
  }

  @Override
  public SignalAspect getSignalAspect(Direction direction) {
    return this.signalController.getSignalAspect();
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.put("signalController", this.signalController.serializeNBT());
    data.put("signalReceiver", this.signalReceiver.serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.signalController.deserializeNBT(data.getCompound("signalController"));
    this.signalReceiver.deserializeNBT(data.getCompound("signalReceiver"));
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    this.signalController.writeSyncData(data);
    this.signalReceiver.writeSyncData(data);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.signalController.readSyncData(data);
    this.signalReceiver.readSyncData(data);
  }

  @Override
  public SimpleSignalController getSignalController() {
    return this.signalController;
  }

  @Override
  public SignalReceiver getSignalReceiver() {
    return this.signalReceiver;
  }

  private static class InterlockController {

    private final Set<SignalInterlockBoxBlockEntity> peers = new HashSet<>();
    private final Queue<SignalInterlockBoxBlockEntity> lockRequests = new ArrayDeque<>();
    @Nullable
    private SignalInterlockBoxBlockEntity activeSignalBox;

    private void add(SignalInterlockBoxBlockEntity signalBox) {
      this.peers.add(signalBox);
      this.updateSignalAspect(signalBox);
    }

    private void remove(SignalInterlockBoxBlockEntity signalBox) {
      this.peers.remove(signalBox);
      this.discardLock(signalBox);
    }

    private void discardLock(SignalInterlockBoxBlockEntity signalBox) {
      if (this.isActive(signalBox)) {
        this.next();
      } else {
        this.lockRequests.remove(signalBox);
      }
    }

    private void requestLock(SignalInterlockBoxBlockEntity signalBox) {
      this.lockRequests.add(signalBox);
      if (this.activeSignalBox == null) {
        this.next();
      }
    }

    private void next() {
      this.activeSignalBox = this.lockRequests.poll();
      this.peers.forEach(this::updateSignalAspect);
    }

    private void updateSignalAspect(SignalInterlockBoxBlockEntity signalBox) {
      if (this.isActive(signalBox)) {
        SignalAspect signalAspect = signalBox.signalReceiver.getPrimarySignalAspect();
        for (SignalInterlockBoxBlockEntity box : this.peers) {
          signalAspect = SignalAspect.mostRestrictive(signalAspect, box.neighborSignalAspect);
        }
        signalBox.signalController.setSignalAspect(signalAspect);
        return;
      }
      signalBox.signalController.setSignalAspect(SignalAspect.RED);
    }

    private boolean isActive(SignalInterlockBoxBlockEntity signalBox) {
      return signalBox == this.activeSignalBox;
    }
  }
}
