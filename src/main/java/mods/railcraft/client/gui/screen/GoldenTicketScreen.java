package mods.railcraft.client.gui.screen;

import java.util.List;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Railcraft;
import mods.railcraft.Translations;
import mods.railcraft.client.gui.screen.IngameWindowScreen;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.RailcraftButton;
import mods.railcraft.client.util.GuiUtil;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetTicketAttributeMessage;
import mods.railcraft.util.ScreenUtil;
import mods.railcraft.world.item.TicketItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class GoldenTicketScreen extends IngameWindowScreen {

  private static final ResourceLocation TICKET_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/item/golden_ticket.png");
  private static final int IMAGE_WIDTH = 256;
  private static final int IMAGE_HEIGHT = 136;

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
    this.dest = "Dest=" + TicketItem.getDestination(this.itemStack);
    this.readingManual = false;
  }

  @Override
  protected void init() {
    var buttons = List.of(
        RailcraftButton
            .builder(CommonComponents.GUI_DONE, button -> {
              sendMessageToServer();
              this.minecraft.setScreen(null);
            }, ButtonTexture.LARGE_BUTTON)
            .pos(0, this.height / 2 + 75)
            .size(65, 20)
            .build(),
        helpButton = RailcraftButton
            .builder(Translations.Screen.HELP, button -> {
              readingManual = !readingManual;
            }, ButtonTexture.LARGE_BUTTON)
            .pos(0, this.height / 2 + 75)
            .size(65, 20)
            .build(),
        RailcraftButton
            .builder(CommonComponents.GUI_CANCEL, button -> {
              this.minecraft.setScreen(null);
            }, ButtonTexture.LARGE_BUTTON)
            .pos(0, this.height / 2 + 75)
            .size(65, 20)
            .build()
    );
    GuiUtil.newButtonRowAuto(this::addRenderableWidget, this.width / 2 - 100, 200, buttons);

    editBoxDest = new EditBox(font, this.width / 2 - (234 / 2), this.height / 2 + 17,
        234, 20, Component.empty());
    editBoxDest.setValue(dest);
    this.addRenderableWidget(editBoxDest);
  }

  @Override
  public void onClose() {}

  @Override
  protected void renderContent(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
    if (readingManual) {
      editBoxDest.setVisible(false);
      var about = Component.translatable(Translations.Screen.GOLDEN_TICKET_ABOUT);
      var help = Component.translatable(Translations.Screen.GOLDEN_TICKET_HELP)
          .withStyle(ChatFormatting.BLACK);

      ScreenUtil.drawCenteredString(poseStack, about, font, IMAGE_WIDTH, 15,
          IngameWindowScreen.TEXT_COLOR, false);
      font.drawWordWrap(help, this.width / 2 - 110, this.height / 2 - 40, 230,
          IngameWindowScreen.TEXT_COLOR);
      helpButton.setMessage(CommonComponents.GUI_BACK);
    } else {
      var title = Component.translatable(Translations.Screen.GOLDEN_TICKET_TITLE)
          .withStyle(ChatFormatting.WHITE, ChatFormatting.BOLD);
      var desc1 = Component.translatable(Translations.Screen.GOLDEN_TICKET_DESC_1);
      var desc2 = Component.translatable(Translations.Screen.GOLDEN_TICKET_DESC_2);
      poseStack.pushPose();
      poseStack.scale(2, 2, 2);
      ScreenUtil.drawCenteredString(poseStack, title, font, IMAGE_WIDTH / 2, 8,
          IngameWindowScreen.TEXT_COLOR, true);
      poseStack.popPose();
      ScreenUtil.drawCenteredString(poseStack, desc1, font, IMAGE_WIDTH, 45,
          IngameWindowScreen.TEXT_COLOR, false);
      ScreenUtil.drawCenteredString(poseStack, desc2, font, IMAGE_WIDTH, 60,
          IngameWindowScreen.TEXT_COLOR, false);
      editBoxDest.setVisible(true);
      helpButton.setMessage(Component.translatable(Translations.Screen.HELP));
    }
  }

  private void sendMessageToServer() {
    this.dest = editBoxDest.getValue();
    var modified = this.dest.startsWith("Dest=") && !this.dest.equals("Dest=");
    if (!modified)
      return;

    var destWithoutPrefix = this.dest.substring("Dest=".length());
    var success = TicketItem.setTicketData(itemStack, destWithoutPrefix,
        this.minecraft.player.getGameProfile());
    if (success) {
      NetworkChannel.GAME.sendToServer(new SetTicketAttributeMessage(this.hand, destWithoutPrefix));
    }
  }
}
