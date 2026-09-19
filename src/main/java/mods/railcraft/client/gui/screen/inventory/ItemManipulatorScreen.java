package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.IngameWindowScreen;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.network.to_server.SetItemManipulatorMessage;
import mods.railcraft.world.inventory.ItemManipulatorMenu;
import mods.railcraft.world.level.block.entity.manipulator.ItemManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ManipulatorBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class ItemManipulatorScreen extends ManipulatorScreen<ItemManipulatorMenu> {

  private static final Identifier WIDGETS_TEXTURE_LOCATION =
      RailcraftConstants.id("textures/gui/container/item_manipulator.png");

  private final static Component FILTERS_TEXT =
      Component.translatable(Translations.Screen.ITEM_MANIPULATOR_FILTERS);
  private final static Component BUFFER_TEXT =
      Component.translatable(Translations.Screen.ITEM_MANIPULATOR_BUFFER);

  private MultiButton<ManipulatorBlockEntity.TransferMode> transferModeButton;

  public ItemManipulatorScreen(ItemManipulatorMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
  }

  @Override
  public void init() {
    super.init();
    int centreX = (this.width - this.getImageWidth()) / 2;
    int centreY = (this.height - this.getImageHeight()) / 2;

    this.addRenderableWidget(
        this.transferModeButton = MultiButton
            .builder(ButtonTexture.SMALL_BUTTON, this.menu.getManipulator().getTransferMode())
            .bounds(centreX + 73, centreY + 45, 30, 16)
            .stateCallback(this::setTransferMode)
            .build());
  }

  private void setTransferMode(ManipulatorBlockEntity.TransferMode transferMode) {
    if (transferMode != this.menu.getManipulator().getTransferMode()) {
      this.menu.getManipulator().setTransferMode(transferMode);
      this.sendAttributes();
    }
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
    super.extractLabels(graphics, xm, ym);
    graphics.text(this.font, FILTERS_TEXT, 18, 16, IngameWindowScreen.TEXT_COLOR, false);
    graphics.text(this.font, BUFFER_TEXT, 126, 16, IngameWindowScreen.TEXT_COLOR, false);
  }

  @Override
  protected void refresh() {
    super.refresh();
    this.transferModeButton.setState(this.menu.getManipulator().getTransferMode());
  }

  @Override
  protected void sendAttributes() {
    ItemManipulatorBlockEntity manipulator = this.menu.getManipulator();
    ClientPacketDistributor.sendToServer(
        new SetItemManipulatorMessage(manipulator.getBlockPos(),
            manipulator.getRedstoneMode(), manipulator.getTransferMode()));
  }

  @Override
  public Identifier getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
