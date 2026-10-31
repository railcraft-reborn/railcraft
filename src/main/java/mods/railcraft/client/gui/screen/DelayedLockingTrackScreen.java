package mods.railcraft.client.gui.screen;

import mods.railcraft.Translations;
import mods.railcraft.client.util.GuiUtil;
import mods.railcraft.network.to_server.SetDelayedLockingTrackMessage;
import mods.railcraft.world.level.block.entity.track.DelayedLockingTrackBlockEntity;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;

public class DelayedLockingTrackScreen extends IngameWindowScreen {

  private static final int SECOND = SharedConstants.TICKS_PER_SECOND;

  private final DelayedLockingTrackBlockEntity track;
  private Button minus10Button, minus1Button, plus1Button, plus10Button;

  public DelayedLockingTrackScreen(DelayedLockingTrackBlockEntity track) {
    super(track.getBlockState().getBlock().getName());
    this.track = track;
  }

  @Override
  public void init() {
    int centredX = (this.width - this.windowWidth) / 2;
    int centredY = (this.height - this.windowHeight) / 2;
    this.minus10Button = this.addRenderableWidget(Button
        .builder(Component.literal("-10"), __ -> this.incrementDelay(-10 * SECOND))
        .bounds(centredX + 13, centredY + 50, 30, 20)
        .build());
    this.minus1Button = this.addRenderableWidget(Button
        .builder(Component.literal("-1"), __ -> this.incrementDelay(-SECOND))
        .bounds(centredX + 53, centredY + 50, 30, 20)
        .build());
    this.plus1Button = this.addRenderableWidget(Button
        .builder(Component.literal("+1"), __ -> this.incrementDelay(SECOND))
        .bounds(centredX + 93, centredY + 50, 30, 20)
        .build());
    this.plus10Button = this.addRenderableWidget(Button
        .builder(Component.literal("+10"), __ -> this.incrementDelay(10 * SECOND))
        .bounds(centredX + 133, centredY + 50, 30, 20)
        .build());
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    var delay = this.track.getDelay();
    this.minus10Button.active = delay > DelayedLockingTrackBlockEntity.MIN_DELAY;
    this.minus1Button.active = delay > DelayedLockingTrackBlockEntity.MIN_DELAY;
    this.plus1Button.active = delay < DelayedLockingTrackBlockEntity.MAX_DELAY;
    this.plus10Button.active = delay < DelayedLockingTrackBlockEntity.MAX_DELAY;
    super.render(guiGraphics, mouseX, mouseY, partialTicks);
  }

  @Override
  protected void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY,
      float partialTicks) {
    var delay = Component.translatable(Translations.Screen.DELAYED_LOCKING_TRACK_DELAY,
        this.track.getDelay() / SECOND);
    GuiUtil.drawCenteredString(guiGraphics, this.font, delay, this.windowWidth, 25);
  }

  private void incrementDelay(int amount) {
    var delay = Mth.clamp(this.track.getDelay() + amount,
        DelayedLockingTrackBlockEntity.MIN_DELAY, DelayedLockingTrackBlockEntity.MAX_DELAY);
    if (this.track.getDelay() != delay) {
      this.track.setDelay(delay);
      PacketDistributor.sendToServer(
          new SetDelayedLockingTrackMessage(this.track.getBlockPos(), delay));
    }
  }
}
