package mods.railcraft.network.play;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import com.google.common.collect.Lists;
import mods.railcraft.world.item.RoutingTableBookItem;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.NetworkEvent;

public record EditRoutingTableBookMessage(InteractionHand hand, List<String> pages,
                                          Optional<String> title) {
  private static final int BOOK_MAX_PAGES = 50;

  public void encode(FriendlyByteBuf out) {
    out.writeEnum(this.hand);
    out.writeCollection(this.pages, FriendlyByteBuf::writeUtf);
    out.writeOptional(this.title, FriendlyByteBuf::writeUtf);
  }

  public static EditRoutingTableBookMessage decode(FriendlyByteBuf in) {
    var hand = in.readEnum(InteractionHand.class);
    var pages = in.readCollection(FriendlyByteBuf
        .limitValue(Lists::newArrayListWithCapacity, BOOK_MAX_PAGES), FriendlyByteBuf::readUtf);
    var title = in.readOptional(FriendlyByteBuf::readUtf);
    return new EditRoutingTableBookMessage(hand, pages, title);
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    var player = context.get().getSender();
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
    return true;
  }
}
