package mods.railcraft.client.gui.screen;

import mods.railcraft.Translations;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.client.util.GuiUtil;
import mods.railcraft.network.PacketHandler;
import mods.railcraft.network.to_server.SetSignalControllerBoxMessage;
import mods.railcraft.world.level.block.entity.signal.SignalControllerBoxBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
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
    this.addRenderableWidget(Button
        .builder(Component.literal("<"), __ -> this.defaultAspect = this.defaultAspect.previous())
        .bounds(centredX + 10, centredY + 25, 30, 20)
        .build());
    this.addRenderableWidget(Button
        .builder(Component.literal(">"), __ -> this.defaultAspect = this.defaultAspect.next())
        .bounds(centredX + 135, centredY + 25, 30, 20)
        .build());
    this.addRenderableWidget(Button
        .builder(Component.literal("<"), __ -> this.poweredAspect = this.poweredAspect.previous())
        .bounds(centredX + 10, centredY + 60, 30, 20)
        .build());
    this.addRenderableWidget(Button
        .builder(Component.literal(">"), __ -> this.poweredAspect = this.poweredAspect.next())
        .bounds(centredX + 135, centredY + 60, 30, 20)
        .build());
  }

  @Override
  protected void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY,
      float partialTicks) {
    GuiUtil.drawCenteredString(guiGraphics, this.font,
        Component.translatable(Translations.Screen.SINGAL_CONTROLLER_BOX_DEFAULT),
        this.windowWidth, 25);
    GuiUtil.drawCenteredString(guiGraphics, this.font,
        this.defaultAspect.getDisplayName(), this.windowWidth, 35);
    GuiUtil.drawCenteredString(guiGraphics, this.font,
        Component.translatable(Translations.Screen.SINGAL_CONTROLLER_BOX_POWERED),
        this.windowWidth, 60);
    GuiUtil.drawCenteredString(guiGraphics, this.font,
        this.poweredAspect.getDisplayName(), this.windowWidth, 70);
  }

  @Override
  public void removed() {
    if (this.minecraft.level != null) {
      this.signalBox.setDefaultAspect(this.defaultAspect);
      this.signalBox.setPoweredAspect(this.poweredAspect);
      PacketHandler.sendToServer(
          new SetSignalControllerBoxMessage(this.signalBox.getBlockPos(),
              this.defaultAspect, this.poweredAspect));
    }
  }
}
