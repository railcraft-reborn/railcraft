package mods.railcraft.api.signal;

import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.core.NetworkSerializable;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public class SingleSignalReceiver
    implements SignalReceiver, ValueIOSerializable, NetworkSerializable {

  private static final Logger LOGGER = LogUtils.getLogger();

  protected final BlockEntity blockEntity;
  private final Runnable syncListener;
  protected final SignalClient primarySignalClient;

  public SingleSignalReceiver(BlockEntity blockEntity, Runnable syncListener) {
    this(blockEntity, syncListener, null);
  }

  public SingleSignalReceiver(BlockEntity blockEntity, Runnable syncListener,
      @Nullable Consumer<SignalAspect> primarySignalAspectListener) {
    this.blockEntity = blockEntity;
    this.syncListener = syncListener;
    this.primarySignalClient = new SignalClient(primarySignalAspectListener);
  }

  public SignalAspect getPrimarySignalAspect() {
    return this.primarySignalClient.getSignalAspect();
  }

  @Override
  public void linked(SignalController signalController) {
    this.primarySignalClient.linked(signalController);
  }

  @Override
  public void unlinked(SignalController signalController) {
    this.primarySignalClient.unlinked();
  }

  @Override
  public void receiveSignalAspect(SignalController signalController,
      SignalAspect signalAspect) {
    this.primarySignalClient.setSignalAspect(signalAspect);
  }

  @Override
  public void refresh() {
    this.primarySignalClient.refresh();
  }

  @Override
  public void destroy() {
    this.primarySignalClient.unlink();
  }

  @Override
  public void serialize(ValueOutput valueOutput) {
    valueOutput.putChild(CompoundTagKeys.PRIMARY_SIGNAL_CLIENT, this.primarySignalClient);
  }

  @Override
  public void deserialize(ValueInput valueInput) {
    this.primarySignalClient.deserialize(valueInput.childOrEmpty(CompoundTagKeys.PRIMARY_SIGNAL_CLIENT));
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf data) {
    data.writeEnum(this.primarySignalClient.getSignalAspect());
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf data) {
    this.primarySignalClient.setSignalAspect(data.readEnum(SignalAspect.class));
  }

  public void syncToClient() {
    this.syncListener.run();
  }

  protected class SignalClient implements ValueIOSerializable {

    @Nullable
    private final Consumer<SignalAspect> signalAspectListener;
    @Nullable
    private BlockPos signalControllerPos;
    private SignalAspect signalAspect = SignalAspect.BLINK_RED;

    protected SignalClient(@Nullable Consumer<SignalAspect> signalAspectListener) {
      this.signalAspectListener = signalAspectListener;
    }

    protected void linked(SignalController signalController) {
      var lastSignalController = this.getSignalController();
      if (lastSignalController == signalController) {
        LOGGER.warn("Signal receiver @ [{}] is already linked to signal controller @ [{}]",
            SingleSignalReceiver.this.blockEntity.getBlockPos(), signalController.blockPos());
        return;
      }
      if (lastSignalController != null) {
        lastSignalController.removePeer(SingleSignalReceiver.this.blockEntity.getBlockPos());
      }
      this.signalControllerPos = signalController.blockPos();
      this.setSignalAspect(signalController.aspect());
      SingleSignalReceiver.this.blockEntity.setChanged();
    }

    protected void unlinked() {
      this.signalControllerPos = null;
      this.setSignalAspect(SignalAspect.BLINK_RED);
      SingleSignalReceiver.this.blockEntity.setChanged();
    }

    protected SignalAspect getSignalAspect() {
      return this.signalAspect;
    }

    protected void setSignalAspect(SignalAspect signalAspect) {
      if (this.signalAspect != signalAspect) {
        this.signalAspect = signalAspect;
        SingleSignalReceiver.this.syncToClient();
        if (this.signalAspectListener != null) {
          this.signalAspectListener.accept(signalAspect);
        }
      }
    }

    @Nullable
    protected BlockPos getSignalControllerBlockPos() {
      return this.signalControllerPos;
    }

    @Nullable
    protected SignalController getSignalController() {
      if (this.signalControllerPos == null) {
        return null;
      }
      var blockEntity =
          SingleSignalReceiver.this.blockEntity.getLevel().getBlockEntity(this.signalControllerPos);
      if (blockEntity instanceof SignalControllerEntity provider) {
        return provider.getSignalController();
      } else {
        this.signalControllerPos = null;
        return null;
      }
    }

    protected void refresh() {
      var signalController = this.getSignalController();
      if (signalController != null) {
        this.setSignalAspect(signalController.aspect());
      }
    }

    protected void unlink() {
      var signalController = this.getSignalController();
      if (signalController != null) {
        signalController.removePeer(SingleSignalReceiver.this.blockEntity.getBlockPos());
      }
    }

    @Override
    public void serialize(ValueOutput valueOutput) {
      valueOutput.storeNullable(CompoundTagKeys.SIGNAL_CONTROLLER_POS, BlockPos.CODEC, this.signalControllerPos);
    }

    @Override
    public void deserialize(ValueInput valueInput) {
      this.signalControllerPos =
          valueInput.read(CompoundTagKeys.SIGNAL_CONTROLLER_POS, BlockPos.CODEC).orElse(null);
    }
  }
}
