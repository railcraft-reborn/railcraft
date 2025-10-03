package mods.railcraft.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.players.NameAndId;

public class RailcraftCodecs {

  public static final StreamCodec<FriendlyByteBuf, NameAndId> NAME_AND_ID =
      StreamCodec.of((o, value) -> {
        o.writeUUID(value.id());
        o.writeUtf(value.name());
      }, friendlyByteBuf -> {
        var uuid = friendlyByteBuf.readUUID();
        var name = friendlyByteBuf.readUtf();
        return new NameAndId(uuid, name);
      });
}
