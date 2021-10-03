package mods.railcraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetSignalCapacitorBoxAttributesMessage;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SignalCapacitorBoxScreen extends BasicIngameScreen {

  private final SignalCapacitorBoxBlockEntity signalBox;
  private short ticksToPower;
  private MultiButton<SignalCapacitorBoxBlockEntity.Mode> stateMode;

  public SignalCapacitorBoxScreen(SignalCapacitorBoxBlockEntity signalBox) {
    super(signalBox.getDisplayName());
    this.signalBox = signalBox;
    this.ticksToPower = signalBox.getTicksToPower();
  }

  @Override
  public void init() {
    int centredX = (this.width - this.x) / 2;
    int centredY = (this.height - this.y) / 2;
    this.addButton(
        new Button(centredX + 13, centredY + 38, 30, 20, new StringTextComponent("-10"),
            __ -> this.ticksToPower -= 200));
    this.addButton(
        new Button(centredX + 53, centredY + 38, 30, 20, new StringTextComponent("-1"),
            __ -> this.ticksToPower -= 20));
    this.addButton(
        new Button(centredX + 93, centredY + 38, 30, 20, new StringTextComponent("+1"),
            __ -> this.ticksToPower += 20));
    this.addButton(
        new Button(centredX + 133, centredY + 38, 30, 20, new StringTextComponent("+10"),
            __ -> this.ticksToPower += 200));
    this.addButton(this.stateMode = new MultiButton<>(centredX + 23, centredY + 65, 130, 15,
        this.signalBox.getModeButtonController().copy()));
  }

  @Override
  protected void renderContent(MatrixStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    this.drawCenteredString(matrixStack,
        new TranslationTextComponent("screen.signal_capacitor_box.duration",
            this.ticksToPower / 20),
        this.x / 2, 25);
  }

  @Override
  public void removed() {
    if (this.minecraft.level != null) {
      this.signalBox.setTicksToPower(this.ticksToPower);
      this.signalBox.getModeButtonController()
          .setCurrentState(this.stateMode.getController().getCurrentState());
      NetworkChannel.PLAY.getSimpleChannel()
          .sendToServer(new SetSignalCapacitorBoxAttributesMessage(this.signalBox.getBlockPos(),
              this.ticksToPower, this.stateMode.getController().getCurrentState()));
    }
  }
}
