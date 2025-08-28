package mods.railcraft.world.module;

import mods.railcraft.api.core.CompoundTagKeys;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;

public abstract class CrafterModule<T extends ModuleProvider> extends ContainerModule<T> {

  protected static final int PROGRESS_STEP = 16;

  protected int progress;
  protected int duration;
  private boolean processing;
  protected boolean paused;
  private long finishedAt;

  private int progressStepTicks;

  protected CrafterModule(T provider, int size) {
    super(provider, size);
  }

  @Override
  public void serverTick() {
    if (this.progressStepTicks++ >= PROGRESS_STEP) {
      this.progressStepTicks = 0;
      this.progressCrafting();
    }
  }

  public final int getProgress() {
    return this.progress;
  }

  public final void setProgress(int progress) {
    this.progress = progress;
  }

  protected void reset() {
    // this.setDuration(0);
    this.setProgress(0);
    this.setProcessing(false);
    this.provider.syncToClient();
  }

  public final boolean isProcessing() {
    return this.processing;
  }

  protected final void setProcessing(boolean processing) {
    if (this.processing != processing) {
      this.processing = processing;
      this.provider.syncToClient();
    }
  }

  public final void setDuration(int duration) {
    this.duration = duration;
  }

  public final int getDuration() {
    return this.duration;
  }

  protected abstract int calculateDuration();

  protected final void setFinished() {
    this.finishedAt = this.provider.level().getGameTime();
  }

  protected final boolean isFinished() {
    return this.processing
        && this.provider.level().getGameTime() > this.finishedAt + PROGRESS_STEP + 5;
  }

  public final float getProgressPercent() {
    if (this.getProgress() == 0 || this.getDuration() == 0) {
      return 0;
    }
    return (float) this.getProgress() / this.getDuration();
  }

  protected void setupCrafting() {}

  protected boolean lacksRequirements() {
    return false;
  }

  protected boolean doProcessStep() {
    return true;
  }

  protected final void progressCrafting() {
    if (this.isFinished()) {
      this.setProcessing(false);
    }
    if (this.paused) {
      return;
    }

    this.setupCrafting();

    if (this.lacksRequirements()) {
      this.reset();
      return;
    }

    this.setProcessing(true);
    if (this.doProcessStep()) {
      this.progress += PROGRESS_STEP;
      this.duration = this.calculateDuration();
      if (this.progress < this.duration) {
        return;
      }

      this.progress = this.duration;
      this.setFinished();
      if (this.craftAndPush()) {
        this.reset();
      }
    }
  }

  protected abstract boolean craftAndPush();

  @Override
  public CompoundTag serializeNBT(HolderLookup.Provider provider) {
    var tag = super.serializeNBT(provider);
    tag.putInt(CompoundTagKeys.PROGRESS, this.progress);
    tag.putBoolean(CompoundTagKeys.PROCESSING, this.processing);
    return tag;
  }

  @Override
  public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
    super.deserializeNBT(provider, tag);
    this.progress = tag.getInt(CompoundTagKeys.PROGRESS).orElse(0);
    this.processing = tag.getBoolean(CompoundTagKeys.PROCESSING).orElse(false);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf out) {
    super.writeToBuf(out);
    out.writeBoolean(this.processing);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf in) {
    super.readFromBuf(in);
    this.processing = in.readBoolean();
  }
}
