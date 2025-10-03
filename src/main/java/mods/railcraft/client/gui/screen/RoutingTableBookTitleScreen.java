package mods.railcraft.client.gui.screen;

import java.util.List;
import java.util.Optional;
import mods.railcraft.Translations;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.RailcraftButton;
import mods.railcraft.network.to_server.EditRoutingTableBookMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class RoutingTableBookTitleScreen extends Screen {

  private static final Component EDIT_TITLE_LABEL = Component.translatable("book.editTitle");
  private final RoutingTableBookScreen routingTableBookScreen;
  private final RoutingTableBookHelpScreen helpScreen;
  private final List<String> pages;
  private final InteractionHand hand;
  private final Component ownerText;
  private EditBox titleBox;
  private String titleValue = "";
  private RailcraftButton backButton;
  private RailcraftButton helpButton;

  protected RoutingTableBookTitleScreen(RoutingTableBookScreen routingTableBookScreen,
      Player owner, InteractionHand hand, List<String> pages) {
    super(GameNarrator.NO_TITLE);
    this.routingTableBookScreen = routingTableBookScreen;
    this.helpScreen = new RoutingTableBookHelpScreen(this.routingTableBookScreen);
    this.hand = hand;
    this.pages = pages;
    this.ownerText = Component.translatable("book.byAuthor", owner.getName()).withStyle(ChatFormatting.DARK_GRAY);
  }

  @Override
  protected void init() {
    this.backButton = this.addRenderableWidget(RailcraftButton
        .builder(CommonComponents.GUI_BACK, button -> {
          this.minecraft.setScreen(this.routingTableBookScreen);
        }, ButtonTexture.LARGE_BUTTON)
        .size(64, 20)
        .build());
    this.helpButton = this.addRenderableWidget(RailcraftButton
        .builder(Translations.Screen.HELP, button -> {
          this.minecraft.setScreen(this.helpScreen);
        }, ButtonTexture.LARGE_BUTTON)
        .size(64, 20)
        .build());
    var doneButton = this.addRenderableWidget(RailcraftButton
        .builder(CommonComponents.GUI_DONE, button -> {
          this.saveChanges();
          this.minecraft.setScreen(null);
        }, ButtonTexture.LARGE_BUTTON)
        .size(64, 20)
        .build());
    var layout = new LinearLayout(this.width / 2 - 100, this.height / 2 + 90,
        LinearLayout.Orientation.HORIZONTAL);
    layout.spacing(4);
    layout.addChild(this.backButton);
    layout.addChild(this.helpButton);
    layout.addChild(doneButton);
    layout.arrangeElements();

    int xOffset = (this.width - RoutingTableBookScreen.IMAGE_WIDTH) / 2;
    int yOffset = (this.height - RoutingTableBookScreen.IMAGE_HEIGHT) / 2;

    this.titleBox = this.addRenderableWidget(
        new EditBox(this.minecraft.font, xOffset + 60, yOffset  + 50, 114, 20, GameNarrator.NO_TITLE));
    this.titleBox.setMaxLength(15);
    this.titleBox.setBordered(false);
    this.titleBox.setCentered(true);
    this.titleBox.setTextColor(-16777216);
    this.titleBox.setTextShadow(false);
    this.titleBox.setResponder((s) -> doneButton.active = !StringUtil.isBlank(s));
    this.titleBox.setValue(this.titleValue);
  }

  @Override
  protected void setInitialFocus() {
    this.setInitialFocus(this.titleBox);
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }

  private void saveChanges() {
    ClientPacketDistributor.sendToServer(new EditRoutingTableBookMessage(this.hand, this.pages,
        Optional.of(this.titleBox.getValue().trim())));
  }

  @Override
  public boolean keyPressed(KeyEvent event) {
    if (this.titleBox.isFocused() && !this.titleBox.getValue().isEmpty() && (event.key() == 257 || event.key() == 335)) {
      this.saveChanges();
      this.minecraft.setScreen(null);
      return true;
    } else {
      return super.keyPressed(event);
    }
  }

  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.render(guiGraphics, mouseX, mouseY, partialTick);
    int xOffset = (this.width - RoutingTableBookScreen.IMAGE_WIDTH) / 2;
    int yOffset = (this.height - RoutingTableBookScreen.IMAGE_HEIGHT) / 2;
    int l = this.font.width(EDIT_TITLE_LABEL);
    guiGraphics.drawString(this.font, EDIT_TITLE_LABEL, xOffset + 160 - l, yOffset + 34, -16777216, false);
    int l1 = this.font.width(this.title);
    guiGraphics.drawString(this.font, this.title, xOffset + 120 - l1 / 2, yOffset + 50, -16777216, false);
    int l2 = this.font.width(this.ownerText);
    guiGraphics.drawString(this.font, this.ownerText, xOffset + 130 - l2, yOffset + 60, -16777216, false);
  }

  @Override
  public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    this.renderTransparentBackground(guiGraphics);
    int xOffset = (this.width - RoutingTableBookScreen.IMAGE_WIDTH) / 2;
    int yOffset = (this.height - RoutingTableBookScreen.IMAGE_HEIGHT) / 2;
    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, RoutingTableBookScreen.BOOK_LOCATION, xOffset, yOffset, 0, 0,
        RoutingTableBookScreen.IMAGE_WIDTH, RoutingTableBookScreen.IMAGE_HEIGHT,
        RoutingTableBookScreen.BACKGROUND_TEXTURE_WIDTH, RoutingTableBookScreen.BACKGROUND_TEXTURE_HEIGHT);
  }
}
