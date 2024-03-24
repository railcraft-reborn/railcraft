package mods.railcraft.client.gui.screen;

import mods.railcraft.Translations;
import mods.railcraft.client.util.GuiUtil;
import mods.railcraft.network.PacketHandler;
import mods.railcraft.network.to_server.SetTrainDetectorMessage;
import mods.railcraft.world.level.block.entity.detector.TrainDetectorBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class TrainDetectorScreen extends IngameWindowScreen {

  private final TrainDetectorBlockEntity trainDetector;

  public TrainDetectorScreen(TrainDetectorBlockEntity trainDetector) {
    super(trainDetector.getDisplayName(), LARGE_WINDOW_TEXTURE, 176, 113);
    this.trainDetector = trainDetector;
  }

  @Override
  public void init() {
    var centeredX = (this.width - this.windowWidth) / 2;
    var centeredY = (this.height - this.windowHeight) / 2;
    this.addRenderableWidget(Button
        .builder(Component.literal("-10"), __ -> this.incrementLength(-10))
        .bounds(centeredX + 13, centeredY + 50, 30, 20)
        .build());
    this.addRenderableWidget(Button
        .builder(Component.literal("-1"), __ -> this.incrementLength(-1))
        .bounds(centeredX + 53, centeredY + 50, 30, 20)
        .build());
    this.addRenderableWidget(Button
        .builder(Component.literal("+1"), __ -> this.incrementLength(1))
        .bounds(centeredX + 93, centeredY + 50, 30, 20)
        .build());
    this.addRenderableWidget(Button
        .builder(Component.literal("+10"), __ -> this.incrementLength(10))
        .bounds(centeredX + 133, centeredY + 50, 30, 20)
        .build());
  }

  private void incrementLength(int incrementAmount) {
    var size = Mth.clamp(trainDetector.getTrainSize() + incrementAmount, 1, 100);
    if (this.trainDetector.getTrainSize() != size) {
      this.trainDetector.setTrainSize(size);
      PacketHandler.sendToServer(
          new SetTrainDetectorMessage(this.trainDetector.getBlockPos(),
              this.trainDetector.getTrainSize()));
    }
  }

  @Override
  protected void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY,
      float partialTicks) {
    var componentForce = Component.translatable(Translations.Screen.TRAIN_DETECTOR_SIZE,
        this.trainDetector.getTrainSize());
    GuiUtil.drawCenteredString(guiGraphics, this.font, componentForce, this.windowWidth, 25);
  }
}
