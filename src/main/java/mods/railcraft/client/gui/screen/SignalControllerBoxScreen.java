package mods.railcraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetSignalControllerBoxAttributesMessage;
import mods.railcraft.world.level.block.entity.signal.SignalControllerBoxBlockEntity;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SignalControllerBoxScreen extends BasicIngameScreen {

  private final SignalControllerBoxBlockEntity signalBox;

  private SignalAspect defaultAspect;
  private SignalAspect poweredAspect;

  public SignalControllerBoxScreen(SignalControllerBoxBlockEntity signalBox) {
    super(signalBox.getDisplayName());
    this.signalBox = signalBox;
    this.defaultAspect = signalBox.getDefaultAspect();
    this.poweredAspect = signalBox.getPoweredAspect();
  }

  @Override
  public void init() {
    int centredX = (this.width - this.x) / 2;
    int centredY = (this.height - this.y) / 2;
    this.addButton(
        new Button(centredX + 10, centredY + 25, 30, 20, new StringTextComponent("<"),
            __ -> this.defaultAspect = this.defaultAspect.getPrevious()));
    this.addButton(
        new Button(centredX + 135, centredY + 25, 30, 20, new StringTextComponent(">"),
            __ -> this.defaultAspect = this.defaultAspect.getNext()));
    this.addButton(
        new Button(centredX + 10, centredY + 60, 30, 20, new StringTextComponent("<"),
            __ -> this.poweredAspect = this.poweredAspect.getPrevious()));
    this.addButton(
        new Button(centredX + 135, centredY + 60, 30, 20, new StringTextComponent(">"),
            __ -> this.poweredAspect = this.poweredAspect.getNext()));
  }

  @Override
  protected void renderContent(MatrixStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    this.drawCenteredString(matrixStack,
        new TranslationTextComponent("screen.signal_controller_box.default_aspect"),
        this.x / 2, 25);
    this.drawCenteredString(matrixStack, this.defaultAspect.getDisplayName(),
        this.x / 2, 35);
    this.drawCenteredString(matrixStack,
        new TranslationTextComponent("screen.signal_controller_box.powered_aspect"),
        this.x / 2, 60);
    this.drawCenteredString(matrixStack, this.poweredAspect.getDisplayName(),
        this.x / 2, 70);
  }

  @Override
  public void removed() {
    if (this.minecraft.level != null) {
      this.signalBox.setDefaultAspect(this.defaultAspect);
      this.signalBox.setPoweredAspect(this.poweredAspect);
      NetworkChannel.PLAY.getSimpleChannel().sendToServer(
          new SetSignalControllerBoxAttributesMessage(this.signalBox.getBlockPos(),
              this.defaultAspect, this.poweredAspect));
    }
  }
}
