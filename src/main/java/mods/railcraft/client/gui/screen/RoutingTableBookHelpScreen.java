package mods.railcraft.client.gui.screen;

import java.util.List;
import com.google.common.collect.Lists;
import mods.railcraft.Translations;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.RailcraftButton;
import mods.railcraft.client.gui.widget.button.RailcraftPageButton;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class RoutingTableBookHelpScreen extends Screen {

  private final RoutingTableBookScreen routingTableBookScreen;

  private int currentPage;
  private final List<String> pages = Lists.newArrayList();
  private RailcraftPageButton forwardButton;
  private RailcraftPageButton backButton;
  private Component numberOfPages;
  private MultiLineEditBox page;

  private RailcraftButton helpBackButton;

  public RoutingTableBookHelpScreen(RoutingTableBookScreen routingTableBookScreen) {
    super(GameNarrator.NO_TITLE);
    this.routingTableBookScreen = routingTableBookScreen;
    this.numberOfPages = CommonComponents.EMPTY;

    Translations.RoutingTable.MANUAL_PAGES.forEach(s -> {
      this.pages.add(Component.translatable(s).getString());
    });
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
    int xOffset = (this.width - RoutingTableBookScreen.IMAGE_WIDTH) / 2;
    int yOffset = (this.height - RoutingTableBookScreen.IMAGE_HEIGHT) / 2;

    this.page = MultiLineEditBox.builder()
        .setShowDecorations(false)
        .setTextColor(-16777216)
        .setCursorColor(-16777216)
        .setShowBackground(false)
        .setTextShadow(false)
        .setX((this.width - RoutingTableBookScreen.TEXT_WIDTH) / 2 - 7)
        .setY((this.height - RoutingTableBookScreen.TEXT_HEIGHT) / 2 - 10)
        .build(this.font, RoutingTableBookScreen.TEXT_WIDTH, 150, CommonComponents.EMPTY);

    this.page.setCharacterLimit(1024);
    this.page.setLineLimit(14);
    this.page.setValueListener((s) -> this.pages.set(this.currentPage, s));
    this.page.active = false;
    this.addRenderableWidget(this.page);
    this.updatePageContent();
    this.numberOfPages = this.getPageNumberMessage();

    this.backButton = this.addRenderableWidget(
        new RailcraftPageButton(xOffset + 30, yOffset + 150, false,
            RoutingTableBookScreen.BOOK_LOCATION, button -> this.pageBack()));
    this.forwardButton = this.addRenderableWidget(
        new RailcraftPageButton(xOffset + 200, yOffset + 150, true,
            RoutingTableBookScreen.BOOK_LOCATION, button -> this.pageForward()));
    this.helpBackButton = this.addRenderableWidget(RailcraftButton
        .builder(CommonComponents.GUI_BACK, button -> {
          this.minecraft.setScreen(this.routingTableBookScreen);
        }, ButtonTexture.LARGE_BUTTON)
        .size(64, 20)
        .build());
    var layout = new LinearLayout(this.width / 2 - 100, this.height / 2 + 90,
        LinearLayout.Orientation.HORIZONTAL);
    layout.spacing(4);
    layout.addChild(this.helpBackButton);
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
    }
    this.updatePageContent();
    this.updateButtonVisibility();
  }

  private void updatePageContent() {
    this.page.setValue(this.pages.get(this.currentPage), true);
    this.numberOfPages = this.getPageNumberMessage();
  }

  private void updateButtonVisibility() {
    this.forwardButton.visible = this.currentPage < this.getNumPages() - 1;
    this.backButton.visible = this.currentPage > 0;
  }

  @Override
  public boolean keyPressed(KeyEvent event) {
    switch (event.key()) {
      case 266:
        this.backButton.onPress(event);
        return true;
      case 267:
        this.forwardButton.onPress(event);
        return true;
      default:
        return super.keyPressed(event);
    }
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    super.render(guiGraphics, mouseX, mouseY, partialTicks);
    int xOffset = (this.width - RoutingTableBookScreen.IMAGE_WIDTH) / 2;
    int yOffset = (this.height - RoutingTableBookScreen.IMAGE_HEIGHT) / 2;
    int l = this.font.width(this.numberOfPages);
    guiGraphics.drawString(this.font, this.numberOfPages, xOffset - l + 225, yOffset + 15,
        -16777216, false);
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
