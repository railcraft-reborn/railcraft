package mods.railcraft.api.signal;

import java.util.function.Consumer;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DualSignalReceiver extends SingleSignalReceiver {

  private static final Logger logger = LogUtils.getLogger();

  private final SignalClient secondarySignalClient;

  public DualSignalReceiver(BlockEntity blockEntity,
      Runnable syncListener, Consumer<SignalAspect> primarySignalAspectListener,
      Consumer<SignalAspect> secondarySignalAspectListener) {
    super(blockEntity, syncListener, primarySignalAspectListener);
    this.secondarySignalClient = new SignalClient(secondarySignalAspectListener);
  }

  public SignalAspect getSecondarySignalAspect() {
    return this.secondarySignalClient.getSignalAspect();
  }

  @Override
  public void linked(SignalController signalController) {
    if (this.primarySignalClient.getSignalController() == null) {
      super.linked(signalController);
      return;
    } else if (this.secondarySignalClient.getSignalController() == null) {
      this.secondarySignalClient.linked(signalController);
      return;
    }
    this.primarySignalClient.unlinked();
    this.secondarySignalClient.unlinked();
    super.linked(signalController);
  }

  @Override
  public void unlinked(SignalController signalController) {
    if (this.primarySignalClient.getSignalControllerBlockPos()
        .equals(signalController.blockPos())) {
      this.primarySignalClient.unlinked();
    } else if (this.secondarySignalClient.getSignalControllerBlockPos()
        .equals(signalController.blockPos())) {
      this.secondarySignalClient.unlinked();
    } else {
      logger.warn(
          "Signal controller @ [{}] tried to unlink with signal receiver @ [{}] without initially being linked",
          signalController.blockPos(), this.blockEntity.getBlockPos());
    }
  }

  @Override
  public void refresh() {
    super.refresh();
    this.secondarySignalClient.refresh();
  }

  @Override
  public void destroy() {
    super.destroy();
    this.secondarySignalClient.unlink();
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag tag = super.serializeNBT();
    tag.put("secondarySignalClient", this.secondarySignalClient.serializeNBT());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    super.deserializeNBT(tag);
    this.secondarySignalClient.deserializeNBT(tag.getCompound("secondarySignalClient"));
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeEnum(this.secondarySignalClient.getSignalAspect());
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.secondarySignalClient.setSignalAspect(data.readEnum(SignalAspect.class));
  }
}
