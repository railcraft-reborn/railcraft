package mods.railcraft.client.gui.screen;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import mods.railcraft.Railcraft;
import mods.railcraft.Translations;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.RailcraftButton;
import mods.railcraft.client.gui.widget.button.RailcraftPageButton;
import mods.railcraft.client.util.GuiUtil;
import mods.railcraft.network.PacketHandler;
import mods.railcraft.network.to_server.EditRoutingTableBookMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RoutingTableBookScreen extends Screen {

  public static final ResourceLocation BOOK_LOCATION =
      Railcraft.rl("textures/gui/item/routing_table_book.png");
  private static final int TEXT_WIDTH = 220;
  private static final int TEXT_HEIGHT = 128;
  private static final int IMAGE_WIDTH = 256;
  private static final int IMAGE_HEIGHT = 192;

  private static final int BOOK_MAX_PAGES = 50;

  private static final Component EDIT_TITLE_LABEL = Component.translatable("book.editTitle");
  private static final FormattedCharSequence BLACK_CURSOR = FormattedCharSequence.forward("_",
      Style.EMPTY.withColor(ChatFormatting.BLACK));
  private static final FormattedCharSequence GRAY_CURSOR = FormattedCharSequence.forward("_",
      Style.EMPTY.withColor(ChatFormatting.GRAY));

  private final Player owner;
  private final ItemStack book;
  private final InteractionHand hand;

  /** Whether the book's title or contents has been modified since being opened */
  private boolean isModified;
  /** If I am changing the title of the book */
  private boolean editingTitle;
  /** Update ticks since the gui was opened */
  private int frameTick;
  private int currentPage;
  private final List<String> pages = Lists.newArrayList();
  private String title = "";

  private final TextFieldHelper pageEdit = new TextFieldHelper(this::getCurrentPageText,
      this::setCurrentPageText, this::getClipboard, this::setClipboard, s ->
      s.length() < 1024 && this.font.wordWrapHeight(s, TEXT_WIDTH) <= TEXT_HEIGHT);

  private final TextFieldHelper titleEdit = new TextFieldHelper(() -> this.title,
      s -> this.title = s, this::getClipboard, this::setClipboard, s -> s.length() < 16);

  private long lastClickTime;
  private int lastIndex = -1;

  private PageButton forwardButton, backButton;
  private RailcraftButton titleButton, helpButton;

  @Nullable
  private DisplayCache displayCache = DisplayCache.EMPTY;
  private Component pageMsg = CommonComponents.EMPTY;
  private final Component ownerText;
  /** If I am reading the manual */
  private boolean readingManual;

  private final int numManualPages;


  public RoutingTableBookScreen(Player owner, ItemStack book, InteractionHand hand) {
    super(GameNarrator.NO_TITLE);
    this.owner = owner;
    this.book = book;
    this.hand = hand;
    this.ownerText = Component.translatable("book.byAuthor", owner.getName())
        .withStyle(ChatFormatting.DARK_GRAY);

    var tag = book.getTag();
    if (tag != null) {
      BookViewScreen.loadPages(tag, this.pages::add);
    }
    if (this.pages.isEmpty()) {
      this.pages.add("");
    }
    this.numManualPages = Translations.RoutingTable.MANUAL_PAGES.size();
  }

  private void setClipboard(String s) {
    if (this.minecraft != null) {
      TextFieldHelper.setClipboardContents(this.minecraft, s);
    }
  }

  private String getClipboard() {
    return this.minecraft != null ? TextFieldHelper.getClipboardContents(this.minecraft) : "";
  }

  private int getNumPages() {
    return this.pages.size();
  }

  @Override
  public void tick() {
    super.tick();
    this.frameTick++;
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }

  @Override
  protected void init() {
    this.clearDisplayCache();
    var buttons = List.of(
        this.titleButton = RailcraftButton
            .builder(Translations.Screen.NAME, button -> {
              this.editingTitle = !this.editingTitle;
              this.readingManual = false;
              currentPage = 0;
              this.updateButtonVisibility();
              this.clearDisplayCacheAfterPageChange();
            }, ButtonTexture.LARGE_BUTTON)
            .pos(0, this.height / 2 + 90)
            .size(65, 20)
            .build(),
        this.helpButton = RailcraftButton
            .builder(Translations.Screen.HELP, button -> {
              readingManual = !readingManual;
              editingTitle = false;
              currentPage = 0;
              this.clearDisplayCacheAfterPageChange();
            }, ButtonTexture.LARGE_BUTTON)
            .pos(0, this.height / 2 + 90)
            .size(65, 20)
            .build(),
        RailcraftButton
            .builder(CommonComponents.GUI_DONE, button -> {
              this.saveChanges();
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
    this.updateButtonVisibility();
  }

  private void pageForward() {
    if (this.readingManual) {
      if (this.currentPage < this.getMaxPages() - 1) {
        this.currentPage++;
      }
      return;
    }
    if (this.currentPage < this.getNumPages() - 1) {
      this.currentPage++;
    } else {
      this.appendPageToBook();
      if (this.currentPage < this.getNumPages() - 1) {
        this.currentPage++;
      }
    }
    this.updateButtonVisibility();
    this.clearDisplayCacheAfterPageChange();
  }

  private void pageBack() {
    if (this.currentPage > 0) {
      this.currentPage--;
    }
    if (!this.readingManual) {
      this.updateButtonVisibility();
      this.clearDisplayCacheAfterPageChange();
    }
  }

  private void updateButtonVisibility() {
    this.forwardButton.visible = !this.editingTitle && this.currentPage < this.getMaxPages() - 1;
    this.backButton.visible = !this.editingTitle && this.currentPage > 0;

    helpButton.setMessage(readingManual ? CommonComponents.GUI_BACK :
        Component.translatable(Translations.Screen.HELP));
    titleButton.setMessage(editingTitle ? CommonComponents.GUI_BACK :
        Component.translatable(Translations.Screen.NAME));
  }

  private int getMaxPages() {
    if (readingManual)
      return numManualPages;
    if (editingTitle)
      return 0;
    return BOOK_MAX_PAGES;
  }

  private void eraseEmptyTrailingPages() {
    var listiterator = this.pages.listIterator(this.pages.size());
    while(listiterator.hasPrevious() && listiterator.previous().isEmpty()) {
      listiterator.remove();
    }
  }

  private void saveChanges() {
    if (this.isModified) {
      this.eraseEmptyTrailingPages();
      this.updateLocalCopy();
      PacketHandler.sendToServer(new EditRoutingTableBookMessage(this.hand, this.pages,
          Optional.of(this.title.trim())));
    }
  }

  private void updateLocalCopy() {
    if (!this.pages.isEmpty()) {
      var listtag = new ListTag();
      this.pages.stream().map(StringTag::valueOf).forEach(listtag::add);
      this.book.addTagElement("pages", listtag);
    }
    this.book.addTagElement("author", StringTag.valueOf(this.owner.getGameProfile().getName()));
    this.book.addTagElement("title", StringTag.valueOf(this.title.trim()));
  }

  private void appendPageToBook() {
    if (this.getNumPages() < BOOK_MAX_PAGES) {
      this.pages.add("");
      this.isModified = true;
    }
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (super.keyPressed(keyCode, scanCode, modifiers)) {
      return true;
    } else if (this.editingTitle) {
      return this.titleKeyPressed(keyCode, scanCode, modifiers);
    } else {
      if (this.bookKeyPressed(keyCode, scanCode, modifiers)) {
        this.clearDisplayCache();
        return true;
      } else {
        return false;
      }
    }
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    if (super.charTyped(codePoint, modifiers)) {
      return true;
    } else if (this.editingTitle) {
      if (this.titleEdit.charTyped(codePoint)) {
        this.updateButtonVisibility();
        this.isModified = true;
        return true;
      } else {
        return false;
      }
    } else if (SharedConstants.isAllowedChatCharacter(codePoint)) {
      this.pageEdit.insertText(Character.toString(codePoint));
      this.clearDisplayCache();
      return true;
    } else {
      return false;
    }
  }

  private boolean bookKeyPressed(int keyCode, int scanCode, int modifiers) {
    if (Screen.isSelectAll(keyCode)) {
      this.pageEdit.selectAll();
      return true;
    } else if (Screen.isCopy(keyCode)) {
      this.pageEdit.copy();
      return true;
    } else if (Screen.isPaste(keyCode)) {
      this.pageEdit.paste();
      return true;
    } else if (Screen.isCut(keyCode)) {
      this.pageEdit.cut();
      return true;
    } else {
      var cursorStep = Screen.hasControlDown()
          ? TextFieldHelper.CursorStep.WORD
          : TextFieldHelper.CursorStep.CHARACTER;
      switch (keyCode) {
        case 257:
        case 335:
          this.pageEdit.insertText("\n");
          return true;
        case 259:
          this.pageEdit.removeFromCursor(-1, cursorStep);
          return true;
        case 261:
          this.pageEdit.removeFromCursor(1, cursorStep);
          return true;
        case 262:
          this.pageEdit.moveBy(1, Screen.hasShiftDown(), cursorStep);
          return true;
        case 263:
          this.pageEdit.moveBy(-1, Screen.hasShiftDown(), cursorStep);
          return true;
        case 264:
          this.keyDown();
          return true;
        case 265:
          this.keyUp();
          return true;
        case 266:
          this.backButton.onPress();
          return true;
        case 267:
          this.forwardButton.onPress();
          return true;
        case 268:
          this.keyHome();
          return true;
        case 269:
          this.keyEnd();
          return true;
        default:
          return false;
      }
    }
  }

  private void keyUp() {
    this.changeLine(-1);
  }

  private void keyDown() {
    this.changeLine(1);
  }

  private void changeLine(int yChange) {
    int i = this.pageEdit.getCursorPos();
    int j = this.getDisplayCache().changeLine(i, yChange);
    this.pageEdit.setCursorPos(j, Screen.hasShiftDown());
  }

  private void keyHome() {
    if (Screen.hasControlDown()) {
      this.pageEdit.setCursorToStart(Screen.hasShiftDown());
    } else {
      int i = this.pageEdit.getCursorPos();
      int j = this.getDisplayCache().findLineStart(i);
      this.pageEdit.setCursorPos(j, Screen.hasShiftDown());
    }
  }

  private void keyEnd() {
    if (Screen.hasControlDown()) {
      this.pageEdit.setCursorToEnd(Screen.hasShiftDown());
    } else {
      var displayCache = this.getDisplayCache();
      int i = this.pageEdit.getCursorPos();
      int j = displayCache.findLineEnd(i);
      this.pageEdit.setCursorPos(j, Screen.hasShiftDown());
    }
  }

  private boolean titleKeyPressed(int keyCode, int pScanCode, int pModifiers) {
    switch (keyCode) {
      case 257, 335 -> {
        if (!this.title.isEmpty()) {
          this.saveChanges();
          this.minecraft.setScreen(null);
        }
        return true;
      }
      case 259 -> {
        this.titleEdit.removeCharsFromCursor(-1);
        this.updateButtonVisibility();
        this.isModified = true;
        return true;
      }
      default -> {
        return false;
      }
    }
  }

  private String getCurrentPageText() {
    return this.currentPage >= 0 && this.currentPage < this.pages.size() ? this.pages.get(this.currentPage) : "";
  }

  private void setCurrentPageText(String text) {
    if (this.currentPage >= 0 && this.currentPage < this.pages.size()) {
      this.pages.set(this.currentPage, text);
      this.isModified = true;
      this.clearDisplayCache();
    }
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    super.render(guiGraphics, mouseX, mouseY, partialTicks);
    this.setFocused(null);
    RenderSystem.setShaderTexture(0, BOOK_LOCATION);
    int xOffset = (this.width - IMAGE_WIDTH) / 2;
    int yOffset = (this.height - IMAGE_HEIGHT) / 2;
    guiGraphics.blit(BOOK_LOCATION, xOffset, yOffset, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    if (this.editingTitle) {
      boolean flag = this.frameTick / 6 % 2 == 0;
      var formattedcharsequence = FormattedCharSequence.composite(
          FormattedCharSequence.forward(this.title, Style.EMPTY),
          flag ? BLACK_CURSOR : GRAY_CURSOR);
      int l = this.font.width(EDIT_TITLE_LABEL);
      guiGraphics.drawString(this.font, EDIT_TITLE_LABEL, xOffset + 160 - l,
          yOffset + 34, 0, false);
      int l1 = this.font.width(formattedcharsequence);
      guiGraphics.drawString(this.font, formattedcharsequence, xOffset + 120 - l1 / 2,
          yOffset + 50, 0, false);
      int l2 = this.font.width(this.ownerText);
      guiGraphics.drawString(this.font, this.ownerText, xOffset + 130 - l2, yOffset + 60, 0, false);
    } else if (this.readingManual) {
      var manualPageIndicator = Component.translatable("book.pageIndicator", this.currentPage + 1,
          this.getMaxPages());
      int l = this.font.width(manualPageIndicator);
      guiGraphics.drawString(this.font, manualPageIndicator, xOffset - l + 225,
          yOffset + 15, 0, false);
      var page = Component.translatable(Translations.RoutingTable.MANUAL_PAGES.get(currentPage));
      guiGraphics.drawWordWrap(this.font, page, xOffset + 20,
          yOffset + 27, TEXT_WIDTH, IngameWindowScreen.TEXT_COLOR);
    } else {
      int l = this.font.width(this.pageMsg);
      guiGraphics.drawString(this.font, this.pageMsg, xOffset - l + 225, yOffset + 15, 0, false);
      var displayCache = this.getDisplayCache();
      for(var lineinfo : displayCache.lines) {
        guiGraphics.drawString(this.font, lineinfo.asComponent, lineinfo.x, lineinfo.y,
            -16777216, false);
      }
      this.renderHighlight(guiGraphics, displayCache.selection);
      this.renderCursor(guiGraphics, displayCache.cursor, displayCache.cursorAtEnd);
    }
    this.updateButtonVisibility();
  }

  private void renderCursor(GuiGraphics guiGraphics, Pos2i cursorPos, boolean isEndOfText) {
    if (this.frameTick / 6 % 2 == 0) {
      cursorPos = this.convertLocalToScreen(cursorPos);
      if (!isEndOfText) {
        guiGraphics.fill(cursorPos.x, cursorPos.y - 1, cursorPos.x + 1, cursorPos.y + 9, -16777216);
      } else {
        guiGraphics.drawString(this.font, "_", cursorPos.x, cursorPos.y, 0, false);
      }
    }
  }

  private void renderHighlight(GuiGraphics guiGraphics, Rect2i[] selected) {
    for(var rect2i : selected) {
      int i = rect2i.getX();
      int j = rect2i.getY();
      int k = i + rect2i.getWidth();
      int l = j + rect2i.getHeight();
      guiGraphics.fill(RenderType.guiTextHighlight(), i, j, k, l, -16776961);
    }
  }

  private Pos2i convertScreenToLocal(Pos2i screenPos) {
    int xOffset = (this.width - IMAGE_WIDTH) / 2;
    int yOffset = (this.height - IMAGE_HEIGHT) / 2;
    int x = screenPos.x - xOffset - 20;
    int y = screenPos.y - yOffset - 27;
    return new Pos2i(x, y);
  }

  private Pos2i convertLocalToScreen(Pos2i localScreenPos) {
    int xOffset = (this.width - IMAGE_WIDTH) / 2;
    int yOffset = (this.height - IMAGE_HEIGHT) / 2;
    int x = localScreenPos.x + xOffset + 20;
    int y = localScreenPos.y + yOffset + 27;
    return new Pos2i(x, y);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (super.mouseClicked(mouseX, mouseY, button)) {
      return true;
    } else {
      if (button == 0) {
        long i = Util.getMillis();
        var displayCache = this.getDisplayCache();
        int j = displayCache.getIndexAtPosition(this.font,
            this.convertScreenToLocal(new Pos2i((int)mouseX, (int)mouseY)));
        if (j >= 0) {
          if (j == this.lastIndex && i - this.lastClickTime < 250L) {
            if (!this.pageEdit.isSelecting()) {
              this.selectWord(j);
            } else {
              this.pageEdit.selectAll();
            }
          } else {
            this.pageEdit.setCursorPos(j, Screen.hasShiftDown());
          }
          this.clearDisplayCache();
        }
        this.lastIndex = j;
        this.lastClickTime = i;
      }
      return true;
    }
  }

  private void selectWord(int pIndex) {
    String s = this.getCurrentPageText();
    this.pageEdit.setSelectionRange(StringSplitter.getWordPosition(s, -1, pIndex, false),
        StringSplitter.getWordPosition(s, 1, pIndex, false));
  }

  public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
    if (super.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
      return true;
    } else {
      if (button == 0) {
        var displayCache = this.getDisplayCache();
        int i = displayCache.getIndexAtPosition(this.font,
            this.convertScreenToLocal(new Pos2i((int) mouseX, (int) mouseY)));
        this.pageEdit.setCursorPos(i, true);
        this.clearDisplayCache();
      }
      return true;
    }
  }

  private DisplayCache getDisplayCache() {
    if (this.displayCache == null) {
      this.displayCache = this.rebuildDisplayCache();
      this.pageMsg = Component.translatable("book.pageIndicator",
          this.currentPage + 1, this.getNumPages());
    }
    return this.displayCache;
  }

  private void clearDisplayCache() {
    this.displayCache = null;
  }

  private void clearDisplayCacheAfterPageChange() {
    this.pageEdit.setCursorToEnd();
    this.clearDisplayCache();
  }

  private DisplayCache rebuildDisplayCache() {
    String s = this.getCurrentPageText();
    if (s.isEmpty()) {
      return DisplayCache.EMPTY;
    } else {
      int i = this.pageEdit.getCursorPos();
      int j = this.pageEdit.getSelectionPos();
      IntList intlist = new IntArrayList();
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
        intlist.add(beginIndex);
        list.add(new LineInfo(style, s3, pos.x, pos.y));
      });
      int[] aint = intlist.toIntArray();
      boolean flag = i == s.length();
      Pos2i pos;
      if (flag && mutableboolean.isTrue()) {
        pos = new Pos2i(0, list.size() * 9);
      } else {
        int k = findLineFromPos(aint, i);
        int l = this.font.width(s.substring(aint[k], i));
        pos = new Pos2i(l, k * 9);
      }

      List<Rect2i> list1 = Lists.newArrayList();
      if (i != j) {
        int l2 = Math.min(i, j);
        int i1 = Math.max(i, j);
        int j1 = findLineFromPos(aint, l2);
        int k1 = findLineFromPos(aint, i1);
        if (j1 == k1) {
          int l1 = j1 * 9;
          int i2 = aint[j1];
          list1.add(this.createPartialLineSelection(s, stringsplitter, l2, i1, l1, i2));
        } else {
          int i3 = j1 + 1 > aint.length ? s.length() : aint[j1 + 1];
          list1.add(this.createPartialLineSelection(s, stringsplitter, l2, i3, j1 * 9, aint[j1]));
          for(int j3 = j1 + 1; j3 < k1; ++j3) {
            int j2 = j3 * 9;
            String s1 = s.substring(aint[j3], aint[j3 + 1]);
            int k2 = (int)stringsplitter.stringWidth(s1);
            list1.add(this.createSelection(new Pos2i(0, j2), new Pos2i(k2, j2 + 9)));
          }
          list1.add(this.createPartialLineSelection(s, stringsplitter, aint[k1], i1, k1 * 9, aint[k1]));
        }
      }
      return new DisplayCache(s, pos, flag, aint, list.toArray(new LineInfo[0]),
          list1.toArray(new Rect2i[0]));
    }
  }

  static int findLineFromPos(int[] lineStarts, int find) {
    int i = Arrays.binarySearch(lineStarts, find);
    return i < 0 ? -(i + 2) : i;
  }


  private Rect2i createPartialLineSelection(String input, StringSplitter splitter, int p_98122_,
      int p_98123_, int p_98124_, int p_98125_) {
    String s = input.substring(p_98125_, p_98122_);
    String s1 = input.substring(p_98125_, p_98123_);
    var corner1 = new Pos2i((int)splitter.stringWidth(s), p_98124_);
    var corner2 = new Pos2i((int)splitter.stringWidth(s1), p_98124_ + 9);
    return this.createSelection(corner1, corner2);
  }

  private Rect2i createSelection(Pos2i pCorner1, Pos2i pCorner2) {
    var pos = this.convertLocalToScreen(pCorner1);
    var pos1 = this.convertLocalToScreen(pCorner2);
    int i = Math.min(pos.x, pos1.x);
    int j = Math.max(pos.x, pos1.x);
    int k = Math.min(pos.y, pos1.y);
    int l = Math.max(pos.y, pos1.y);
    return new Rect2i(i, k, j - i, l - k);
  }

  static class DisplayCache {
    static final DisplayCache EMPTY = new DisplayCache("", new Pos2i(0, 0), true,
        new int[]{0}, new LineInfo[]{new LineInfo(Style.EMPTY, "", 0, 0)}, new Rect2i[0]);
    private final String fullText;
    final Pos2i cursor;
    final boolean cursorAtEnd;
    private final int[] lineStarts;
    final LineInfo[] lines;
    final Rect2i[] selection;

    public DisplayCache(String fullText, Pos2i cursor, boolean cursorAtEnd, int[] lineStarts,
        LineInfo[] lines, Rect2i[] selection) {
      this.fullText = fullText;
      this.cursor = cursor;
      this.cursorAtEnd = cursorAtEnd;
      this.lineStarts = lineStarts;
      this.lines = lines;
      this.selection = selection;
    }

    public int getIndexAtPosition(Font font, Pos2i cursorPosition) {
      int i = cursorPosition.y / 9;
      if (i < 0) {
        return 0;
      } else if (i >= this.lines.length) {
        return this.fullText.length();
      } else {
        return this.lineStarts[i] + font.getSplitter()
            .plainIndexAtWidth(this.lines[i].contents, cursorPosition.x, this.lines[i].style);
      }
    }

    public int changeLine(int xChange, int yChange) {
      int i = RoutingTableBookScreen.findLineFromPos(this.lineStarts, xChange);
      int j = i + yChange;
      int k;
      if (0 <= j && j < this.lineStarts.length) {
        int l = xChange - this.lineStarts[i];
        int i1 = this.lines[j].contents.length();
        k = this.lineStarts[j] + Math.min(l, i1);
      } else {
        k = xChange;
      }

      return k;
    }

    public int findLineStart(int line) {
      int i = RoutingTableBookScreen.findLineFromPos(this.lineStarts, line);
      return this.lineStarts[i];
    }

    public int findLineEnd(int line) {
      int i = RoutingTableBookScreen.findLineFromPos(this.lineStarts, line);
      return this.lineStarts[i] + this.lines[i].contents.length();
    }
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

  record Pos2i(int x, int y) {}
}
