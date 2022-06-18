package mods.railcraft.client.gui.screen.inventory;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Railcraft;
import mods.railcraft.client.gui.screen.IngameWindowScreen;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetItemManipulatorAttributesMessage;
import mods.railcraft.world.inventory.ItemManipulatorMenu;
import mods.railcraft.world.level.block.entity.manipulator.ItemManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ManipulatorBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class ItemManipulatorScreen extends ManipulatorScreen<ItemManipulatorMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/item_manipulator.png");

  private final static Component FILTERS_TEXT =
      new TranslatableComponent("screen.item_manipulator.filters");
  private final static Component BUFFER_TEXT =
      new TranslatableComponent("screen.item_manipulator.buffer");

  private MultiButton<ManipulatorBlockEntity.TransferMode> transferModeButton;

  public ItemManipulatorScreen(ItemManipulatorMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
  }

  @Override
  public void init() {
    super.init();
    int centreX = (this.width - this.getXSize()) / 2;
    int centreY = (this.height - this.getYSize()) / 2;
    this.addRenderableWidget(this.transferModeButton = new MultiButton<>(centreX + 73, centreY + 45, 30, 16,
        this.menu.getManipulator().getTransferMode(), this::renderComponentTooltip,
        __ -> this.setTransferMode(this.transferModeButton.getState())));
  }

  private void setTransferMode(ManipulatorBlockEntity.TransferMode transferMode) {
    if (transferMode != this.menu.getManipulator().getTransferMode()) {
      this.menu.getManipulator().setTransferMode(transferMode);
      this.sendAttributes();
    }
  }

  @Override
  protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
    super.renderLabels(poseStack, mouseX, mouseY);
    this.font.draw(poseStack, FILTERS_TEXT, 18, 16, IngameWindowScreen.TEXT_COLOR);
    this.font.draw(poseStack, BUFFER_TEXT, 126, 16, IngameWindowScreen.TEXT_COLOR);
  }

  @Override
  protected void refresh() {
    super.refresh();
    this.transferModeButton.setState(this.menu.getManipulator().getTransferMode());
  }

  @Override
  protected void sendAttributes() {
    ItemManipulatorBlockEntity manipulator = this.menu.getManipulator();
    NetworkChannel.GAME.getSimpleChannel().sendToServer(
        new SetItemManipulatorAttributesMessage(manipulator.getBlockPos(),
            manipulator.getRedstoneMode(), manipulator.getTransferMode()));
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
