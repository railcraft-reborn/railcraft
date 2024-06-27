package mods.railcraft.client.gui.screen.inventory.detector;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.RailcraftMenuScreen;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetTankDetectorAttributesMessage;
import mods.railcraft.world.inventory.detector.TankDetectorMenu;
import mods.railcraft.world.level.block.entity.detector.TankDetectorBlockEntity;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TankDetectorScreen extends RailcraftMenuScreen<TankDetectorMenu> {

  private static final ResourceLocation BACKGROUND_TEXTURE =
      RailcraftConstants.rl("textures/gui/container/tank_detector.png");
  private static final int REFRESH_INTERVAL_TICKS = SharedConstants.TICKS_PER_SECOND;
  private final TankDetectorBlockEntity tankDetectorBlockEntity;
  private MultiButton<TankDetectorBlockEntity.Mode> mode;
  private int refreshTimer;

  public TankDetectorScreen(TankDetectorMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.imageHeight = 140;
    this.inventoryLabelY = this.imageHeight - 94;
    this.tankDetectorBlockEntity = this.menu.getTankDetectorBlockEntity();
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return BACKGROUND_TEXTURE;
  }

  @Override
  public void init() {
    super.init();
    int centreX = (this.width - this.getXSize()) / 2;
    int centreY = (this.height - this.getYSize()) / 2;

    this.addRenderableWidget(
        this.mode = MultiButton
            .builder(ButtonTexture.LARGE_BUTTON, this.tankDetectorBlockEntity.getMode())
            .bounds(centreX + 95, centreY + 22, 60, 16)
            .stateCallback(this::setMode)
            .build());
  }

  private void setMode(TankDetectorBlockEntity.Mode mode) {
    if (mode != this.tankDetectorBlockEntity.getMode()) {
      this.tankDetectorBlockEntity.setMode(mode);
      NetworkChannel.GAME.sendToServer(
          new SetTankDetectorAttributesMessage(this.tankDetectorBlockEntity.getBlockPos(), mode));
    }
  }

  @Override
  public void containerTick() {
    super.containerTick();
    if (this.refreshTimer++ >= REFRESH_INTERVAL_TICKS) {
      this.mode.setState(this.tankDetectorBlockEntity.getMode());
    }
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    super.renderLabels(guiGraphics, mouseX, mouseY);
    guiGraphics.drawString(this.font, Component.translatable(Translations.Screen.FILTER), 50,
        29, 0x404040, false);
  }
}
