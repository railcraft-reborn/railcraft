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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SignalInterlockBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements SignalControllerProvider, SignalReceiverProvider {

  private final SimpleSignalController signalController =
      new SimpleSignalController(1, this::syncToClient, this, true);

  private final SingleSignalReceiver signalReceiver = new SingleSignalReceiver(this,
      this::syncToClient, this::signalAspectChanged);

  private InterlockController interlockController;

  private SignalAspect neighborSignalAspect;

  public SignalInterlockBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.SIGNAL_INTERLOCK_BOX.get(), blockPos, blockState);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (!this.level.isClientSide()) {
      this.findOrCreateInterlockController();
      this.signalReceiver.refresh();
      this.signalController.refresh();
    }
  }

  @Override
  public void blockRemoved() {
    super.blockRemoved();
    this.signalController.destroy();
    this.signalReceiver.destroy();
    if (this.interlockController != null) {
      this.interlockController.remove(this);
    }
  }

  @Override
  public void neighborSignalBoxChanged(AbstractSignalBoxBlockEntity neighborSignalBox,
      Direction neighborDirection, boolean removed) {
    this.findOrCreateInterlockController();
    this.neighborSignalAspect = SignalAspect.GREEN;
    for (var direction : Direction.values()) {
      var blockEntity = this.level.getBlockEntity(this.getBlockPos().relative(direction));
      if (blockEntity instanceof AbstractSignalBoxBlockEntity signalBox
          && SignalBoxBlock.isAspectEmitter(signalBox.getBlockState())) {
        this.neighborSignalAspect = SignalAspect.mostRestrictive(this.neighborSignalAspect,
            signalBox.getSignalAspect(direction.getOpposite()));
      }
    }
  }

  private void findOrCreateInterlockController() {
    if (this.level.isClientSide()) {
      return;
    }

    var lastInterlockController = this.interlockController;
    this.interlockController = null;

    for (var direction : Direction.values()) {
      var blockEntity =
          this.level.getBlockEntity(this.getBlockPos().relative(direction));
      if (blockEntity instanceof SignalInterlockBoxBlockEntity signalBox
          && signalBox.interlockController != null) {
        this.interlockController = signalBox.interlockController;
        break;
      }
    }

    if (this.interlockController == null) {
      this.interlockController = new InterlockController();
    }

    if (this.interlockController != lastInterlockController) {
      this.interlockController.add(this);
      this.refreshSignalAspect();
    }
  }

  private void refreshSignalAspect() {
    this.signalAspectChanged(this.getRequestedSignalAspect());
  }

  private void signalAspectChanged(SignalAspect signalAspect) {
    if (this.interlockController != null) {
      if (isLockableSignalAspect(signalAspect)) {
        this.interlockController.requestLock(this);
      } else {
        this.interlockController.discardLock(this);
      }
    }
  }

  private SignalAspect getRequestedSignalAspect() {
    return this.signalReceiver.getPrimarySignalAspect();
  }

  @Override
  public SignalAspect getSignalAspect(Direction direction) {
    return this.signalController.getSignalAspect();
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("signalController", this.signalController.serializeNBT());
    tag.put("signalReceiver", this.signalReceiver.serializeNBT());
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.signalController.deserializeNBT(tag.getCompound("signalController"));
    this.signalReceiver.deserializeNBT(tag.getCompound("signalReceiver"));
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    this.signalController.writeToBuf(data);
    this.signalReceiver.writeToBuf(data);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.signalController.readFromBuf(data);
    this.signalReceiver.readFromBuf(data);
  }

  @Override
  public SimpleSignalController getSignalController() {
    return this.signalController;
  }

  @Override
  public SignalReceiver getSignalReceiver() {
    return this.signalReceiver;
  }

  private static boolean isLockableSignalAspect(SignalAspect signalAspect) {
    return signalAspect.ordinal() <= SignalAspect.YELLOW.ordinal();
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
        this.updateSignalAspect(signalBox);
        this.lockRequests.remove(signalBox);
      }
    }

    private void requestLock(SignalInterlockBoxBlockEntity signalBox) {
      if (this.activeSignalBox != signalBox) {
        this.lockRequests.add(signalBox);
        if (this.activeSignalBox == null) {
          this.next();
        } else {
          this.updateSignalAspect(signalBox);
        }
      }
    }

    private void next() {
      do {
        this.activeSignalBox = this.lockRequests.poll();
      } while (this.activeSignalBox != null
          && !isLockableSignalAspect(this.activeSignalBox.getRequestedSignalAspect()));
      this.peers.forEach(this::updateSignalAspect);
    }

    private void updateSignalAspect(SignalInterlockBoxBlockEntity signalBox) {
      if (this.isActive(signalBox)) {
        var signalAspect = signalBox.signalReceiver.getPrimarySignalAspect();
        for (var box : this.peers) {
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

  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState,
      SignalInterlockBoxBlockEntity blockEntity) {
    blockEntity.signalController.spawnTuningAuraParticles();
  }
}
