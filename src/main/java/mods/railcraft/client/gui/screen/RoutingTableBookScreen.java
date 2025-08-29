package mods.railcraft.client.gui.screen;

import java.util.List;
import java.util.Optional;
import com.google.common.collect.Lists;
import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.RailcraftButton;
import mods.railcraft.client.gui.widget.button.RailcraftPageButton;
import mods.railcraft.network.to_server.EditRoutingTableBookMessage;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import mods.railcraft.world.item.component.RoutingTableBookContent;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class RoutingTableBookScreen extends Screen {

  static final ResourceLocation BOOK_LOCATION =
      RailcraftConstants.rl("textures/gui/item/routing_table_book.png");
  static final int TEXT_WIDTH = 220;
  static final int TEXT_HEIGHT = 128;
  static final int IMAGE_WIDTH = 256;
  static final int IMAGE_HEIGHT = 192;
  static final int BACKGROUND_TEXTURE_WIDTH = 256;
  static final int BACKGROUND_TEXTURE_HEIGHT = 256;

  private final Player owner;
  private final ItemStack book;
  private final RoutingTableBookHelpScreen helpScreen;
  private final RoutingTableBookTitleScreen titleScreen;

  private int currentPage;
  private final List<String> pages = Lists.newArrayList();
  private RailcraftPageButton forwardButton;
  private RailcraftPageButton backButton;
  private final InteractionHand hand;
  private Component numberOfPages;
  private MultiLineEditBox page;

  private static final int BOOK_MAX_PAGES = 50;
  private RailcraftButton titleButton, helpButton;

  public RoutingTableBookScreen(Player owner, ItemStack book, InteractionHand hand) {
    super(GameNarrator.NO_TITLE);
    this.numberOfPages = CommonComponents.EMPTY;
    this.owner = owner;
    this.book = book;
    this.hand = hand;

    var writableBookContent = book.get(RailcraftDataComponents.ROUTING_TABLE_BOOK);
    if (writableBookContent != null) {
      this.pages.addAll(writableBookContent.pages());
    }
    if (this.pages.isEmpty()) {
      this.pages.add("");
    }

    this.helpScreen = new RoutingTableBookHelpScreen(this);
    this.titleScreen = new RoutingTableBookTitleScreen(this, owner, hand, this.pages);
  }

  private int getNumPages() {
    return this.pages.size();
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }

  @Override
  protected void init() {
    int xOffset = (this.width - IMAGE_WIDTH) / 2;
    int yOffset = (this.height - IMAGE_HEIGHT) / 2;

    this.page = MultiLineEditBox.builder()
        .setShowDecorations(false)
        .setTextColor(-16777216)
        .setCursorColor(-16777216)
        .setShowBackground(false)
        .setTextShadow(false)
        .setX((this.width - TEXT_WIDTH) / 2 - 7)
        .setY((this.height - TEXT_HEIGHT) / 2 - 10)
        .build(this.font, TEXT_WIDTH, 134, CommonComponents.EMPTY);

    this.page.setCharacterLimit(1024);
    this.page.setLineLimit(14);
    this.page.setValueListener((s) -> this.pages.set(this.currentPage, s));
    this.addRenderableWidget(this.page);
    this.updatePageContent();
    this.numberOfPages = this.getPageNumberMessage();

    this.backButton = this.addRenderableWidget(
        new RailcraftPageButton(xOffset + 30, yOffset + 150, false, BOOK_LOCATION,
            button -> this.pageBack()));
    this.forwardButton = this.addRenderableWidget(
        new RailcraftPageButton(xOffset + 200, yOffset + 150, true, BOOK_LOCATION,
            button -> this.pageForward()));
    this.titleButton = this.addRenderableWidget(RailcraftButton
        .builder(Translations.Screen.NAME, button -> {
          this.minecraft.setScreen(this.titleScreen);
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
    layout.addChild(this.titleButton);
    layout.addChild(this.helpButton);
    layout.addChild(doneButton);
    layout.arrangeElements();
    this.updateButtonVisibility();
  }

  @Override
  protected void setInitialFocus() {
    this.setInitialFocus(this.page);
  }

  @Override
  public Component getNarrationMessage() {
    return CommonComponents.joinForNarration(super.getNarrationMessage(), this.getPageNumberMessage());
  }

  private Component getPageNumberMessage() {
    return Component.translatable("book.pageIndicator", this.currentPage + 1, this.getNumPages());
  }

  private void pageBack() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.updatePageContent();
    }
    this.updateButtonVisibility();
  }

  private void pageForward() {
    if (this.currentPage < this.getNumPages() - 1) {
      this.currentPage++;
    } else {
      this.appendPageToBook();
      if (this.currentPage < this.getNumPages() - 1) {
        this.currentPage++;
      }
    }
    this.updatePageContent();
    this.updateButtonVisibility();
  }

  private void updatePageContent() {
    this.page.setValue(this.pages.get(this.currentPage), true);
    this.numberOfPages = this.getPageNumberMessage();
  }

  private void updateButtonVisibility() {
    this.forwardButton.visible = this.currentPage < BOOK_MAX_PAGES - 1;
    this.backButton.visible = this.currentPage > 0;
  }

  private void eraseEmptyTrailingPages() {
    var listiterator = this.pages.listIterator(this.pages.size());
    while (listiterator.hasPrevious() && listiterator.previous().isEmpty()) {
      listiterator.remove();
    }
  }

  private void saveChanges() {
    this.eraseEmptyTrailingPages();
    this.updateLocalCopy();
    ClientPacketDistributor.sendToServer(new EditRoutingTableBookMessage(this.hand, this.pages,
        Optional.empty()));
  }

  private void updateLocalCopy() {
    this.book.set(RailcraftDataComponents.ROUTING_TABLE_BOOK,
        new RoutingTableBookContent(this.pages,
            this.owner.getGameProfile().getName(), Optional.empty()));
  }

  private void appendPageToBook() {
    if (this.getNumPages() < BOOK_MAX_PAGES) {
      this.pages.add("");
    }
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    switch (keyCode) {
      case 266:
        this.backButton.onPress();
        return true;
      case 267:
        this.forwardButton.onPress();
        return true;
      default:
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    super.render(guiGraphics, mouseX, mouseY, partialTicks);
    int xOffset = (this.width - IMAGE_WIDTH) / 2;
    int yOffset = (this.height - IMAGE_HEIGHT) / 2;
    int l = this.font.width(this.numberOfPages);
    guiGraphics.drawString(this.font, this.numberOfPages, xOffset - l + 225, yOffset + 15,
        -16777216, false);
  }

  @Override
  public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    this.renderTransparentBackground(guiGraphics);
    int xOffset = (this.width - IMAGE_WIDTH) / 2;
    int yOffset = (this.height - IMAGE_HEIGHT) / 2;
    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BOOK_LOCATION, xOffset, yOffset, 0, 0, IMAGE_WIDTH,
        IMAGE_HEIGHT, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
  }
}
