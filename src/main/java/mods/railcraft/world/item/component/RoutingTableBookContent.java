package mods.railcraft.world.item.component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mods.railcraft.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public record RoutingTableBookContent(
    List<String> pages,
    String author,
    Optional<String> title)
    implements TooltipProvider {

  public static final RoutingTableBookContent EMPTY =
      new RoutingTableBookContent(List.of(), "", Optional.empty());
  private static final int PAGE_EDIT_LENGTH = 1024;
  private static final int MAX_PAGES = 50;
  private static final Codec<String> PAGE_CODEC = Codec.string(0, PAGE_EDIT_LENGTH);
  private static final Codec<List<String>> PAGES_CODEC =
      PAGE_CODEC.sizeLimitedListOf(MAX_PAGES);
  public static final Codec<RoutingTableBookContent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          PAGES_CODEC.fieldOf("pages").forGetter(RoutingTableBookContent::pages),
          Codec.STRING.fieldOf("author").forGetter(RoutingTableBookContent::author),
          Codec.STRING.optionalFieldOf("title").forGetter(RoutingTableBookContent::title)
      ).apply(instance, RoutingTableBookContent::new));
  public static final StreamCodec<ByteBuf, RoutingTableBookContent> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.stringUtf8(PAGE_EDIT_LENGTH).apply(ByteBufCodecs.list(MAX_PAGES)), RoutingTableBookContent::pages,
          ByteBufCodecs.STRING_UTF8, RoutingTableBookContent::author,
          ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), RoutingTableBookContent::title,
          RoutingTableBookContent::new);

  public RoutingTableBookContent(List<String> pages,
      String author, Optional<String> title) {
    if (pages.size() > MAX_PAGES) {
      throw new IllegalArgumentException("Got " + pages.size() + " pages, but maximum is " + MAX_PAGES);
    } else {
      this.pages = pages;
      this.author = author;
      this.title = title;
    }
  }

  @Override
  public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer,
      TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
    if (!author.isEmpty()) {
      consumer.accept(Component.translatable(Translations.Tips.ROUTING_TABLE_BOOK_LAST_EDIT, author)
          .withStyle(ChatFormatting.GRAY));
    }
  }
}
