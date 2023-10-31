package mods.railcraft.client.gui.screen.inventory;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import mods.railcraft.Railcraft;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.RailcraftButton;
import mods.railcraft.client.gui.widget.button.RailcraftPageButton;
import mods.railcraft.client.util.GuiUtil;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class LogBookScreen extends Screen {

  private static final int TEXT_WIDTH = 220;
  private static final ResourceLocation BOOK_LOCATION =
      Railcraft.rl("textures/gui/block/logbook.png");
  private static final int IMAGE_WIDTH = 256;
  private static final int IMAGE_HEIGHT = 181;
  private final List<String> pages = Lists.newArrayList();
  private int currentPage;
  private PageButton forwardButton, backButton;

  @Nullable
  private DisplayCache displayCache = DisplayCache.EMPTY;
  private Component pageMsg = CommonComponents.EMPTY;


  public LogBookScreen(List<List<String>> pages) {
    super(GameNarrator.NO_TITLE);
    if (pages.isEmpty()) {
      this.pages.add("");
    } else {
      for (var page : pages) {
        this.pages.add(String.join("\n", page));
      }
    }
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
    super.init();
    this.clearDisplayCache();
    var buttons = List.of(
        RailcraftButton
            .builder(CommonComponents.GUI_DONE, button -> {
              this.minecraft.setScreen(null);
            }, ButtonTexture.LARGE_BUTTON)
            .pos(0, this.height / 2 + 90)
            .size(65, 20)
            .build());
    GuiUtil.newButtonRowAuto(this::addRenderableWidget, this.width / 2 - 100, 200, buttons);

    int xOffset = (this.width - IMAGE_WIDTH) / 2;
    int yOffset = (this.height - IMAGE_HEIGHT) / 2;
    forwardButton = this.addRenderableWidget(
        new RailcraftPageButton(xOffset + 200, yOffset + 150, true, BOOK_LOCATION, button -> {
          this.pageForward();
        })
    );
    backButton = this.addRenderableWidget(
        new RailcraftPageButton(xOffset + 30, yOffset + 150, false, BOOK_LOCATION, button -> {
          this.pageBack();
        })
    );
  }

  private void pageForward() {
    if (this.currentPage < this.getNumPages() - 1) {
      this.currentPage++;
    }
    this.updateButtonVisibility();
    this.clearDisplayCache();
  }

  private void pageBack() {
    if (this.currentPage > 0) {
      this.currentPage--;
    }
    this.updateButtonVisibility();
    this.clearDisplayCache();
  }

  private void updateButtonVisibility() {
    this.forwardButton.visible = this.currentPage < this.getNumPages() - 1;
    this.backButton.visible = this.currentPage > 0;
  }

  private String getCurrentPageText() {
    return this.currentPage >= 0 && this.currentPage < this.pages.size() ? this.pages.get(
        this.currentPage) : "";
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
    this.setFocused(null);
    RenderSystem.setShaderTexture(0, BOOK_LOCATION);
    int xOffset = (this.width - IMAGE_WIDTH) / 2;
    int yOffset = (this.height - IMAGE_HEIGHT) / 2;
    guiGraphics.blit(BOOK_LOCATION, xOffset, yOffset, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

    int l = this.font.width(this.pageMsg);
    guiGraphics.drawString(this.font, this.pageMsg, xOffset - l + 225, yOffset + 15, 0, false);
    var displayCache = this.getDisplayCache();
    for (var lineinfo : displayCache.lines) {
      guiGraphics.drawString(this.font, lineinfo.asComponent, lineinfo.x, lineinfo.y,
          -16777216, false);
    }
    this.updateButtonVisibility();
    super.render(guiGraphics, mouseX, mouseY, partialTicks);
  }

  private DisplayCache getDisplayCache() {
    if (this.displayCache == null) {
      this.displayCache = this.rebuildDisplayCache();
      this.pageMsg = Component.translatable("book.pageIndicator",
          this.currentPage + 1, this.getNumPages());
    }
    return this.displayCache;
  }

  private DisplayCache rebuildDisplayCache() {
    String s = this.getCurrentPageText();
    if (s.isEmpty()) {
      return DisplayCache.EMPTY;
    } else {
      List<LineInfo> list = Lists.newArrayList();
      MutableInt mutableint = new MutableInt();
      MutableBoolean mutableboolean = new MutableBoolean();
      StringSplitter stringsplitter = this.font.getSplitter();
      stringsplitter.splitLines(s, TEXT_WIDTH, Style.EMPTY, true,
          (style, beginIndex, endIndex) -> {
            int k3 = mutableint.getAndIncrement();
            String s2 = s.substring(beginIndex, endIndex);
            mutableboolean.setValue(s2.endsWith("\n"));
            String s3 = StringUtils.stripEnd(s2, " \n");
            int l3 = k3 * 9;
            var pos = this.convertLocalToScreen(new Pos2i(0, l3));
            list.add(new LineInfo(style, s3, pos.x, pos.y));
          });
      return new DisplayCache(list.toArray(new LineInfo[0]));
    }
  }

  private Pos2i convertLocalToScreen(Pos2i localScreenPos) {
    int xOffset = (this.width - IMAGE_WIDTH) / 2;
    int yOffset = (this.height - IMAGE_HEIGHT) / 2;
    int x = localScreenPos.x + xOffset + 20;
    int y = localScreenPos.y + yOffset + 27;
    return new Pos2i(x, y);
  }

  private void clearDisplayCache() {
    this.displayCache = null;
  }

  record DisplayCache(LineInfo[] lines) {

    static final DisplayCache EMPTY =
        new DisplayCache(new LineInfo[]{new LineInfo(Style.EMPTY, "", 0, 0)});
  }

  static class LineInfo {

    final Style style;
    final String contents;
    final Component asComponent;
    final int x;
    final int y;

    public LineInfo(Style style, String contents, int x, int y) {
      this.style = style;
      this.contents = contents;
      this.x = x;
      this.y = y;
      this.asComponent = Component.literal(contents).setStyle(style);
    }
  }

  record Pos2i(int x, int y) {

  }
}
