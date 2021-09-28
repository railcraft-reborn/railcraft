package mods.railcraft.world.level.block.entity.signal;

import static net.minecraft.util.Direction.EAST;
import static net.minecraft.util.Direction.NORTH;
import static net.minecraft.util.Direction.SOUTH;
import static net.minecraft.util.Direction.WEST;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import javax.annotation.Nullable;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SignalController;
import mods.railcraft.api.signals.SignalControllerNetwork;
import mods.railcraft.api.signals.SignalReceiver;
import mods.railcraft.api.signals.SimpleSignalControllerNetwork;
import mods.railcraft.api.signals.SimpleSignalReceiverNetwork;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class SignalInterlockBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements SignalController, SignalReceiver, IAspectProvider {

  private static final Direction[] SIDES = {NORTH, WEST, SOUTH, EAST};
  private final SimpleSignalControllerNetwork controllerNetwork =
      new SimpleSignalControllerNetwork(this, this::syncToClient);
  private final SimpleSignalReceiverNetwork receiver =
      new SimpleSignalReceiverNetwork(this, this::syncToClient);
  private Interlock interlock = new Interlock(this);
  private SignalAspect overrideAspect = SignalAspect.RED;

  public SignalInterlockBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_INTERLOCK_BOX.get());
  }

  @Override
  public void onControllerAspectChange(SignalControllerNetwork con, SignalAspect aspect) {}

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      controllerNetwork.tickClient();
      receiver.tickClient();
      return;
    }

    controllerNetwork.tickServer();
    receiver.tickServer();

    overrideAspect = getOverrideAspect();

    mergeInterlocks();

    interlock.tick(this);

    SignalAspect prevAspect = controllerNetwork.getAspect();
    if (receiver.isLinking() || controllerNetwork.isLinking())
      controllerNetwork.setAspect(SignalAspect.BLINK_YELLOW);
    else if (controllerNetwork.hasPeers() && receiver.hasPeers())
      controllerNetwork.setAspect(determineAspect());
    else
      controllerNetwork.setAspect(SignalAspect.BLINK_RED);

    if (prevAspect != controllerNetwork.getAspect())
      syncToClient();
  }

  private void mergeInterlocks() {
    for (Direction side : SIDES) {
      TileEntity tile = adjacentCache.getTileOnSide(side);
      if (tile instanceof SignalInterlockBoxBlockEntity) {
        SignalInterlockBoxBlockEntity box = (SignalInterlockBoxBlockEntity) tile;
        if (box.interlock != interlock) {
          box.interlock.merge(interlock);
          return;
        }
      }
    }
  }

  private SignalAspect getOverrideAspect() {
    SignalAspect newAspect = SignalAspect.GREEN;
    for (int side = 2; side < 6; side++) {
      Direction forgeSide = Direction.values()[side];
      TileEntity t = adjacentCache.getTileOnSide(forgeSide);
      if (t instanceof AbstractSignalBoxBlockEntity) {
        AbstractSignalBoxBlockEntity tile = (AbstractSignalBoxBlockEntity) t;
        if (tile.canTransferAspect())
          newAspect = SignalAspect.mostRestrictive(newAspect,
              tile.getBoxSignalAspect(forgeSide.getOpposite()));
      }
    }
    return newAspect;
  }

  private SignalAspect determineAspect() {
    interlock.requestLock(this, receiver.getAspect().ordinal() <= SignalAspect.YELLOW.ordinal());
    return interlock.getAspect(this, receiver.getAspect());
  }

  @Override
  public SignalAspect getBoxSignalAspect(@Nullable Direction side) {
    return controllerNetwork.getAspect();
  }


  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.put("controllerNetwork", this.controllerNetwork.serializeNBT());
    data.put("receiverNetwork", this.receiver.serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);

    controllerNetwork.deserializeNBT(data.getCompound("controllerNetwork"));
    receiver.deserializeNBT(data.getCompound("receiverNetwork"));
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    controllerNetwork.writeSyncData(data);
    receiver.writeSyncData(data);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    controllerNetwork.readSyncData(data);
    receiver.readSyncData(data);
  }

  @Override
  public boolean attachesTo(BlockState state, BlockPos pos, Direction side) {
    TileEntity tile = this.adjacentCache.getTileOnSide(side);
    return tile instanceof SignalInterlockBoxBlockEntity || super.attachesTo(state, pos, side);
  }

  @Override
  public boolean canTransferAspect() {
    return false;
  }

  @Override
  public boolean canReceiveAspect() {
    return true;
  }

  @Override
  public SignalControllerNetwork getSignalControllerNetwork() {
    return controllerNetwork;
  }

  @Override
  public SimpleSignalReceiverNetwork getSignalReceiverNetwork() {
    return receiver;
  }

  @Override
  public SignalAspect getTriggerAspect() {
    return getBoxSignalAspect(null);
  }

  @Override
  public List<String> getDebugOutput() {
    List<String> debug = super.getDebugOutput();
    debug.add("Interlock Obj: " + interlock);
    debug.add("Interlock Pool: " + interlock.interlocks);
    debug.add("Lock Requests: " + interlock.lockRequests);
    debug.add("Active: " + interlock.active);
    debug.add("Delay: " + interlock.delay);
    debug.add("In Aspect: " + receiver.getAspect().name());
    debug.add("Out Aspect: " + controllerNetwork.getAspect().name());
    debug.add("Override Aspect: " + overrideAspect.name());
    return debug;
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
    private final NavigableSet<SignalInterlockBoxBlockEntity> interlocks =
        new TreeSet<>(TileComparator.INSTANCE);
    private final NavigableSet<SignalInterlockBoxBlockEntity> lockRequests =
        new TreeSet<>(TileComparator.INSTANCE);
    private @Nullable SignalInterlockBoxBlockEntity active;
    private int delay;

    public Interlock(SignalInterlockBoxBlockEntity tile) {
      interlocks.add(tile);
    }

    void merge(Interlock interlock) {
      interlocks.addAll(interlock.interlocks);
      for (SignalInterlockBoxBlockEntity box : interlocks) {
        box.interlock = this;
      }
    }

    public void tick(SignalInterlockBoxBlockEntity host) {
      interlocks.removeIf(TileEntity::isRemoved);
      if (delay < DELAY) {
        delay++;
        return;
      }
      if (active != null && active.isRemoved())
        active = null;
      if (active == null && !lockRequests.isEmpty() && interlocks.first() == host) {
        active = lockRequests.last();
        lockRequests.clear();
      }
    }

    void requestLock(SignalInterlockBoxBlockEntity host, boolean request) {
      if (request)
        lockRequests.add(host);
      else if (active == host)
        active = null;
    }

    public SignalAspect getAspect(SignalInterlockBoxBlockEntity host,
        SignalAspect requestedAspect) {
      if (host == active) {
        SignalAspect overrideAspect = SignalAspect.GREEN;
        for (SignalInterlockBoxBlockEntity box : interlocks) {
          overrideAspect = SignalAspect.mostRestrictive(overrideAspect, box.overrideAspect);
        }
        return SignalAspect.mostRestrictive(overrideAspect, requestedAspect);
      }
      return SignalAspect.RED;
    }
  }
}
