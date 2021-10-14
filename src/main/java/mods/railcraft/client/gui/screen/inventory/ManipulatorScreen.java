package mods.railcraft.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.client.gui.screen.IngameWindowScreen;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.world.inventory.ManipulatorMenu;
import mods.railcraft.world.level.block.entity.ManipulatorBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class ManipulatorScreen<T extends ManipulatorMenu<?>>
    extends RailcraftMenuScreen<T> {

  private static final int REFRESH_INTERVAL_TICKS = 20;

  private static final ITextComponent CART_FILTER_TEXT =
      new TranslationTextComponent("screen.manipulator.cart_filters");

  private MultiButton<ManipulatorBlockEntity.RedstoneMode> redstoneModeButton;

  private int refreshTimer;

  protected ManipulatorScreen(T menu, PlayerInventory playerInventory, ITextComponent title) {
    super(menu, playerInventory, title);
  }

  @Override
  public void init() {
    super.init();
    int centreX = (this.width - this.getXSize()) / 2;
    int centreY = (this.height - this.getYSize()) / 2;
    this.addButton(this.redstoneModeButton = new MultiButton<>(centreX + 73, centreY + 62, 30, 16,
        this.menu.getManipulator().getRedstoneMode(),
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
  public void tick() {
    super.tick();
    if (this.refreshTimer++ >= REFRESH_INTERVAL_TICKS) {
      this.refreshTimer = 0;
      this.refresh();
    }
  }

  protected void refresh() {
    this.redstoneModeButton.setState(this.menu.getManipulator().getRedstoneMode());
  }

  @Override
  protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
    this.font.draw(matrixStack, this.title, this.titleLabelX, this.titleLabelY, 0x333333);
    if (this.getMenu().hasCartFilter()) {
      this.font.draw(matrixStack, CART_FILTER_TEXT, 75, 16, IngameWindowScreen.TEXT_COLOR);
    }
  }
}
