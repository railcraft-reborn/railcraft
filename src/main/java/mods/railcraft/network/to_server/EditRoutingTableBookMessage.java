package mods.railcraft.network.to_server;

import java.util.List;
import java.util.Optional;
import com.google.common.collect.Lists;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.network.RailcraftCustomPacketPayload;
import mods.railcraft.world.item.RoutingTableBookItem;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record EditRoutingTableBookMessage(
    InteractionHand hand, List<String> pages,
    Optional<String> title) implements RailcraftCustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("edit_routing_table_book");
  private static final int BOOK_MAX_PAGES = 50;

  public static EditRoutingTableBookMessage read(FriendlyByteBuf buf) {
    var hand = buf.readEnum(InteractionHand.class);
    var pages = buf.readCollection(FriendlyByteBuf
        .limitValue(Lists::newArrayListWithCapacity, BOOK_MAX_PAGES), FriendlyByteBuf::readUtf);
    var title = buf.readOptional(FriendlyByteBuf::readUtf);
    return new EditRoutingTableBookMessage(hand, pages, title);
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeEnum(this.hand);
    buf.writeCollection(this.pages, FriendlyByteBuf::writeUtf);
    buf.writeOptional(this.title, FriendlyByteBuf::writeUtf);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext context) {
    context.player().ifPresent(player -> {
      var senderProfile = player.getGameProfile();
      var itemStack = player.getItemInHand(this.hand);
      if (itemStack.getItem() instanceof RoutingTableBookItem) {
        if (!this.pages.isEmpty()) {
          var listtag = new ListTag();
          this.pages.stream().map(StringTag::valueOf).forEach(listtag::add);
          itemStack.addTagElement("pages", listtag);
        }
        itemStack.addTagElement("author", StringTag.valueOf(senderProfile.getName()));
        itemStack.addTagElement("title", StringTag.valueOf(this.title.orElse("").trim()));
      }
    });
  }
}
