package mods.railcraft.client.gui.screen;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.RailcraftButton;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.EditTicketAttributeMessage;
import mods.railcraft.world.item.TicketItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class GoldenTicketScreen extends IngameWindowScreen {

  private static final ResourceLocation TICKET_LOCATION =
      RailcraftConstants.rl("textures/gui/item/golden_ticket.png");
  private static final int IMAGE_WIDTH = 256;
  private static final int IMAGE_HEIGHT = 136;
  private static final String PREFIX = "Dest=";

  private final ItemStack itemStack;
  private final InteractionHand hand;
  private String dest;
  private RailcraftButton helpButton;
  private EditBox editBoxDest;
  private boolean readingManual;

  public GoldenTicketScreen(ItemStack itemStack, InteractionHand hand) {
    super(GameNarrator.NO_TITLE, TICKET_LOCATION, IMAGE_WIDTH, IMAGE_HEIGHT);
    this.itemStack = itemStack;
    this.hand = hand;
    this.dest = PREFIX + TicketItem.getDestination(this.itemStack);
    this.readingManual = false;
  }

  @Override
  protected void init() {
    var doneButton = this.addRenderableWidget(RailcraftButton
        .builder(CommonComponents.GUI_DONE, button -> {
          sendMessageToServer();
          this.minecraft.setScreen(null);
        }, ButtonTexture.LARGE_BUTTON)
        .size(64, 20)
        .build());
    this.helpButton = this.addRenderableWidget(RailcraftButton
        .builder(Translations.Screen.HELP, button -> {
          this.readingManual = !this.readingManual;
        }, ButtonTexture.LARGE_BUTTON)
        .size(64, 20)
        .build());
    var cancelButton = this.addRenderableWidget(RailcraftButton
        .builder(CommonComponents.GUI_CANCEL, button -> {
          this.minecraft.setScreen(null);
        }, ButtonTexture.LARGE_BUTTON)
        .size(64, 20)
        .build());
    var layout = new LinearLayout(this.width / 2 - 100, this.height / 2 + 75, 200, 20,
        LinearLayout.Orientation.HORIZONTAL);
    layout.addChild(doneButton);
    layout.addChild(this.helpButton);
    layout.addChild(cancelButton);
    layout.arrangeElements();

    this.editBoxDest = new EditBox(font, this.width / 2 - (234 / 2), this.height / 2 + 23,
        234, 20, Component.empty());
    this.editBoxDest.setValue(this.dest);
    this.editBoxDest.setBordered(false);
    this.addRenderableWidget(this.editBoxDest);
  }

  @Override
  protected void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY,
      float partialTicks) {
    if (this.readingManual) {
      this.editBoxDest.setVisible(false);
      var about = Component.translatable(Translations.Screen.GOLDEN_TICKET_ABOUT);
      var help = Component.translatable(Translations.Screen.GOLDEN_TICKET_HELP)
          .withStyle(ChatFormatting.BLACK);

      guiGraphics.drawString(this.font, about, this.windowWidth / 2 - this.font.width(about) / 2,
          15, TEXT_COLOR, false);

      guiGraphics.drawWordWrap(this.font, help, 15, 30, 230, TEXT_COLOR);
      this.helpButton.setMessage(CommonComponents.GUI_BACK);
    } else {
      var title = Component.translatable(Translations.Screen.GOLDEN_TICKET_TITLE)
          .withStyle(ChatFormatting.WHITE, ChatFormatting.BOLD);
      var desc1 = Component.translatable(Translations.Screen.GOLDEN_TICKET_DESC_1);
      var desc2 = Component.translatable(Translations.Screen.GOLDEN_TICKET_DESC_2);
      var poseStack = guiGraphics.pose();
      poseStack.pushPose();
      {
        poseStack.scale(2, 2, 2);
        guiGraphics.drawCenteredString(this.font, title, IMAGE_WIDTH / 4, 8, TEXT_COLOR);
      }
      poseStack.popPose();
      guiGraphics.drawString(this.font, desc1, this.windowWidth / 2 - this.font.width(desc1) / 2,
          45, TEXT_COLOR, false);
      guiGraphics.drawString(this.font, desc2, this.windowWidth / 2 - this.font.width(desc2) / 2,
          60, TEXT_COLOR, false);
      this.editBoxDest.setVisible(true);
      this.helpButton.setMessage(Component.translatable(Translations.Screen.HELP));
    }
  }

  private void sendMessageToServer() {
    this.dest = this.editBoxDest.getValue();
    var isValid = this.dest.startsWith(PREFIX);
    if (!isValid)
      return;
    this.dest = this.dest.trim();
    var destWithoutPrefix = this.dest.substring(PREFIX.length());
    var success = TicketItem.setTicketData(this.itemStack, destWithoutPrefix,
        this.minecraft.player.getGameProfile());
    if (success) {
      NetworkChannel.GAME
          .sendToServer(new EditTicketAttributeMessage(this.hand, destWithoutPrefix));
    }
  }
}
