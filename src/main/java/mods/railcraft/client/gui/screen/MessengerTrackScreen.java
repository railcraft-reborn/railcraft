package mods.railcraft.client.gui.screen;

import mods.railcraft.Translations;
import mods.railcraft.client.util.GuiUtil;
import mods.railcraft.network.to_server.SetMessengerTrackMessage;
import mods.railcraft.world.level.block.entity.track.MessengerTrackBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

public class MessengerTrackScreen extends IngameWindowScreen {

  private static final int LABEL_X = 8;
  private static final int BOX_X = 56;
  private static final int LABEL_GAP = 4;
  private static final int BOX_WIDTH = 112;
  private static final int BOX_HEIGHT = 16;
  private static final int FIRST_ROW_Y = 22;
  private static final int ROW_SPACING = 21;

  private final MessengerTrackBlockEntity track;
  private EditBox titleBox, subtitleBox, actionbarBox;

  public MessengerTrackScreen(MessengerTrackBlockEntity track) {
    super(track.getBlockState().getBlock().getName(), LARGE_WINDOW_TEXTURE,
        DEFAULT_WINDOW_WIDTH, LARGE_WINDOW_HEIGHT);
    this.track = track;
  }

  @Override
  protected void init() {
    int centredX = (this.width - this.windowWidth) / 2;
    int centredY = (this.height - this.windowHeight) / 2;
    this.titleBox = this.createBox(centredX, centredY, 0, this.track.getTitle(),
        Translations.Screen.MESSENGER_TRACK_TITLE);
    this.subtitleBox = this.createBox(centredX, centredY, 1, this.track.getSubtitle(),
        Translations.Screen.MESSENGER_TRACK_SUBTITLE);
    this.actionbarBox = this.createBox(centredX, centredY, 2, this.track.getActionbar(),
        Translations.Screen.MESSENGER_TRACK_ACTIONBAR);
    this.setInitialFocus(this.titleBox);

    this.addRenderableWidget(Button
        .builder(CommonComponents.GUI_DONE, __ -> this.onClose())
        .bounds(centredX + (this.windowWidth - 80) / 2, centredY + 86, 80, 20)
        .build());
  }

  private EditBox createBox(int centredX, int centredY, int row, String value,
      String narrationKey) {
    var box = new EditBox(this.font, centredX + BOX_X, centredY + FIRST_ROW_Y + row * ROW_SPACING,
        BOX_WIDTH, BOX_HEIGHT, Component.translatable(narrationKey));
    box.setMaxLength(MessengerTrackBlockEntity.MAX_LENGTH);
    box.setValue(value);
    return this.addRenderableWidget(box);
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    super.render(guiGraphics, mouseX, mouseY, partialTicks);
    int centredX = (this.width - this.windowWidth) / 2;
    int centredY = (this.height - this.windowHeight) / 2;
    this.drawLabel(guiGraphics, centredX, centredY, 0, Translations.Screen.MESSENGER_TRACK_TITLE);
    this.drawLabel(guiGraphics, centredX, centredY, 1,
        Translations.Screen.MESSENGER_TRACK_SUBTITLE);
    this.drawLabel(guiGraphics, centredX, centredY, 2,
        Translations.Screen.MESSENGER_TRACK_ACTIONBAR);
  }

  /**
   * Labels scroll when they do not fit before their text box, as translations can be long.
   */
  private void drawLabel(GuiGraphics guiGraphics, int centredX, int centredY, int row,
      String key) {
    int y = centredY + FIRST_ROW_Y + row * ROW_SPACING;
    GuiUtil.drawScrollingString(guiGraphics, this.font, Component.translatable(key),
        centredX + LABEL_X, y, centredX + BOX_X - LABEL_GAP, y + BOX_HEIGHT, TEXT_COLOR);
  }

  @Override
  public void onClose() {
    var title = this.titleBox.getValue();
    var subtitle = this.subtitleBox.getValue();
    var actionbar = this.actionbarBox.getValue();
    if (!title.equals(this.track.getTitle())
        || !subtitle.equals(this.track.getSubtitle())
        || !actionbar.equals(this.track.getActionbar())) {
      this.track.setMessages(title, subtitle, actionbar);
      PacketDistributor.sendToServer(new SetMessengerTrackMessage(this.track.getBlockPos(),
          this.track.getTitle(), this.track.getSubtitle(), this.track.getActionbar()));
    }
    super.onClose();
  }
}
