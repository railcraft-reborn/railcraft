package mods.railcraft.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Translations;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetSignalCapacitorBoxAttributesMessage;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class SignalCapacitorBoxScreen extends IngameWindowScreen {

  private final SignalCapacitorBoxBlockEntity signalBox;
  private MultiButton<SignalCapacitorBoxBlockEntity.Mode> modeButton;

  public SignalCapacitorBoxScreen(SignalCapacitorBoxBlockEntity signalBox) {
    super(signalBox.getDisplayName());
    this.signalBox = signalBox;
  }

  @Override
  public void init() {
    int centredX = (this.width - this.windowWidth) / 2;
    int centredY = (this.height - this.windowHeight) / 2;
    this.addRenderableWidget(Button
        .builder(Component.literal("-10"), __ -> this.incrementTicksToPower(-10))
        .bounds(centredX + 13, centredY + 38, 30, 20)
        .build());
    this.addRenderableWidget(Button
        .builder(Component.literal("-1"), __ -> this.incrementTicksToPower(-1))
        .bounds(centredX + 53, centredY + 38, 30, 20)
        .build());
    this.addRenderableWidget(Button
        .builder(Component.literal("+1"), __ -> this.incrementTicksToPower(1))
        .bounds(centredX + 93, centredY + 38, 30, 20)
        .build());
    this.addRenderableWidget(Button
        .builder(Component.literal("+10"), __ -> this.incrementTicksToPower(10))
        .bounds(centredX + 133, centredY + 38, 30, 20)
        .build());
    this.modeButton = this.addRenderableWidget(MultiButton
        .builder(ButtonTexture.SMALL_BUTTON, this.signalBox.getMode())
        .bounds(centredX + 23, centredY + 65, 130, 15)
        .stateCallback(this::setMode)
        .build());

  }

  @Override
  protected void renderContent(PoseStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    this.drawCenteredString(matrixStack,
        Component.translatable(Translations.Screen.SIGNAL_CAPACITOR_BOX_DURATION,
            this.signalBox.getTicksToPower() / 20),
        this.windowWidth / 2, 25);
  }

  @Override
  public void tick() {
    super.tick();
    this.modeButton.setState(this.signalBox.getMode());
  }

  private void setMode(SignalCapacitorBoxBlockEntity.Mode mode) {
    if (this.signalBox.getMode() != mode) {
      this.signalBox.setMode(mode);
      this.sendAttributes();
    }
  }

  private void incrementTicksToPower(int incrementSeconds) {
    short ticksToPower = (short) Math.max(0,
        this.signalBox.getTicksToPower() + (incrementSeconds * SharedConstants.TICKS_PER_SECOND));
    if (this.signalBox.getTicksToPower() != ticksToPower) {
      this.signalBox.setTicksToPower(ticksToPower);
      this.sendAttributes();
    }
  }

  private void sendAttributes() {
    NetworkChannel.GAME.sendToServer(
        new SetSignalCapacitorBoxAttributesMessage(this.signalBox.getBlockPos(),
            this.signalBox.getTicksToPower(), this.modeButton.getState()));
  }
}
