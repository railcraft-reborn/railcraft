package mods.railcraft.client.gui.screen.inventory;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.client.gui.screen.IngameWindowScreen;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.world.inventory.ManipulatorMenu;
import mods.railcraft.world.level.block.entity.manipulator.ManipulatorBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class ManipulatorScreen<T extends ManipulatorMenu<?>>
    extends RailcraftMenuScreen<T> {

  private static final int REFRESH_INTERVAL_TICKS = 20;

  private static final Component CART_FILTER_TEXT =
      Component.translatable("screen.manipulator.cart_filters");

  private MultiButton<ManipulatorBlockEntity.RedstoneMode> redstoneModeButton;

  private int refreshTimer;

  protected ManipulatorScreen(T menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
  }

  @Override
  public void init() {
    super.init();
    var centreX = (this.width - this.getXSize()) / 2;
    var centreY = (this.height - this.getYSize()) / 2;
    this.addRenderableWidget(this.redstoneModeButton = new MultiButton<>(centreX + 73, centreY + 62, 30, 16,
        this.menu.getManipulator().getRedstoneMode(), this::renderComponentTooltip,
        __ -> this.setRedstoneMode(this.redstoneModeButton.getState())));
    this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
  }

  private void setRedstoneMode(ManipulatorBlockEntity.RedstoneMode redstoneMode) {
    if (redstoneMode != this.menu.getManipulator().getRedstoneMode()) {
      this.menu.getManipulator().setRedstoneMode(redstoneMode);
      this.sendAttributes();
    }
  }

  protected abstract void sendAttributes();

  @Override
  public void containerTick() {
    super.containerTick();
    if (this.refreshTimer++ >= REFRESH_INTERVAL_TICKS) {
      this.refreshTimer = 0;
      this.refresh();
    }
  }

  protected void refresh() {
    this.redstoneModeButton.setState(this.menu.getManipulator().getRedstoneMode());
  }

  @Override
  protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
    this.font.draw(poseStack, this.title, this.titleLabelX, this.titleLabelY, 0x333333);
    if (this.getMenu().hasCartFilter()) {
      this.font.draw(poseStack, CART_FILTER_TEXT, 75, 16, IngameWindowScreen.TEXT_COLOR);
    }
  }
}
