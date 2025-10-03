package mods.railcraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.RailcraftCodecs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.players.NameAndId;

public record LocomotiveOwnerComponent(NameAndId owner) {

  public static final Codec<LocomotiveOwnerComponent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          NameAndId.CODEC.fieldOf(CompoundTagKeys.OWNER).forGetter(LocomotiveOwnerComponent::owner)
      ).apply(instance, LocomotiveOwnerComponent::new));

  public static final StreamCodec<FriendlyByteBuf, LocomotiveOwnerComponent> STREAM_CODEC =
      StreamCodec.composite(
          RailcraftCodecs.NAME_AND_ID, LocomotiveOwnerComponent::owner,
          LocomotiveOwnerComponent::new
      );
}
