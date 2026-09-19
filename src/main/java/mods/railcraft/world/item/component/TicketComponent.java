package mods.railcraft.world.item.component;

import java.util.Optional;
import org.jspecify.annotations.Nullable;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.RailcraftCodecs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.players.NameAndId;

public record TicketComponent(String destination, Optional<NameAndId> owner) {

  public TicketComponent(String destination, @Nullable NameAndId owner) {
    this(destination, Optional.ofNullable(owner));
  }

  public static final Codec<TicketComponent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          Codec.STRING.fieldOf(CompoundTagKeys.DESTINATION).forGetter(TicketComponent::destination),
          NameAndId.CODEC.optionalFieldOf(CompoundTagKeys.OWNER).forGetter(TicketComponent::owner)
      ).apply(instance, TicketComponent::new));

  public static final StreamCodec<FriendlyByteBuf, TicketComponent> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.STRING_UTF8, TicketComponent::destination,
          ByteBufCodecs.optional(RailcraftCodecs.NAME_AND_ID), TicketComponent::owner,
          TicketComponent::new);
}
