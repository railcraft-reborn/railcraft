package mods.railcraft.client.gui.screen.inventory.detector;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.util.GuiUtil;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetItemDetectorAttributesMessage;
import mods.railcraft.world.inventory.detector.ItemDetectorMenu;
import mods.railcraft.world.level.block.entity.detector.ItemDetectorBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.ScreenUtils;

public class ItemDetectorScreen extends AbstractContainerScreen<ItemDetectorMenu> {

  private static final ResourceLocation BACKGROUND_TEXTURE =
      RailcraftConstants.rl("textures/gui/container/item_detector.png");
  private final ItemDetectorBlockEntity itemDetector;
  private Button filterLeft, filterRight;

  public ItemDetectorScreen(ItemDetectorMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.itemDetector = menu.getItemDetector();
  }

  @Override
  protected void init() {
    super.init();
    int centreX = (this.width - this.getXSize()) / 2;
    int centreY = (this.height - this.getYSize()) / 2;
    this.addRenderableWidget(Button
        .builder(Component.literal("<"), __ -> {
          var value = this.itemDetector.getPrimaryMode().previous();
          this.itemDetector.setPrimaryMode(value);
          this.sendAttributes();
        })
        .bounds(centreX + 10, centreY + 16, 20, 20)
        .build());
    this.addRenderableWidget(Button
        .builder(Component.literal(">"), __ -> {
          var value = this.itemDetector.getPrimaryMode().next();
          this.itemDetector.setPrimaryMode(value);
          this.sendAttributes();
        })
        .bounds(centreX + 146, centreY + 16, 20, 20)
        .build());
    this.addRenderableWidget(this.filterLeft = Button
        .builder(Component.literal("<"), __ -> {
          var value = this.itemDetector.getFilterMode().previous();
          this.itemDetector.setFilterMode(value);
          this.sendAttributes();
        })
        .bounds(centreX + 10, centreY + 38, 20, 20)
        .build());
    this.addRenderableWidget(this.filterRight = Button
        .builder(Component.literal(">"), __ -> {
          var value = this.itemDetector.getFilterMode().next();
          this.itemDetector.setFilterMode(value);
          this.sendAttributes();
        })
        .bounds(centreX + 146, centreY + 38, 20, 20)
        .build());
  }

  private void sendAttributes() {
    NetworkChannel.GAME.sendToServer(
        new SetItemDetectorAttributesMessage(this.itemDetector.getBlockPos(),
            this.itemDetector.getPrimaryMode(), this.itemDetector.getFilterMode()));
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    this.renderBackground(guiGraphics);
    final int x = this.leftPos;
    final int y = this.topPos;
    guiGraphics.blit(BACKGROUND_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    var visible = itemDetector.getPrimaryMode() == ItemDetectorBlockEntity.PrimaryMode.FILTERED;
    this.filterLeft.visible = visible;
    this.filterRight.visible = visible;
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.render(guiGraphics, mouseX, mouseY, partialTick);
    if (itemDetector.getPrimaryMode() != ItemDetectorBlockEntity.PrimaryMode.FILTERED) {

    }
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    GuiUtil.drawCenteredString(guiGraphics, this.font,
        this.itemDetector.getPrimaryMode().getName(), this.imageWidth, 21);
    if (itemDetector.getPrimaryMode() == ItemDetectorBlockEntity.PrimaryMode.FILTERED) {
      GuiUtil.drawCenteredString(guiGraphics, this.font,
          this.itemDetector.getFilterMode().getName(), this.imageWidth, 43);
    } else {
      var color = RenderUtil.getColorARGB(80, 0, 0, 0);
      for (int slotNum = 0; slotNum < 9; slotNum++) {
        var slot = this.menu.slots.get(slotNum);
        int displayX = slot.x;
        int displayY = slot.y;
        var matrix4f = guiGraphics.pose().last().pose();
        ScreenUtils.drawGradientRect(matrix4f, 0, displayX, displayY,
            displayX + 16, displayY + 16, color, color);
      }
    }
  }
}
