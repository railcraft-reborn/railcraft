package mods.railcraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public record TagFilterComponent(Holder<Item> prototype, TagKey<Item> tag) {

  public static final Codec<TagFilterComponent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("prototype")
              .forGetter(TagFilterComponent::prototype),
          TagKey.codec(Registries.ITEM).fieldOf("tag")
              .forGetter(TagFilterComponent::tag)
      ).apply(instance, TagFilterComponent::new));

  public static final StreamCodec<RegistryFriendlyByteBuf, TagFilterComponent> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.holderRegistry(Registries.ITEM), TagFilterComponent::prototype,
          TagKey.streamCodec(Registries.ITEM), TagFilterComponent::tag,
          TagFilterComponent::new);
}
