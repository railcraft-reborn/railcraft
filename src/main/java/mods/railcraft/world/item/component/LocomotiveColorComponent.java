package mods.railcraft.world.item.component;

import java.util.function.Consumer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.Translations;
import mods.railcraft.api.core.CompoundTagKeys;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public record LocomotiveColorComponent(DyeColor primary, DyeColor secondary)
    implements TooltipProvider {

  public static final Codec<LocomotiveColorComponent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          DyeColor.CODEC.fieldOf(CompoundTagKeys.PRIMARY_COLOR)
              .forGetter(LocomotiveColorComponent::primary),
          DyeColor.CODEC.fieldOf(CompoundTagKeys.SECONDARY_COLOR)
              .forGetter(LocomotiveColorComponent::secondary)
      ).apply(instance, LocomotiveColorComponent::new));

  public static final StreamCodec<FriendlyByteBuf, LocomotiveColorComponent> STREAM_CODEC =
      StreamCodec.composite(
          DyeColor.STREAM_CODEC, LocomotiveColorComponent::primary,
          DyeColor.STREAM_CODEC, LocomotiveColorComponent::secondary,
          LocomotiveColorComponent::new);

  @Override
  public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer,
      TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
    consumer.accept(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_PRIMARY)
        .withStyle(ChatFormatting.AQUA)
        .append(CommonComponents.SPACE)
        .append(Component.translatable("color.minecraft." + primary.getName())
            .withStyle(ChatFormatting.GRAY)));

    consumer.accept(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_SECONDARY)
        .withStyle(ChatFormatting.AQUA)
        .append(CommonComponents.SPACE)
        .append(Component.translatable("color.minecraft." + secondary.getName())
            .withStyle(ChatFormatting.GRAY)));
  }
}
