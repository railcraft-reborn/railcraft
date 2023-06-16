package mods.railcraft.client.gui.screen;

import mods.railcraft.Translations;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetEmbarkingTrackAttributesMessage;
import mods.railcraft.world.level.block.track.outfitted.EmbarkingTrackBlock;
import net.minecraft.client.gui.GuiGraphics;
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
    this.decrementButton = this.addRenderableWidget(Button
        .builder(Component.literal("-1"), __ -> this.setRadius(this.radius - 1))
        .bounds(centreX + 53, centreY + 50, 30, 20)
        .build());
    this.incrementButton = this.addRenderableWidget(Button
        .builder(Component.literal("+1"), __ -> this.setRadius(this.radius + 1))
        .bounds(centreX + 93, centreY + 50, 30, 20)
        .build());
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
  protected void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    this.drawCenteredString(guiGraphics,
        Component.translatable(Translations.Screen.EMBARKING_TRACK_RADIUS, this.radius),
        this.windowWidth / 2, 25);
  }
}
