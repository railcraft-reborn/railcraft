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
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public record LocomotiveWhistlePitchComponent(float whistlePitch) implements TooltipProvider {

  public static LocomotiveWhistlePitchComponent NO_WHISTLE =
      new LocomotiveWhistlePitchComponent(-1);

  public static final Codec<LocomotiveWhistlePitchComponent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          Codec.FLOAT.fieldOf(CompoundTagKeys.WHISTLE_PITCH).forGetter(
              LocomotiveWhistlePitchComponent::whistlePitch)
      ).apply(instance, LocomotiveWhistlePitchComponent::new));

  public static final StreamCodec<FriendlyByteBuf, LocomotiveWhistlePitchComponent> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.FLOAT, LocomotiveWhistlePitchComponent::whistlePitch,
          LocomotiveWhistlePitchComponent::new);

  @Override
  public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer,
      TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
    if (whistlePitch < 0) {
      consumer.accept(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_NO_WHISTLE)
          .withStyle(ChatFormatting.GRAY));
    } else {
      consumer.accept(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_WHISTLE)
          .withStyle(ChatFormatting.AQUA)
          .append(CommonComponents.SPACE)
          .append(Component.literal(String.format("%.2f", whistlePitch))
              .withStyle(ChatFormatting.GRAY)));
    }
  }
}
