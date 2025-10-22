package mods.railcraft.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Translations;
import mods.railcraft.client.util.GuiUtil;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetTrainDetectorAttributesMessage;
import mods.railcraft.world.level.block.entity.detector.TrainDetectorBlockEntity;
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
    this.addRenderableWidget(new Button(
        centeredX + 13, centeredY + 50, 30, 20,
        Component.literal("-10"),
        __ -> this.incrementLength(-10)));
    this.addRenderableWidget(new Button(
        centeredX + 53, centeredY + 50, 30, 20,
        Component.literal("-1"),
        __ -> this.incrementLength(-1)));
    this.addRenderableWidget(new Button(
        centeredX + 93, centeredY + 50, 30, 20,
        Component.literal("+1"),
        __ -> this.incrementLength(1)));
    this.addRenderableWidget(new Button(
        centeredX + 133, centeredY + 50, 30, 20,
        Component.literal("+10"),
        __ -> this.incrementLength(10)));
  }

  private void incrementLength(int incrementAmount) {
    var size = Mth.clamp(trainDetector.getTrainSize() + incrementAmount, 1, 100);
    if (this.trainDetector.getTrainSize() != size) {
      this.trainDetector.setTrainSize(size);
      NetworkChannel.GAME.sendToServer(
          new SetTrainDetectorAttributesMessage(this.trainDetector.getBlockPos(),
              this.trainDetector.getTrainSize()));
    }
  }

  @Override
  protected void renderContent(PoseStack poseStack, int mouseX, int mouseY,
                               float partialTicks) {
    var componentForce = Component.translatable(Translations.Screen.TRAIN_DETECTOR_SIZE,
        this.trainDetector.getTrainSize());
    GuiUtil.drawCenteredString(poseStack, this.font, componentForce, this.windowWidth, 25);
  }
}
