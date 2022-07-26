package mods.railcraft.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.client.Translations;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetSignalControllerBoxAttributesMessage;
import mods.railcraft.world.level.block.entity.signal.SignalControllerBoxBlockEntity;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class SignalControllerBoxScreen extends IngameWindowScreen {

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
    int centredX = (this.width - this.windowWidth) / 2;
    int centredY = (this.height - this.windowHeight) / 2;
    this.addRenderableWidget(
        new Button(centredX + 10, centredY + 25, 30, 20, Component.literal("<"),
            __ -> this.defaultAspect = this.defaultAspect.getPrevious()));
    this.addRenderableWidget(
        new Button(centredX + 135, centredY + 25, 30, 20, Component.literal(">"),
            __ -> this.defaultAspect = this.defaultAspect.getNext()));
    this.addRenderableWidget(
        new Button(centredX + 10, centredY + 60, 30, 20, Component.literal("<"),
            __ -> this.poweredAspect = this.poweredAspect.getPrevious()));
    this.addRenderableWidget(
        new Button(centredX + 135, centredY + 60, 30, 20, Component.literal(">"),
            __ -> this.poweredAspect = this.poweredAspect.getNext()));
  }

  @Override
  protected void renderContent(PoseStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    this.drawCenteredString(matrixStack,
        Component.translatable(Translations.Screen.SINGAL_CONTROLLER_BOX_DEFAULT),
        this.windowWidth / 2, 25);
    this.drawCenteredString(matrixStack, this.defaultAspect.getDisplayName(),
        this.windowWidth / 2, 35);
    this.drawCenteredString(matrixStack,
        Component.translatable(Translations.Screen.SINGAL_CONTROLLER_BOX_POWERED),
        this.windowWidth / 2, 60);
    this.drawCenteredString(matrixStack, this.poweredAspect.getDisplayName(),
        this.windowWidth / 2, 70);
  }

  @Override
  public void removed() {
    if (this.minecraft.level != null) {
      this.signalBox.setDefaultAspect(this.defaultAspect);
      this.signalBox.setPoweredAspect(this.poweredAspect);
      NetworkChannel.GAME.getSimpleChannel().sendToServer(
          new SetSignalControllerBoxAttributesMessage(this.signalBox.getBlockPos(),
              this.defaultAspect, this.poweredAspect));
    }
  }
}
