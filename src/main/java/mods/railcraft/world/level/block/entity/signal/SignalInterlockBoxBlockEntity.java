package mods.railcraft.world.level.block.entity.signal;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.TreeSet;
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
      this::syncToClient, null);

  private Interlock interlock = new Interlock(this);

  private SignalAspect neighborAspect = SignalAspect.RED;

  public SignalInterlockBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_INTERLOCK_BOX.get());
  }

  @Override
  public void load() {
    if (!this.level.isClientSide()) {
      this.signalController.refresh();
      this.signalReceiver.refresh();
    }
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    this.signalController.removed();
    this.signalReceiver.removed();
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      this.signalController.spawnTuningAuraParticles();
      return;
    }

    this.neighborAspect = this.getNeighborAspect();
    this.mergeInterlocks();
    this.interlock.tick();
    this.signalController.setSignalAspect(this.determineInterlockAspect());
  }

  private void mergeInterlocks() {
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      TileEntity tile = this.level.getBlockEntity(this.getBlockPos().relative(direction));
      if (tile instanceof SignalInterlockBoxBlockEntity) {
        SignalInterlockBoxBlockEntity box = (SignalInterlockBoxBlockEntity) tile;
        if (box.interlock != interlock) {
          box.interlock.transfer(interlock);
          return;
        }
      }
    }
  }

  private SignalAspect getNeighborAspect() {
    SignalAspect newAspect = SignalAspect.GREEN;
    for (Direction direction : Direction.values()) {
      TileEntity blockEntity = this.level.getBlockEntity(this.getBlockPos().relative(direction));
      if (blockEntity instanceof AbstractSignalBoxBlockEntity) {
        AbstractSignalBoxBlockEntity signalBox = (AbstractSignalBoxBlockEntity) blockEntity;
        if (SignalBoxBlock.isAspectEmitter(signalBox.getBlockState()))
          newAspect = SignalAspect.mostRestrictive(newAspect,
              signalBox.getSignalAspect(direction.getOpposite()));
      }
    }
    return newAspect;
  }

  private SignalAspect determineInterlockAspect() {
    if (this.signalReceiver.getPrimarySignalAspect().ordinal() <= SignalAspect.YELLOW.ordinal()) {
      this.interlock.requestLock();
    } else {
      this.interlock.discardLock();
    }
    return this.interlock.getAspect(this.signalReceiver.getPrimarySignalAspect());
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

  private static class TileComparator implements Comparator<SignalInterlockBoxBlockEntity> {

    public static final TileComparator INSTANCE = new TileComparator();

    @Override
    public int compare(SignalInterlockBoxBlockEntity o1, SignalInterlockBoxBlockEntity o2) {
      if (o1.getX() != o2.getX())
        return o1.getX() - o2.getX();
      if (o1.getZ() != o2.getZ())
        return o1.getZ() - o2.getZ();
      if (o1.getY() != o2.getY())
        return o1.getY() - o2.getY();
      return 0;
    }
  }

  private class Interlock {

    private static final int DELAY = 20 * 10;
    private final NavigableSet<SignalInterlockBoxBlockEntity> peers =
        new TreeSet<>(TileComparator.INSTANCE);
    private final NavigableSet<SignalInterlockBoxBlockEntity> lockRequests =
        new TreeSet<>(TileComparator.INSTANCE);
    @Nullable
    private SignalInterlockBoxBlockEntity active;
    private int delay;

    public Interlock(SignalInterlockBoxBlockEntity blockEntity) {
      this.peers.add(blockEntity);
    }

    private void transfer(Interlock interlock) {
      this.peers.addAll(interlock.peers);
      for (SignalInterlockBoxBlockEntity box : this.peers) {
        box.interlock = this;
      }
    }

    private void tick() {
      this.peers.removeIf(TileEntity::isRemoved);
      if (this.delay < DELAY) {
        this.delay++;
        return;
      }
      if (this.active != null && this.active.isRemoved())
        this.active = null;
      if (this.active == null && !this.lockRequests.isEmpty()
          && this.peers.first() == this.getHost()) {
        this.active = this.lockRequests.last();
        this.lockRequests.clear();
      }
    }

    private void discardLock() {
      if (this.isHostActive()) {
        this.active = null;
      }
    }

    private void requestLock() {
      this.lockRequests.add(this.getHost());
    }

    private SignalAspect getAspect(SignalAspect requestedAspect) {
      if (this.isHostActive()) {
        SignalAspect overrideAspect = SignalAspect.GREEN;
        for (SignalInterlockBoxBlockEntity box : this.peers) {
          overrideAspect = SignalAspect.mostRestrictive(overrideAspect, box.neighborAspect);
        }
        return SignalAspect.mostRestrictive(overrideAspect, requestedAspect);
      }
      return SignalAspect.RED;
    }

    private boolean isHostActive() {
      return this.getHost() == this.active;
    }

    private SignalInterlockBoxBlockEntity getHost() {
      return SignalInterlockBoxBlockEntity.this;
    }
  }
}
