package mods.railcraft.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Translations;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetEmbarkingTrackAttributesMessage;
import mods.railcraft.world.level.block.track.outfitted.EmbarkingTrackBlock;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class EmbarkingTrackScreen extends IngameWindowScreen {

  private final BlockPos blockPos;
  private int radius;

  private Button incrementButton;
  private Button decrementButton;

  public EmbarkingTrackScreen(BlockState blockState, BlockPos blockPos) {
    super(blockState.getBlock().getName());
    this.blockPos = blockPos;
    this.radius = EmbarkingTrackBlock.getRadius(blockState);
  }

  @Override
  protected void init() {
    super.init();
    int centreX = (this.width - this.windowWidth) / 2;
    int centreY = (this.height - this.windowHeight) / 2;
    this.addRenderableWidget(this.decrementButton =
        new Button(centreX + 53, centreY + 50, 30, 20, Component.literal("-1"),
            __ -> this.setRadius(this.radius - 1)));
    this.addRenderableWidget(this.incrementButton =
        new Button(centreX + 93, centreY + 50, 30, 20, Component.literal("+1"),
            __ -> this.setRadius(this.radius + 1)));
    this.updateButtons();
  }

  private void setRadius(int radius) {
    this.radius = radius;
    this.updateButtons();
    NetworkChannel.GAME.sendToServer(
        new SetEmbarkingTrackAttributesMessage(this.blockPos, this.radius));
  }

  private void updateButtons() {
    this.incrementButton.active = this.radius < EmbarkingTrackBlock.MAX_RADIUS;
    this.decrementButton.active = this.radius > EmbarkingTrackBlock.MIN_RADIUS;
  }

  @Override
  protected void renderContent(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
    this.drawCenteredString(poseStack,
        Component.translatable(Translations.Screen.EMBARKING_TRACK_RADIUS, this.radius),
        this.windowWidth / 2, 25);
  }
}
