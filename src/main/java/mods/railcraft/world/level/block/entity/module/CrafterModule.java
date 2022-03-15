package mods.railcraft.world.level.block.entity.module;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public abstract class CrafterModule extends ContainerModule {

  public static final int PROGRESS_STEP = 16;

  protected int progress;
  protected int duration;
  private boolean processing;
  protected boolean paused;
  private long finishedAt;

  private int progressStepTicks;

  protected CrafterModule(ModuleProvider provider, int size) {
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
    return duration;
  }

  protected abstract int calculateDuration();

  protected final void setFinished() {
    this.finishedAt = this.provider.getLevel().getGameTime();
  }

  protected final boolean isFinished() {
    return this.processing
        && this.provider.getLevel().getGameTime() > this.finishedAt + PROGRESS_STEP + 5;
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
  public CompoundTag serializeNBT() {
    var tag = super.serializeNBT();
    tag.putInt("progress", this.progress);
    tag.putBoolean("processing", this.processing);
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    super.deserializeNBT(tag);
    this.progress = tag.getInt("progress");
    this.processing = tag.getBoolean("processing");
  }

  @Override
  public void writeToBuf(FriendlyByteBuf out) {
    out.writeBoolean(this.processing);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf in) {
    this.processing = in.readBoolean();
  }
}
