package mods.railcraft.api.signal;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import mods.railcraft.api.core.Syncable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.INBTSerializable;

public class SingleSignalReceiver
    implements SignalReceiver, INBTSerializable<CompoundTag>, Syncable {

  private static final Logger logger = LogManager.getLogger();

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
  public void removed() {
    this.primarySignalClient.unlink();
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag tag = new CompoundTag();
    tag.put("primarySignalClient", this.primarySignalClient.serializeNBT());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    this.primarySignalClient.deserializeNBT(tag.getCompound("primarySignalClient"));
  }

  @Override
  public void writeSyncData(FriendlyByteBuf data) {
    data.writeEnum(this.primarySignalClient.getSignalAspect());
  }

  @Override
  public void readSyncData(FriendlyByteBuf data) {
    this.primarySignalClient.setSignalAspect(data.readEnum(SignalAspect.class));
  }

  @Override
  public void syncToClient() {
    this.syncListener.run();
  }

  protected class SignalClient implements INBTSerializable<CompoundTag> {

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
        logger.warn("Signal receiver @ [{}] is already linked to signal controller @ [{}]",
            SingleSignalReceiver.this.blockEntity.getBlockPos(), signalController.getBlockPos());
        return;
      }
      if (lastSignalController != null) {
        lastSignalController.removePeer(SingleSignalReceiver.this.blockEntity.getBlockPos());
      }
      this.signalControllerPos = signalController.getBlockPos();
      this.setSignalAspect(signalController.getSignalAspect());
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
      if (blockEntity instanceof SignalControllerProvider provider) {
        return provider.getSignalController();
      } else {
        this.signalControllerPos = null;
        return null;
      }
    }

    protected void refresh() {
      var signalController = this.getSignalController();
      if (signalController != null) {
        this.setSignalAspect(signalController.getSignalAspect());
      }
    }

    protected void unlink() {
      var signalController = this.getSignalController();
      if (signalController != null) {
        signalController.removePeer(SingleSignalReceiver.this.blockEntity.getBlockPos());
      }
    }

    @Override
    public CompoundTag serializeNBT() {
      var tag = new CompoundTag();
      if (this.signalControllerPos != null) {
        tag.put("signalControllerPos", NbtUtils.writeBlockPos(this.signalControllerPos));
      }
      return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
      if (tag.contains("signalControllerPos", Tag.TAG_COMPOUND)) {
        this.signalControllerPos = NbtUtils.readBlockPos(tag.getCompound("signalControllerPos"));
      }
    }
  }
}
