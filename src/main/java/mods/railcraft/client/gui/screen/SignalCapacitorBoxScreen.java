package mods.railcraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetSignalCapacitorBoxAttributesMessage;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
    this.addButton(
        new Button(centredX + 13, centredY + 38, 30, 20, new StringTextComponent("-10"),
            __ -> this.incrementTicksToPower(-200)));
    this.addButton(
        new Button(centredX + 53, centredY + 38, 30, 20, new StringTextComponent("-1"),
            __ -> this.incrementTicksToPower(-20)));
    this.addButton(
        new Button(centredX + 93, centredY + 38, 30, 20, new StringTextComponent("+1"),
            __ -> this.incrementTicksToPower(20)));
    this.addButton(
        new Button(centredX + 133, centredY + 38, 30, 20, new StringTextComponent("+10"),
            __ -> this.incrementTicksToPower(200)));
    this.addButton(this.modeButton = new MultiButton<>(centredX + 23, centredY + 65, 130, 15,
        this.signalBox.getMode(), __ -> this.setMode(this.modeButton.getState())));
  }

  @Override
  protected void renderContent(MatrixStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    this.drawCenteredString(matrixStack,
        new TranslationTextComponent("screen.signal_capacitor_box.duration",
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

  private void incrementTicksToPower(int incrementAmount) {
    short ticksToPower = (short) (this.signalBox.getTicksToPower() + incrementAmount);
    if (this.signalBox.getTicksToPower() != ticksToPower) {
      this.signalBox.setTicksToPower(ticksToPower);
      this.sendAttributes();
    }
  }

  private void sendAttributes() {
    NetworkChannel.PLAY.getSimpleChannel()
        .sendToServer(new SetSignalCapacitorBoxAttributesMessage(this.signalBox.getBlockPos(),
            this.signalBox.getTicksToPower(), this.modeButton.getState()));
  }
}
