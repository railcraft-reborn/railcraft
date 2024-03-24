package mods.railcraft.client.gui.screen;

import mods.railcraft.RailcraftConfig;
import mods.railcraft.Translations;
import mods.railcraft.client.util.GuiUtil;
import mods.railcraft.network.PacketHandler;
import mods.railcraft.network.to_server.SetLauncherTrackMessage;
import mods.railcraft.world.level.block.entity.track.LauncherTrackBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class LauncherTrackScreen extends IngameWindowScreen {

  private final LauncherTrackBlockEntity track;

  public LauncherTrackScreen(LauncherTrackBlockEntity track) {
    super(track.getBlockState().getBlock().getName());
    this.track = track;
  }

  @Override
  public void init() {
    int centredX = (this.width - this.windowWidth) / 2;
    int centredY = (this.height - this.windowHeight) / 2;
    this.addRenderableWidget(Button
        .builder(Component.literal("-10"), __ -> this.incrementForce(-10))
        .bounds(centredX + 13, centredY + 50, 30, 20)
        .build());
    this.addRenderableWidget(Button
        .builder(Component.literal("-1"), __ -> this.incrementForce(-1))
        .bounds(centredX + 53, centredY + 50, 30, 20)
        .build());
    this.addRenderableWidget(Button
        .builder(Component.literal("+1"), __ -> this.incrementForce(1))
        .bounds(centredX + 93, centredY + 50, 30, 20)
        .build());
    this.addRenderableWidget(Button
        .builder(Component.literal("+10"), __ -> this.incrementForce(10))
        .bounds(centredX + 133, centredY + 50, 30, 20)
        .build());
  }

  @Override
  protected void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY,
      float partialTicks) {
    var componentForce = Component.translatable(Translations.Screen.LAUNCHER_TRACK_LAUNCH_FORCE,
        this.track.getLaunchForce());
    GuiUtil.drawCenteredString(guiGraphics, this.font, componentForce, this.windowWidth, 25);
  }

  private void incrementForce(int incrementAmount) {
    var force = (byte) Mth.clamp(this.track.getLaunchForce() + incrementAmount,
        LauncherTrackBlockEntity.MIN_LAUNCH_FORCE,
        RailcraftConfig.SERVER.maxLauncherTrackForce.get());
    if (this.track.getLaunchForce() != force) {
      this.track.setLaunchForce(force);
      this.sendAttributes();
    }
  }

  private void sendAttributes() {
    PacketHandler.sendToServer(
        new SetLauncherTrackMessage(this.track.getBlockPos(),
            this.track.getLaunchForce()));
  }
}
